package Client.viewModel;

import Client.Networking.Client;
import Server.Model.Facility;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class FacilityViewModel {
    private Client client;
    private PropertyChangeSupport support;

    public FacilityViewModel(Client client) {
        this.client = client;
        this.support = new PropertyChangeSupport(this);
    }

    public void addFacility(String name, String description) throws IOException, SQLException {
        client.addFacility(name, description);
        support.firePropertyChange("FacilityAdded", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
