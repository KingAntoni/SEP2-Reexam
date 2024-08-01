package client.core;

import client.model.SportFacilityModel;
import client.model.SportFacilityModelManager;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class ModelFactory {

    private static ModelFactory instance;

    private SportFacilityModel sportFacilityModel;

    private ModelFactory()
    {
    }
    public static synchronized ModelFactory getInstance()
    {
        if(instance== null)
            instance = new ModelFactory();
        return instance;
    }

    public SportFacilityModel getModel()
            throws IOException, NotBoundException, SQLException
    {
        if(sportFacilityModel==null)
            sportFacilityModel = new SportFacilityModelManager();
        return sportFacilityModel;
    }

}
