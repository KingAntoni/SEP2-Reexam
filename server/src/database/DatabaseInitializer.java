package database;

public class DatabaseInitializer {
    public static void main(String[] args) {
        try {
            // Explicitly load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            //DatabaseManager.initializeDatabase();
            //DatabaseManager.createTables();
            DatabaseManager.createInitialUsers();
            System.out.println("Database and tables initialized.");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver.");
            e.printStackTrace();
        }
    }
}
