package network;

import transferObjects.Schedule;
import transferObjects.Facility;
import transferObjects.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface Client extends Remote {
    boolean createFacility(String title, String description) throws IOException, SQLException;
    List<Schedule> getAllSchedules() throws IOException, SQLException;
    boolean reserveFacility(Schedule schedule) throws IOException, SQLException;
    boolean login(User user) throws RemoteException, IOException, SQLException;
}
