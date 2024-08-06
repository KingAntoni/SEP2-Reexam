package database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DATABASE_NAME = "facilities.db";
    private static final String DATABASE_FOLDER = "/database/";
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

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Facility (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Facility table created or already exists.");
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
