
package shared.networking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface RMIServer extends Remote {
    boolean addFacility(String title, String description)
            throws IOException, SQLException;
}
