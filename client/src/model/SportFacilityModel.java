package model;

import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface SportFacilityModel {
    boolean createFacility(String title, String description) throws IOException, SQLException;
    List<Schedule> getSchedulesForDate(LocalDate date, int facilityId);
    void addSchedule(Schedule schedule);

    boolean editFacility(Facility facility) throws IOException, SQLException;
    boolean deleteFacility(Facility facility) throws IOException, SQLException;
    List<Facility> getAllFacilities() throws IOException, SQLException;  // Add this line

}
