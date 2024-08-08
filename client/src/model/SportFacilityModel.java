package model;

import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface SportFacilityModel {
    boolean createFacility(String title, String description) throws IOException, SQLException;
    List<Schedule> getAllSchedules() throws IOException, SQLException;
    void reserveFacility(Schedule schedule) throws IOException, SQLException;
    boolean logIn(String username, String password) throws IOException, SQLException;
}
