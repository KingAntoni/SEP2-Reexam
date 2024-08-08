package network;

import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RMIClient extends UnicastRemoteObject implements Client {
    private RMIServer server;

    public RMIClient() throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        server = (RMIServer) registry.lookup("Server");
        if (server == null) {
            throw new RemoteException("Failed to lookup server");
        }
        System.out.println("Connected to RMI server");
    }


    @Override
    public boolean createFacility(String title, String description) throws IOException, SQLException {
        Facility facility = new Facility(title, description);
        return server.createFacility(facility);
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
    public boolean editFacility(Facility facility) throws IOException, SQLException {
        return server.editFacility(facility);
    }

    @Override
    public boolean deleteFacility(Facility facility) throws IOException, SQLException {
        return server.deleteFacility(facility);
    }

    @Override
    public List<Facility> readAllFacilities() throws IOException, SQLException {
        return server.readAllFacilities();
    }

    public static void main(String[] args) {
        try {
            RMIClient client = new RMIClient();
            Facility facility = new Facility("TestFacility", "This is a test description.");
            boolean result = client.createFacility(facility.getTitle(), facility.getDescription());
            System.out.println("Facility added: " + result);
        } catch (NotBoundException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
