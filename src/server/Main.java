package server;

import server.network.RMIServerImpl;
import server.model.Manager;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Manager manager = new Manager(); // Initialize the manager
            RMIServerImpl server = new RMIServerImpl(manager);
            server.startServer();
        } catch (IOException | AlreadyBoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
