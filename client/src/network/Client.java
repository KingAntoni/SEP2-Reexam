package network;

import transferObjects.Schedule;
import transferObjects.Facility;


import java.io.IOException;
import java.rmi.Remote;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public interface Client extends Remote {
    boolean createFacility(String title, String description) throws IOException, SQLException;
    List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) throws IOException, SQLException;
    void addSchedule(Schedule schedule) throws IOException, SQLException;
}
