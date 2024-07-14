package server.Data;

import java.util.HashMap;
import java.util.Map;

public class FacilitiesData {
    private Map<String, server.Model.Facility> facilities = new HashMap<>();

    public server.Model.Facility getFieldsByName(String name) {
        return facilities.get(name);
    }

    public void create(String name, String description) {
        facilities.put(name, new server.Model.Facility(name, description));
    }
}
