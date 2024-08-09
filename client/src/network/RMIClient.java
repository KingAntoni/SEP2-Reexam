package network;

import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class RMIClient extends UnicastRemoteObject implements Client {
    private RMIServer server;

    public RMIClient() throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        server = (RMIServer) registry.lookup("Server");
    }

    @Override
    public List<Schedule> getAllSchedules() throws IOException, SQLException {
        return server.getAllSchedules();
    }

    @Override
    public boolean reserveFacility(Schedule schedule) throws IOException, SQLException {
        return server.reserveFacility(schedule);
    }

    @Override
    public boolean createFacility(Facility facility) throws IOException, SQLException {
        return server.createFacility(facility);
    }

    @Override
    public boolean logIn(User user) throws IOException, SQLException {
        return server.login(user);
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
