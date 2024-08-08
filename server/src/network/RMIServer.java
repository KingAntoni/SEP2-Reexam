package network;

import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface RMIServer extends Remote {
    boolean createFacility(Facility facility) throws RemoteException, IOException, SQLException;
    List<Schedule> getAllSchedules() throws RemoteException, IOException, SQLException;
    void reserveFacility(Schedule schedule) throws RemoteException, IOException, SQLException;
}
