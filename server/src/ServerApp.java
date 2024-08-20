import network.RMIServer;
import network.RMIServerImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerApp {
    public static void main(String[] args) {
        try {
            // Create and export a registry instance on port 1099
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
