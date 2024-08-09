package model;

import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface SportFacilityModel {
    boolean createFacility(Facility facility) throws IOException, SQLException;
    boolean reserveFacility(Schedule schedule) throws IOException, SQLException;
    boolean logIn(String username, String password, boolean admin) throws IOException, SQLException;
    boolean editFacility(Facility facility) throws IOException, SQLException;
    boolean deleteFacility(Facility facility) throws IOException, SQLException;
    List<Facility> getAllFacilities() throws IOException, SQLException;
    List<Schedule> getSchedulesForDate(LocalDate date);
}
