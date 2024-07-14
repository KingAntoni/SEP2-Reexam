package Server;

import server.Networking.RMIServerImpl;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            RMIServerImpl server = new RMIServerImpl();
            server.startServer();
        } catch (RemoteException | IOException | AlreadyBoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
