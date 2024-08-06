package network;

import transferObjects.Facility;

import java.io.IOException;
import java.rmi.Remote;
import java.sql.SQLException;

public interface Client extends Remote {
    boolean createFacility(Facility facility) throws IOException, SQLException;
}
