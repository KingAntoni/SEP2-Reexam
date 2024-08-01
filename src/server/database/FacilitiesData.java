package server.database;

import server.model.Facility;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FacilitiesData {
    private static FacilitiesData instance;
    private Map<String, Facility> facilities;

    private FacilitiesData() {
        facilities = new HashMap<>();
    }

    public static FacilitiesData getInstance() {
        if (instance == null) {
            instance = new FacilitiesData();
        }
        return instance;
    }

    public void create(String name, String description) throws SQLException {
        Facility facility = new Facility(name, description);
        facilities.put(name, facility);
    }

    public Facility getFacilityByName(String name) {
        return facilities.get(name);
    }
}