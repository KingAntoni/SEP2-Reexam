package client.network;

import java.io.IOException;
import java.sql.SQLException;

public interface Client {
    boolean createFacility(String name, String description) throws IOException, SQLException;
}
