package client.core;

import client.views.CreateFacility.RecipeVM;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class ViewModelFactory {
    private static ViewModelFactory instance;

    private CreateFacilityVM createFacilityVM;

    private ViewModelFactory() throws IOException, NotBoundException, SQLException
    {
        createFacilityVM = new CreateFacilityVM();

    }

    public static synchronized ViewModelFactory getInstance()
            throws IOException, NotBoundException, SQLException
    {
        if(instance==null)
        {
            instance = new ViewModelFactory();
        }
        return instance;
    }

    public createFacilityVM getLogInVM()
    {
        return createFacilityVM;
    }

}