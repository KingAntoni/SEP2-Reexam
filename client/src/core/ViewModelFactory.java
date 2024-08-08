package core;

import model.SportFacilityModel;
import views.addFacility.FacilityViewModel;
import views.facilitySchedule.FacilityScheduleViewModel;
import views.login.LoginViewModel;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class ViewModelFactory {
    private static ViewModelFactory instance;

    private FacilityViewModel createFacilityVM;
    private FacilityScheduleViewModel createFacilityScheduleVM;
    private LoginViewModel createLoginVM;

    private ViewModelFactory() throws IOException, NotBoundException, SQLException {
        SportFacilityModel model = ModelFactory.getInstance().getModel();
        createFacilityVM = new FacilityViewModel(model);
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

    public FacilityScheduleViewModel getFacilityScheduleVM() {
        return createFacilityScheduleVM;
    }

    public LoginViewModel getLoginVM() {
        return createLoginVM;
    }

}
