package core;

import model.SportFacilityModel;
import views.editFacility.EditFacilityViewModel;
import views.facilityMenu.FacilityMenuViewModel;
import views.addFacility.FacilityViewModel;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class ViewModelFactory {
    private static ViewModelFactory instance;

    private FacilityViewModel createFacilityVM;
    private FacilityMenuViewModel facilityMenuVM;
    private EditFacilityViewModel editFacilityVM;
    private SportFacilityModel sportFacilityModel;

    private ViewModelFactory() throws IOException, NotBoundException, SQLException {
        sportFacilityModel = ModelFactory.getInstance().getModel();
        createFacilityVM = new FacilityViewModel(sportFacilityModel);
        facilityMenuVM = new FacilityMenuViewModel(sportFacilityModel);
        editFacilityVM = new EditFacilityViewModel(sportFacilityModel);
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

    public FacilityMenuViewModel getFacilityMenuVM() {
        return facilityMenuVM;
    }

    public EditFacilityViewModel getEditFacilityVM() {
        return editFacilityVM;
    }

    public SportFacilityModel getSportFacilityModel() {
        return sportFacilityModel;
    }
}
