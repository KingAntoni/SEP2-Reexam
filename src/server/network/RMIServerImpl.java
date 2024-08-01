package server.network;

import server.model.Manager;
import server.network.RMIServer;
import server.database.FacilitiesData;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class RMIServerImpl implements RMIServer {

    private Manager manager;
    private FacilitiesData facilitiesData;

    public RMIServerImpl(Manager manager) throws SQLException {
        this.manager = manager;
        this.facilitiesData = FacilitiesData.getInstance();
    }

    public void startServer() throws IOException, AlreadyBoundException, SQLException {
        UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("server", this);
    }

    @Override
    public boolean createFacility(String name, String description) throws IOException, SQLException {
        if (facilitiesData.getFacilityByName(name) == null) {
            if (name == null || description == null) {
                return false;
            }
            facilitiesData.create(name, description);
            return true;
        } else {
            return false;
        }
    }
}
