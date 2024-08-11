package network;

import database.DatabaseManager;
import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {

    private DatabaseManager dbm;

    public RMIServerImpl() throws RemoteException {
        super();
        dbm = new DatabaseManager(); // Initialize dbm here
    }

    @Override
    public boolean createFacility(Facility facility) throws RemoteException, IOException, SQLException {
        return dbm.createFacility(facility);
    }

    @Override
    public boolean login(User user) throws RemoteException, IOException, SQLException {
        return dbm.login(user);
    }

    @Override
    public boolean reserveFacility(Schedule schedule) throws RemoteException, IOException, SQLException {
        return dbm.reserveFacility(schedule);
    }

    @Override
    public boolean cancelReserveFacility(Schedule schedule) throws IOException, SQLException {
        return dbm.cancelReserveFacility(schedule);
    }

    @Override
    public boolean editFacility(Facility facility) throws RemoteException, IOException, SQLException {
        return dbm.editFacility(facility);
    }

    @Override
    public boolean deleteFacility(Facility facility) throws RemoteException, IOException, SQLException {
        return dbm.deleteFacility(facility);
    }

    @Override
    public List<Facility> readAllFacilities() throws RemoteException, IOException, SQLException {
        return dbm.readAllFacilities();
    }

    @Override
    public List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) throws RemoteException, IOException, SQLException {
        return dbm.getSchedulesForDate(date, facilityId);
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            RMIServerImpl server = new RMIServerImpl();
            registry.rebind("Server", server);
            System.out.println("RMI Server is running...");
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
