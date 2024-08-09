package network;

import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RMIServer extends Remote {
    boolean createFacility(Facility facility) throws RemoteException, IOException, SQLException;
    boolean login(User user) throws RemoteException, IOException, SQLException;
    boolean reserveFacility(Schedule schedule) throws RemoteException, IOException, SQLException;
    boolean editFacility(Facility facility) throws RemoteException, IOException, SQLException;
    boolean deleteFacility(Facility facility) throws RemoteException, IOException, SQLException;
    List<Facility> readAllFacilities() throws RemoteException, IOException, SQLException;
    void addSchedule(Schedule schedule) throws RemoteException, IOException, SQLException;
    List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) throws RemoteException, IOException, SQLException;
}
