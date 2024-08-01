package client.core;

import client.network.Client;
import client.network.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientFactory {

    private Client client;
    private static ClientFactory instance;

    private  ClientFactory()
    {

    }
    public static  synchronized  ClientFactory getInstance()
    {
        if(instance ==null)
        {
            instance= new ClientFactory();
        }
        return instance;
    }
    public Client getClient() throws RemoteException, NotBoundException {
        if(client ==null)
        {
            client = new RMIClient();
        }
        return client;
    }
}