package core;

import model.SportFacilityModel;
import views.editFacility.EditFacilityViewModel;
import views.facilityMenu.FacilityMenuViewModel;
import views.addFacility.FacilityViewModel;
import views.facilityMenuUser.FacilityMenuUserViewModel;
import views.facilitySchedule.FacilityScheduleViewModel;
import views.facilityScheduleAdmin.FacilityScheduleAdminViewModel;
import views.login.LoginViewModel;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class ViewModelFactory {
    private static ViewModelFactory instance;

    private FacilityViewModel createFacilityVM;
    private FacilityScheduleViewModel createFacilityScheduleVM;
    private LoginViewModel createLoginVM;
    private FacilityMenuViewModel facilityMenuVM;
    private EditFacilityViewModel editFacilityVM;
    private FacilityMenuUserViewModel facilityMenuUserVM;
    private FacilityScheduleAdminViewModel createFacilityScheduleAdminVM;
    private FacilityScheduleAdminViewModel editFacilityScheduleAdminVM;
    private SportFacilityModel sportFacilityModel;


    private ViewModelFactory() throws IOException, NotBoundException, SQLException {
        sportFacilityModel = ModelFactory.getInstance().getModel();
        createFacilityVM = new FacilityViewModel(sportFacilityModel);
        facilityMenuVM = new FacilityMenuViewModel(sportFacilityModel);
        editFacilityVM = new EditFacilityViewModel(sportFacilityModel);
        createFacilityScheduleVM = new FacilityScheduleViewModel(sportFacilityModel);
        createLoginVM = new LoginViewModel(sportFacilityModel);
        createFacilityScheduleAdminVM = new FacilityScheduleAdminViewModel(sportFacilityModel);
        editFacilityScheduleAdminVM = new FacilityScheduleAdminViewModel(sportFacilityModel);
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

    public FacilityMenuViewModel getFacilityMenuVM() {
        return facilityMenuVM;
    }

    public EditFacilityViewModel getEditFacilityVM() {
        return editFacilityVM;
    }

    public LoginViewModel getLoginVM() {
        return createLoginVM;
    }

    public FacilityMenuUserViewModel getFacilityMenuUserVM() {
        return facilityMenuUserVM;
    }

    public FacilityScheduleAdminViewModel getFacilityScheduleAdminVM() {
        return createFacilityScheduleAdminVM;
    }

    public FacilityScheduleAdminViewModel getEditFacilityScheduleAdminVM() {return editFacilityScheduleAdminVM;}

    public SportFacilityModel getSportFacilityModel() {
        return sportFacilityModel;
    }
}
