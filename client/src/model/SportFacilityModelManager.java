package model;

import network.Client;
import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SportFacilityModelManager implements SportFacilityModel {
    private final Client client;
    private int facilityId;
    private User user = new User(null, null, false);

    public SportFacilityModelManager(Client client) {
        this.client = client;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    @Override
    public boolean createFacility(Facility facility) throws IOException, SQLException {
        return client.createFacility(facility);
    }

    @Override
    public boolean reserveFacility(Schedule schedule) throws IOException, SQLException {
        try {
            if(!user.isAdmin()) {
                schedule.setUser(user);
            }
            return client.reserveFacility(schedule);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancelReserveFacility(Schedule schedule) throws IOException, SQLException {
        if(schedule.getUser().getUsername().equals(null)) {
            schedule.setUser(user);
        }
        return client.cancelReserveFacility(schedule);
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
        return client.readAllFacilities();
    }

    @Override
    public List<User> getAllUsers() throws SQLException, IOException {
        return client.readAllUsers();
    }

    @Override
    public List<Schedule> getSchedulesForDate(LocalDate date) {
        try {
            return client.getSchedulesForDate(date, facilityId);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
