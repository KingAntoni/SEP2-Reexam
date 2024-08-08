package core;

import model.SportFacilityModel;
import views.addFacility.FacilityViewModel;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class ViewModelFactory {
    private static ViewModelFactory instance;

    private FacilityViewModel createFacilityVM;
    private SportFacilityModel sportFacilityModel;

    private ViewModelFactory() throws IOException, NotBoundException, SQLException {
        SportFacilityModel model = ModelFactory.getInstance().getModel();
        createFacilityVM = new FacilityViewModel(model);
        sportFacilityModel = ModelFactory.getInstance().getModel();
    }

    public static synchronized ViewModelFactory getInstance() throws IOException, NotBoundException, SQLException {
        if (instance == null) {
            instance = new ViewModelFactory();
        }
        return instance;
    }

    public FacilityViewModel getFacilityVM() {
        return createFacilityVM;
    }

    public SportFacilityModel getSportFacilityModel() {
        return sportFacilityModel;
    }
}
