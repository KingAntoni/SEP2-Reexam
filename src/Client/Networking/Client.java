package Client.Networking;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface Client {
    void addFacility(String name, String description) throws IOException, SQLException;
}
