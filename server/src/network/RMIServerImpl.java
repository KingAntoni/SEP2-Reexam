package network;

import database.DatabaseManager;
import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {

    public RMIServerImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean createFacility(Facility facility) throws RemoteException, IOException, SQLException {
        System.out.println("Received facility creation request:");
        System.out.println(facility);
        return storeFacilityInDatabase(facility);
    }

    private boolean storeFacilityInDatabase(Facility facility) {
        String insertFacilitySQL = "INSERT INTO Facility (title, description) VALUES (?, ?)";
        String insertUserSQL = "INSERT INTO User (username, password) VALUES (?, ?)";
        String insertScheduleSQL = "INSERT INTO Schedule (startTime, endTime, userId, facilityId) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            // Start a transaction
            conn.setAutoCommit(false);

            // Insert the facility
            int facilityId;
            try (PreparedStatement pstmt = conn.prepareStatement(insertFacilitySQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, facility.getTitle());
                pstmt.setString(2, facility.getDescription());
                pstmt.executeUpdate();

                var rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    facilityId = rs.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // Insert the schedules and users
            for (Schedule schedule : facility.getSchedules()) {
                int userId;
                try (PreparedStatement pstmtUser = conn.prepareStatement(insertUserSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmtUser.setString(1, schedule.getUser().getUsername());
                    pstmtUser.setString(2, schedule.getUser().getPassword());
                    pstmtUser.executeUpdate();

                    var rsUser = pstmtUser.getGeneratedKeys();
                    if (rsUser.next()) {
                        userId = rsUser.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement pstmtSchedule = conn.prepareStatement(insertScheduleSQL)) {
                    pstmtSchedule.setString(1, schedule.getStartTime().toString());
                    pstmtSchedule.setString(2, schedule.getEndTime().toString());
                    pstmtSchedule.setInt(3, userId);
                    pstmtSchedule.setInt(4, facilityId);
                    pstmtSchedule.executeUpdate();
                }
            }

            // Commit the transaction
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) throws RemoteException, IOException, SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT Schedule.startTime, Schedule.endTime, User.username, User.password FROM Schedule " +
                "JOIN User ON Schedule.userId = User.id " +
                "WHERE DATE(Schedule.startTime) = ? AND Schedule.facilityId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, date.toString());
            pstmt.setInt(2, facilityId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime startTime = LocalDateTime.parse(rs.getString("startTime"));
                    LocalDateTime endTime = LocalDateTime.parse(rs.getString("endTime"));
                    User user = new User(rs.getString("username"), rs.getString("password"));
                    schedules.add(new Schedule(startTime, endTime, user, facilityId));
                }
            }
        }
        return schedules;
    }

    @Override
    public void addSchedule(Schedule schedule) throws RemoteException, IOException, SQLException {
        String insertUserSQL = "INSERT INTO User (username, password) VALUES (?, ?)";
        String insertScheduleSQL = "INSERT INTO Schedule (startTime, endTime, userId, facilityId) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            int userId;
            try (PreparedStatement pstmtUser = conn.prepareStatement(insertUserSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmtUser.setString(1, schedule.getUser().getUsername());
                pstmtUser.setString(2, schedule.getUser().getPassword());
                pstmtUser.executeUpdate();

                var rsUser = pstmtUser.getGeneratedKeys();
                if (rsUser.next()) {
                    userId = rsUser.getInt(1);
                } else {
                    conn.rollback();
                    return;
                }
            }

            try (PreparedStatement pstmtSchedule = conn.prepareStatement(insertScheduleSQL)) {
                pstmtSchedule.setString(1, schedule.getStartTime().toString());
                pstmtSchedule.setString(2, schedule.getEndTime().toString());
                pstmtSchedule.setInt(3, userId);
                pstmtSchedule.setInt(4, schedule.getFacilityId());
                pstmtSchedule.executeUpdate();
            }

            conn.commit();
        }
    }

    public static void main(String[] args) {
        try {
            // Create and export a registry instance on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            RMIServerImpl server = new RMIServerImpl();
            registry.rebind("Server", server);

            System.out.println("RMI Server is running...");
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
