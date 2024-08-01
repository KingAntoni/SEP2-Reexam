package client.model;

import client.network.Client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;


public class SportFacilityModelManager implements SportFacilityModel {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private Client client;

    @Override
    public boolean createFacility(String title, String description) throws IOException, SQLException {
        return client.createFacility(title,description);
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) throws IOException, SQLException {

    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {

    }
}
