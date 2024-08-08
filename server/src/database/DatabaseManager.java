package database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {
    private static final String DATABASE_NAME = "facilities.db";
    private static final String DATABASE_FOLDER = "src/database/";
    private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_FOLDER + DATABASE_NAME;

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
}
