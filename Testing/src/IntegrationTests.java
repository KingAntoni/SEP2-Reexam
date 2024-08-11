import network.Client;
import network.RMIClient;
import org.junit.Before;
import org.junit.Test;
import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IntegrationTests {

    private Client client;

    @Before
    public void setUp() throws IOException, NotBoundException {
        client = new RMIClient();
    }

    @Test
    public void testCreateFacility() throws IOException, SQLException {
        Facility facility = new Facility("Gym", "A place to exercise");
        boolean result = client.createFacility(facility);
        assertTrue(result);
    }

    @Test
    public void testReserveFacility() throws IOException, SQLException {
        User user = new User("user1", "password1", false);
        Schedule schedule = new Schedule(LocalDateTime.now(), LocalDateTime.now().plusHours(2), user, 2);
        boolean result = client.reserveFacility(schedule);
        assertTrue(result);
    }

    @Test
    public void testCancelReserveFacility() throws IOException, SQLException {
        User user = new User("user1", "password1", false);
        Schedule schedule = new Schedule(LocalDateTime.parse("2024-08-11T19:27:51.732565900"), LocalDateTime.parse("2024-08-11T21:27:51.735569200"), user, 2);
        boolean result = client.cancelReserveFacility(schedule);
        assertTrue(result);
    }

    @Test
    public void testEditFacility() throws IOException, SQLException {
        Facility facility = new Facility(10, "Gym", "Updated description");
        boolean result = client.editFacility(facility);
        assertTrue(result);
    }

    @Test
    public void testDeleteFacility() throws IOException, SQLException {
        Facility facility = new Facility(11, "Gym", "A place to exercise");
        boolean result = client.deleteFacility(facility);
        assertTrue(result);
    }

    @Test
    public void testReadAllFacilities() throws IOException, SQLException {
        List<Facility> result = client.readAllFacilities();
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0) instanceof Facility);
    }

    @Test
    public void testGetSchedulesForDate() throws IOException, SQLException {
        LocalDate date = LocalDate.now();
        int facilityId = 2;
        List<Schedule> result = client.getSchedulesForDate(date, facilityId);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0) instanceof Schedule);
    }

    @Test
    public void testLogIn() throws IOException, SQLException {
        User user = new User("admin1", "password1", true);
        boolean result = client.logIn(user);
        assertTrue(result);
    }
}
