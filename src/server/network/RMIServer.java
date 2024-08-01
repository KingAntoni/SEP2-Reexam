package server.network;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface RMIServer extends Remote {
    boolean createFacility(String name, String description) throws IOException, SQLException;
}
