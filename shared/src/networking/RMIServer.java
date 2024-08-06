
package networking;

import java.io.IOException;
import java.rmi.Remote;
import java.sql.SQLException;

public interface RMIServer extends Remote {
    boolean addFacility(String title, String description)
            throws IOException, SQLException;
}
