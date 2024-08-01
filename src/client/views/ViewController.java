package client.views;

import java.io.IOException;
import java.sql.SQLException;

public interface ViewController {
    void init()
            throws IOException, SQLException;
}
