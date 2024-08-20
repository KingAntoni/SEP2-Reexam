package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

public class Manager implements ManagerModel{

    private PropertyChangeSupport support= new PropertyChangeSupport(this);

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    @Override
    public void sendNotification() throws RemoteException {

    }
}