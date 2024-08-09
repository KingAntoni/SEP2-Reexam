package network;

import database.DatabaseManager;
import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public boolean login(User user) throws RemoteException, IOException, SQLException {
        return checkUserCredentials(user);
    }

    private boolean checkUserCredentials(User user) {
        String query = "SELECT * FROM User WHERE username = ? AND password = ? AND admin = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setBoolean(3, user.isAdmin()); // Assuming User class has an isAdmin() method

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true; // User found with matching credentials and admin status
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // User not found or incorrect admin status
    }

    @Override
    public List<Schedule> getAllSchedules() throws RemoteException, IOException, SQLException {
        return fetchAllSchedulesFromDatabase();
    }

    @Override
    public boolean reserveFacility(Schedule schedule) throws RemoteException, IOException, SQLException {
        storeScheduleInDatabase(schedule);
        return true;
    }

    private boolean storeFacilityInDatabase(Facility facility) {
        String insertFacilitySQL = "INSERT INTO Facility (title, description) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertFacilitySQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);

            pstmt.setString(1, facility.getTitle());
            pstmt.setString(2, facility.getDescription());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            int facilityId;
            if (rs.next()) {
                facilityId = rs.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Schedule> fetchAllSchedulesFromDatabase() {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT s.id, s.startTime, s.endTime, u.username, f.title " +
                "FROM Schedule s " +
                "JOIN User u ON s.userId = u.id " +
                "JOIN Facility f ON s.facilityId = f.id";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Schedule schedule = new Schedule(
                        LocalDateTime.parse(rs.getString("startTime")),
                        LocalDateTime.parse(rs.getString("endTime")),
                        new User(rs.getString("username"), "",true), // password not needed here
                        rs.getInt("facilityId")
                );
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    private void storeScheduleInDatabase(Schedule schedule) {
        String insertUserSQL = "INSERT INTO User (username, password) VALUES (?, ?)";
        String insertScheduleSQL = "INSERT INTO Schedule (startTime, endTime, userId, facilityId) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            int userId;
            try (PreparedStatement pstmtUser = conn.prepareStatement(insertUserSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmtUser.setString(1, schedule.getUser().getUsername());
                pstmtUser.setString(2, schedule.getUser().getPassword());
                pstmtUser.executeUpdate();

                ResultSet rsUser = pstmtUser.getGeneratedKeys();
                if (rsUser.next()) {
                    userId = rsUser.getInt(1);
                } else {
                    conn.rollback();
                    throw new SQLException("Failed to insert user.");
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
        } catch (SQLException e) {
            e.printStackTrace();
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
