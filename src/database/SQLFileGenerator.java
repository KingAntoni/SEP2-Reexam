package database;

import java.io.FileWriter;
import java.io.IOException;

public class SQLFileGenerator {
    public static void main(String[] args) {
        // Define the SQL statements
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

        // Concatenate all SQL statements into a single string
        String allTablesSQL = userTable + "\n\n" + facilityTable + "\n\n" + scheduleTable;

        // Specify the file path where the SQL file will be created
        String filePath = "schema.sql";

        // Write the SQL statements to the .sql file
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(allTablesSQL);
            System.out.println("SQL file created successfully at " + filePath);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
