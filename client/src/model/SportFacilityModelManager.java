package model;

import network.Client;
import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SportFacilityModelManager implements SportFacilityModel {
    private final Client client;

    public SportFacilityModelManager(Client client) {
        this.client = client;
    }

    @Override
    public boolean createFacility(String title, String description) throws IOException, SQLException {
        return client.createFacility(title, description);
    }

    @Override
    public List<Schedule> getAllSchedules() throws IOException, SQLException {
        try {
            return client.getAllSchedules();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void reserveFacility(Schedule schedule) throws IOException, SQLException {
        try {
            client.reserveFacility(schedule);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
