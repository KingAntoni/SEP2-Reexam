package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseCon {

    private static DatabaseCon instance;
    private static Object lock = new Object();
    private Connection connection;

    private DatabaseCon() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SEP2Reexam?currentSchema=public", "", "");
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseCon getInstance() throws SQLException {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseCon();
                }
            }
        }
        return instance;
    }
}