package client.model;

import shared.util.Subject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface SportFacilityModel extends Subject {
    boolean createFacility(String title, String description)
            throws IOException, SQLException;
}
