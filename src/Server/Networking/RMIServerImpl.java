package server.Networking;

import Server.Data.FieldsData;
import Server.Networking.RMIServer;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {
    private FieldsData fieldsData;

    public RMIServerImpl() throws RemoteException {
        super();
        fieldsData = new FieldsData();
    }

    public void startServer() throws IOException, AlreadyBoundException, SQLException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("Server", this);
        System.out.println("Server started...");
    }

    @Override
    public boolean addField(String name, String description) throws IOException, SQLException {
        if (fieldsData.getFieldsByName(name) == null) {
            if (name == null || description == null) {
                return false;
            }
            fieldsData.create(name, description);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addFacility(String name, String description) {

    }
}
