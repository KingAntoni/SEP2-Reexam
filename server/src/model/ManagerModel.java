package model;

import util.Subject;
import java.rmi.RemoteException;

public interface ManagerModel extends Subject {
    void sendNotification() throws RemoteException;
}
