package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transferObjects.Facility;
import views.ViewController;
import views.editFacility.EditFacilityViewController;
import views.addFacility.FacilityViewController;
import views.facilityMenu.FacilityMenuViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.rmi.NotBoundException;

public class ViewHandler {

    private Stage stage;
    private Scene createFacilityScene;
    private Scene facilityMenuScene;
    private Scene facilityMenuUserScene;
    private Scene scheduleScene;
    private Scene scheduleAdminScene;
    private Scene loginScene;
    private Scene editFacilityScene;
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
            Parent root = loadFXML("/views/facilityMenu/FacilityMenuView.fxml");
            stage.setScene(new Scene(root));
            FacilityMenuViewController facilityMenuViewController = new FacilityMenuViewController();
            facilityMenuViewController.init();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        stage.setTitle("Admin Facility Menu");
        stage.show();
    }

    public void openFacilityMenuUser() {
        try {
            Parent root = loadFXML("/views/facilityMenu/FacilityMenuUserView.fxml");
            stage.setScene(new Scene(root));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
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

    public void openFacilitySchedule() {
        try {
            Parent root = loadFXML("/views/facilitySchedule/FacilityScheduleView.fxml");
            stage.setScene(new Scene(root));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        stage.setTitle("Schedule");
        stage.show();
    }

    public void openFacilityScheduleAdmin(){
        try {
            Parent root = loadFXML("/views/facilityScheduleAdmin/FacilityScheduleAdminView.fxml");
            stage.setScene(new Scene(root));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        stage.setTitle("Schedule Admin");
        stage.show();
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
