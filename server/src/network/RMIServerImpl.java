package network;

import database.DatabaseManager;
import transferObjects.Facility;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {

    public RMIServerImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean createFacility(Facility facility) throws RemoteException, IOException, SQLException {
        System.out.println("Received facility creation request:");
        System.out.println(facility);
        storeFacilityInDatabase(facility);
        return true;
    }

    private void storeFacilityInDatabase(Facility facility) {
        String sql = "INSERT INTO Facility (title, description) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, facility.getTitle());
            pstmt.setString(2, facility.getDescription());
            pstmt.executeUpdate();
            System.out.println("Facility stored in database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Create and export a registry instance on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            RMIServerImpl server = new RMIServerImpl();
            registry.rebind("Server", server);

            System.out.println("RMI Server is running...");
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
