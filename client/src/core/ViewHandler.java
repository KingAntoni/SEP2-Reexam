package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transferObjects.Facility;
import transferObjects.User;
import views.ViewController;
import views.editFacility.EditFacilityViewController;
import views.addFacility.FacilityViewController;
import views.facilityMenu.FacilityMenuViewController;
import views.facilityMenuUser.FacilityMenuUserViewController;
import views.facilitySchedule.FacilityScheduleViewController;
import views.facilityScheduleAdmin.FacilityScheduleAdminViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.rmi.NotBoundException;

public class ViewHandler {

    private Stage stage;
    private static ViewHandler instance;

    private ViewHandler() {}

    public static synchronized ViewHandler getInstance() {
        if (instance == null) {
            instance = new ViewHandler();
        }
        return instance;
    }

    public void start(Stage primaryStage) throws IOException, SQLException, NotBoundException {
        this.stage = primaryStage;
        openLogin();
    }

    public void openFacilityMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/facilityMenu/FacilityMenuView.fxml"));
            Parent root = loader.load();

            FacilityMenuViewController facilityMenuViewController = loader.getController();
            facilityMenuViewController.init(); // Initialize the controller after FXML is loaded
            facilityMenuViewController.refresh(); // Refresh the facility list

            stage.setScene(new Scene(root));
            stage.setTitle("Admin Facility Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally show an alert or handle the exception
        }
    }


    public void openFacilityMenuUser() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/facilityMenuUser/FacilityMenuUserView.fxml"));
        Parent root = loader.load();
        FacilityMenuUserViewController facilityMenuUserViewController = loader.getController();
        facilityMenuUserViewController.init();
        stage.setScene(new Scene(root));
        stage.setTitle("User Facility Menu");
        stage.show();
    }

    public void openCreateFacility() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addFacility/FacilityView.fxml"));
            Parent root = loader.load();
            FacilityViewController controller = loader.getController();
            controller.init(ViewModelFactory.getInstance(), this);
            stage.setScene(new Scene(root));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Create Facility");
        stage.show();
    }

    public void openEditFacility(Facility facility) throws IOException, NotBoundException, SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/editFacility/EditFacilityView.fxml"));
            Parent root = loader.load();

            EditFacilityViewController controller = loader.getController();
            controller.init(ViewModelFactory.getInstance(), this, facility);

            stage.setScene(new Scene(root));
            stage.setTitle("Edit Facility");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void openFacilitySchedule(Facility facility) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/facilitySchedule/FacilityScheduleView.fxml"));
            Parent root = loader.load();
            FacilityScheduleViewController controller = loader.getController();
            controller.init(ViewModelFactory.getInstance(), this, facility);
            stage.setScene(new Scene(root));
            stage.setTitle("Schedule");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Schedule");
        stage.show();
    }

    public void openFacilityScheduleAdmin(Facility facility) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/facilityScheduleAdmin/FacilityScheduleAdminView.fxml"));
            Parent root = loader.load();

            // Obtain the controller from the loader
            FacilityScheduleAdminViewController controller = loader.getController();

            // Ensure that the ViewModelFactory and ViewHandler instances are correctly passed
            ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
            controller.init(viewModelFactory, this, facility);

            // Set up the stage
            stage.setScene(new Scene(root));
            stage.setTitle("Schedule Admin");
            stage.show();
        } catch (IOException | SQLException | NotBoundException e) {
            e.printStackTrace();
            // Handle the exception or show an alert
        }
    }




    public void openLogin(){
        try {
            Parent root = loadFXML("/views/login/LoginView.fxml");
            stage.setScene(new Scene(root));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        stage.setTitle("Log in");
        stage.show();
    }



    private Parent loadFXML(String path) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();

        ViewController ctrl = loader.getController();
        ctrl.init();

        return root;
    }
}
