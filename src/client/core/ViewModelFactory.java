package client.core;

import client.network.Client;
import client.views.addFacility.FacilityViewModel;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class ViewModelFactory {
    private static ViewModelFactory instance;

    private FacilityViewModel createFacilityVM;

    private ViewModelFactory(Client client) throws IOException, NotBoundException, SQLException {
        createFacilityVM = new FacilityViewModel(client);
    }

    public static synchronized ViewModelFactory getInstance(Client client)
            throws IOException, NotBoundException, SQLException {
        if(instance == null) {
            instance = new ViewModelFactory(client);
        }
        return instance;
    }

    public FacilityViewModel getFacilityVM() {
        return createFacilityVM;
    }
}
