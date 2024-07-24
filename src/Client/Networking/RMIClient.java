package Client.Networking;

import Server.Networking.RMIServer;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class RMIClient implements Client {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private RMIServer server;
    private String username;

    public RMIClient() throws RemoteException, NotBoundException, MalformedURLException {
        UnicastRemoteObject.exportObject((Remote) this, 0);
        server = (RMIServer) Naming.lookup("rmi://localhost:1099/Server");
    }

    @Override
    public void addFacility(String name, String description) throws IOException, SQLException {
        server.addField(name, description);
    }

    public static void main(String[] args) {
        try {
            RMIClient client = new RMIClient();
            client.addFacility("TestFacility", "This is a test description.");
            System.out.println("Facility added.");
        } catch (NotBoundException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
