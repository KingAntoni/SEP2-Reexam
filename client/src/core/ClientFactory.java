package core;

import network.Client;
import network.RMIClient;

import java.io.IOException;
import java.rmi.NotBoundException;

public class ClientFactory {

    private Client client;
    private static ClientFactory instance;

    private ClientFactory() {}

    public static synchronized ClientFactory getInstance() {
        if (instance == null) {
            instance = new ClientFactory();
        }
        return instance;
    }

    public Client getClient() throws IOException, NotBoundException {
        if (client == null) {
            client = new RMIClient();
        }
        return client;
    }
}
