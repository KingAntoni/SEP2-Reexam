package client.views.addFacility;

import client.network.Client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.sql.SQLException;

public class FacilityViewModel {
    private Client client;
    private PropertyChangeSupport support;

    public FacilityViewModel(Client client) {
        this.client = client;
        this.support = new PropertyChangeSupport(this);
    }

    public void addFacility(String name, String description) throws IOException, SQLException {
        client.createFacility(name, description);
        support.firePropertyChange("FacilityAdded", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
