package model;

import network.Client;
import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SportFacilityModelManager implements SportFacilityModel {
    private final Client client;
    private User user = new User(null,null,false);

    public SportFacilityModelManager(Client client) {
        this.client = client;
    }

    @Override
    public boolean createFacility(String title, String description) throws IOException, SQLException {
        return client.createFacility(new Facility(title, description));
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
            schedule.setUser(user);
            client.reserveFacility(schedule);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean logIn(String username, String password, boolean admin) throws IOException, SQLException {
        try {
            user.setUsername(username);
            user.setPassword(password);
            user.setAdmin(admin);
            return client.logIn(new User(username, password, admin));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
