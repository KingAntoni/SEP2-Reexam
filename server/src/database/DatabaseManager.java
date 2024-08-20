package database;

import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DATABASE_NAME = "facilities.db";
    private static final String DATABASE_FOLDER = "src/database/";
    private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_FOLDER + DATABASE_NAME;
    private boolean admin;

    static {
        try {
            // Register the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void createTables() {
        String userTable = "CREATE TABLE IF NOT EXISTS User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "admin BOOLEAN NOT NULL" +
                ");";

        String facilityTable = "CREATE TABLE IF NOT EXISTS Facility (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL" +
                ");";

        String scheduleTable = "CREATE TABLE IF NOT EXISTS Schedule (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "startTime TEXT NOT NULL," +
                "endTime TEXT NOT NULL," +
                "userId INTEGER NOT NULL," +
                "facilityId INTEGER NOT NULL," +
                "FOREIGN KEY (userId) REFERENCES User(id)," +
                "FOREIGN KEY (facilityId) REFERENCES Facility(id)" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(userTable);
            stmt.execute(facilityTable);
            stmt.execute(scheduleTable);
            System.out.println("Tables created or already exist.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createInitialUsers() {
        String insertUserSQL = "INSERT INTO User (username, password, admin) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {

            conn.setAutoCommit(false);

            // Create 2 admin users
            pstmt.setString(1, "admin1");
            pstmt.setString(2, "password1");
            pstmt.setBoolean(3, true);
            pstmt.executeUpdate();

            pstmt.setString(1, "admin2");
            pstmt.setString(2, "password2");
            pstmt.setBoolean(3, true);
            pstmt.executeUpdate();

            // Create 8 normal users
            for (int i = 1; i <= 8; i++) {
                pstmt.setString(1, "user" + i);
                pstmt.setString(2, "password" + i);
                pstmt.setBoolean(3, false);
                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Initial users created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() {
        try {
            Path path = Paths.get(DATABASE_FOLDER);
            File dir = path.toFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dbFile = new File(dir, DATABASE_NAME);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
                System.out.println("Database file created: " + dbFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createFacility(Facility facility) throws RemoteException, IOException, SQLException {
        System.out.println("Received facility creation request:");
        System.out.println(facility);
        return storeFacilityInDatabase(facility);
    }


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

    public boolean reserveFacility(Schedule schedule) throws RemoteException, IOException, SQLException {
        return storeScheduleInDatabase(schedule);
    }


    public boolean cancelReserveFacility(Schedule schedule) throws IOException, SQLException {
        String findScheduleSQL = "SELECT COUNT(*) FROM Schedule WHERE facilityId = ? AND startTime = ? AND endTime = ?";
        String deleteScheduleSQL = "DELETE FROM Schedule WHERE facilityId = ? AND startTime = ? AND endTime = ?";

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            // Check if the Schedule exists
            boolean scheduleExists;
            try (PreparedStatement pstmtFindSchedule = conn.prepareStatement(findScheduleSQL)) {
                pstmtFindSchedule.setInt(1, schedule.getFacilityId());
                pstmtFindSchedule.setObject(2, schedule.getStartTime());
                pstmtFindSchedule.setObject(3, schedule.getEndTime());

                try (ResultSet rsSchedule = pstmtFindSchedule.executeQuery()) {
                    if (rsSchedule.next()) {
                        scheduleExists = rsSchedule.getInt(1) > 0;
                    } else {
                        scheduleExists = false;
                    }
                }
            }

            if (!scheduleExists) {
                conn.rollback();
                throw new SQLException("Schedule with facilityId " + schedule.getFacilityId() +
                        ", startTime " + schedule.getStartTime() +
                        ", and endTime " + schedule.getEndTime() + " does not exist.");
            }

            // If the Schedule exists, delete it
            try (PreparedStatement pstmtDeleteSchedule = conn.prepareStatement(deleteScheduleSQL)) {
                pstmtDeleteSchedule.setInt(1, schedule.getFacilityId());
                pstmtDeleteSchedule.setObject(2, schedule.getStartTime());
                pstmtDeleteSchedule.setObject(3, schedule.getEndTime());
                pstmtDeleteSchedule.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle rollback in case of error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
        } finally {
            // Ensure the connection is closed
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
        return false;
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

    public List<User> readAllUsers() throws RemoteException, IOException, SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM User";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                boolean admin = rs.getBoolean("admin");

                User user = new User(username, null, admin);
                users.add(user);
                System.out.println("Server retrieved user: " + user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }



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
                    User user = new User(rs.getString("username"), null);
                    schedules.add(new Schedule(startTime, endTime, user, facilityId));
                }
            }
        }
        return schedules;
    }

    private boolean storeScheduleInDatabase(Schedule schedule) {
        String findUserIdSQL = "SELECT id FROM User WHERE username = ?";
        String checkFacilitySQL = "SELECT id FROM Facility WHERE id = ?";
        String insertScheduleSQL = "INSERT INTO Schedule (startTime, endTime, userId, facilityId) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            int userId;

            // Check if the User exists and retrieve the userId
            try (PreparedStatement pstmtFindUser = conn.prepareStatement(findUserIdSQL)) {
                pstmtFindUser.setString(1, schedule.getUser().getUsername());
                ResultSet rsUser = pstmtFindUser.executeQuery();

                if (rsUser.next()) {
                    userId = rsUser.getInt("id");
                } else {
                    conn.rollback();
                    throw new SQLException("User with username '" + schedule.getUser().getUsername() + "' does not exist.");
                }
            }

            // Check if the Facility exists
            try (PreparedStatement pstmtCheckFacility = conn.prepareStatement(checkFacilitySQL)) {
                pstmtCheckFacility.setInt(1, schedule.getFacilityId());
                ResultSet rsFacility = pstmtCheckFacility.executeQuery();
                if (!rsFacility.next()) {
                    conn.rollback();
                    throw new SQLException("Facility with id " + schedule.getFacilityId() + " does not exist.");
                }
            }

            // If both User and Facility exist, insert the Schedule
            try (PreparedStatement pstmtSchedule = conn.prepareStatement(insertScheduleSQL)) {
                pstmtSchedule.setObject(1, schedule.getStartTime());
                pstmtSchedule.setObject(2, schedule.getEndTime());
                pstmtSchedule.setInt(3, userId);
                pstmtSchedule.setInt(4, schedule.getFacilityId());
                pstmtSchedule.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle rollback in case of error
            if (conn != null) {
                try {
                    conn.rollback();
                    return false;
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
        } finally {
            // Ensure the connection is closed
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
        return false;
    }
}
