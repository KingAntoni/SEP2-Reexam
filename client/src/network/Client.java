package network;

import transferObjects.Schedule;
import transferObjects.Facility;
import transferObjects.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface Client extends Remote {
    boolean createFacility(Facility facility) throws IOException, SQLException;
    boolean reserveFacility(Schedule schedule) throws IOException, SQLException;
    boolean cancelReserveFacility(Schedule schedule) throws IOException, SQLException;
    boolean logIn(User user) throws RemoteException, IOException, SQLException;
    boolean editFacility(Facility facility) throws IOException, SQLException;
    boolean deleteFacility(Facility facility) throws IOException, SQLException;
    List<Facility> readAllFacilities() throws IOException, SQLException;
    List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) throws IOException, SQLException;
}
