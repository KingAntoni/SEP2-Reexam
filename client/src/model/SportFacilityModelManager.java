package model;

import network.Client;
import transferObjects.Facility;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.sql.SQLException;

public class SportFacilityModelManager implements SportFacilityModel {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private Client client;

    public SportFacilityModelManager(Client client) {
        this.client = client;
    }

    @Override
    public boolean createFacility(String title, String description) throws IOException, SQLException {
        Facility facility = new Facility(title, description);
        return client.createFacility(facility);
    }

    @Override
    public void addListener(String eventName, java.beans.PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, java.beans.PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
