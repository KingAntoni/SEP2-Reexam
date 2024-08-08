package network;

import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RMIClient extends UnicastRemoteObject implements Client {
    private RMIServer server;
    private Client client;


    public RMIClient() throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        //server = (RMIServer) registry.lookup("Server");
    }

    @Override
    public List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) throws IOException, SQLException {
        return server.getSchedulesForDate(date, facilityId);
    }

    @Override
    public void addSchedule(Schedule schedule) throws IOException, SQLException {
        server.addSchedule(schedule);
    }

    @Override
    public boolean createFacility(String title, String description) throws IOException, SQLException {
        Facility facility = new Facility(title, description);
        return client.createFacility(title,description);
    }

    public static void main(String[] args) {
        try {
            RMIClient client = new RMIClient();
            // Test the client methods here if needed
        } catch (NotBoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
