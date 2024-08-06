package model;

import util.Subject;

import java.io.IOException;
import java.sql.SQLException;

public interface SportFacilityModel extends Subject {
    boolean createFacility(String title, String description) throws IOException, SQLException;
}
