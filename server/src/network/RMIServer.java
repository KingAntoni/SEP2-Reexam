package network;

import transferObjects.Facility;
import transferObjects.Schedule;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RMIServer extends Remote {
    boolean createFacility(Facility facility) throws RemoteException, IOException, SQLException;
    List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) throws RemoteException, IOException, SQLException;
    void addSchedule(Schedule schedule) throws RemoteException, IOException, SQLException;
}
