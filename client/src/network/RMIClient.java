package network;

import transferObjects.Facility;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class RMIClient extends UnicastRemoteObject implements Client {
    private RMIServer server;

    public RMIClient() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        server = (RMIServer) registry.lookup("Server");
    }

    @Override
    public boolean createFacility(Facility facility) throws IOException, SQLException {
        return server.createFacility(facility);
    }

    public static void main(String[] args) {
        try {
            RMIClient client = new RMIClient();
            Facility facility = new Facility("TestFacility", "This is a test description.");
            boolean result = client.createFacility(facility);
            System.out.println("Facility added: " + result);
        } catch (NotBoundException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
