package model;

import network.Client;
import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SportFacilityModelManager implements SportFacilityModel {
    private final Client client;

    public SportFacilityModelManager(Client client) {
        this.client = client;
    }

    @Override
    public boolean createFacility(String title, String description) throws IOException, SQLException {
        Facility facility = new Facility(title, description);
        return client.createFacility(title,description);
    }
    @Override
    public List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) {
        try {
            return client.getSchedulesForDate(date, facilityId);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addSchedule(Schedule schedule) {
        try {
            client.addSchedule(schedule);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean editFacility(Facility facility) throws IOException, SQLException {
        return client.editFacility(facility);
    }

    @Override
    public boolean deleteFacility(Facility facility) throws IOException, SQLException {
        return client.deleteFacility(facility);
    }

    @Override
    public List<Facility> getAllFacilities() throws IOException, SQLException {
        return client.readAllFacilities();  // Implement this method
    }
}
