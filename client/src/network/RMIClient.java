package network;

import transferObjects.Facility;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RMIClient extends UnicastRemoteObject implements Client {
    private RMIServer server;

    public RMIClient() throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        server = (RMIServer) registry.lookup("Server");
        if (server == null) {
            throw new RemoteException("Failed to lookup server");
        }
        System.out.println("Connected to RMI server");
    }

    @Override
    public boolean createFacility(Facility facility) throws IOException, SQLException {
        return server.createFacility(facility);
    }

    @Override
    public boolean reserveFacility(Schedule schedule) throws IOException, SQLException {
        return server.reserveFacility(schedule);
    }

    @Override
    public boolean cancelReserveFacility(Schedule schedule) throws IOException, SQLException {
        return server.cancelReserveFacility(schedule);

    }

    @Override
    public boolean editFacility(Facility facility) throws IOException, SQLException {
        return server.editFacility(facility);
    }

    @Override
    public boolean deleteFacility(Facility facility) throws IOException, SQLException {
        return server.deleteFacility(facility);
    }

    @Override
    public List<Facility> readAllFacilities() throws IOException, SQLException {
        List<Facility> facilities = server.readAllFacilities();
        facilities.forEach(facility -> System.out.println("Client received facility: " + facility));
        return facilities;
    }

    @Override
    public List<Schedule> getSchedulesForDate(LocalDate date, int facilityId) throws IOException, SQLException {
        List<Schedule> schedules = server.getSchedulesForDate(date, facilityId);
        schedules.forEach(schedule -> System.out.println("Client received schedule: " + schedule));
        return schedules;
    }

    @Override
    public List<User> readAllUsers() throws SQLException, IOException {
        List<User> users = server.readAllUsers();
        users.forEach(user -> System.out.println("Client received facility: " + user));
        return users;
    }

    @Override
    public boolean logIn(User user) throws IOException, SQLException {
        return server.login(user);
    }
}
