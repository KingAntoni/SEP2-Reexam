package network;

import transferObjects.Facility;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface RMIServer extends Remote {
    boolean createFacility(Facility facility) throws RemoteException, IOException, SQLException;
}
