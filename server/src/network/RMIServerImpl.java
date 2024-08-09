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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {

    private boolean admin;

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
        admin = user.isAdmin();
        return checkUserCredentials(user);
    }

    private boolean checkUserCredentials(User user) {
        String query = "SELECT * FROM User WHERE username = ? AND password = ? AND admin = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setBoolean(3, user.isAdmin());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean reserveFacility(Schedule schedule) throws RemoteException, IOException, SQLException {
        storeScheduleInDatabase(schedule);
        return true;
    }

    @Override
    public boolean cancelReserveFacility(Schedule schedule) throws IOException, SQLException {
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

    @Override
    public boolean editFacility(Facility facility) throws RemoteException, IOException, SQLException {
        String updateFacilitySQL = "UPDATE Facility SET title = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateFacilitySQL)) {
            pstmt.setString(1, facility.getTitle());
            pstmt.setString(2, facility.getDescription());
            pstmt.setInt(3, facility.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteFacility(Facility facility) throws RemoteException, IOException, SQLException {
        String deleteFacilitySQL = "DELETE FROM Facility WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteFacilitySQL)) {
            pstmt.setInt(1, facility.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Facility> readAllFacilities() throws RemoteException, IOException, SQLException {
        List<Facility> facilities = new ArrayList<>();
        String query = "SELECT * FROM Facility";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Facility facility = new Facility(id, title, description);
                facilities.add(facility);
                System.out.println("Server retrieved facility: " + facility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facilities;
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
