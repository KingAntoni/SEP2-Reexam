package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.ViewController;
import views.login.LoginViewController;
import views.login.LoginViewModel;
import views.facilitySchedule.FacilityScheduleViewModel;

import java.io.IOException;
import java.sql.SQLException;
import java.rmi.NotBoundException;

public class ViewHandler {

    private Stage stage;
    private Scene createFacilityScene;
    private Scene scheduleScene;
    private Scene loginScene;
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
        System.out.println("Starting application...");
        openLoginView();
    }

    public void openCreateFacility() {
        try {
            System.out.println("Opening Create Facility view...");
            Parent root = loadFXML("/views/addFacility/FacilityView.fxml");
            createFacilityScene = new Scene(root);
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
        }

        stage.setTitle("Create Facility");
        stage.setScene(createFacilityScene);
        stage.show();
        System.out.println("Create Facility view opened.");
    }

    public void openFacilitySchedule(){
        try {
            System.out.println("Opening Facility Schedule view...");
            Parent root = loadFXML("/views/facilitySchedule/FacilityScheduleView.fxml");
            scheduleScene = new Scene(root);
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
        }

        stage.setTitle("Schedule");
        stage.setScene(scheduleScene);
        stage.show();
        System.out.println("Schedule Scene view opened.");
    }

    public void openLoginView() {
        try {
            System.out.println("Opening Login view...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login/LoginView.fxml"));
            Parent root = loader.load();

            LoginViewController ctrl = loader.getController();
            ctrl.init();

            loginScene = new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Log in");
        stage.setScene(loginScene);
        stage.show();
        System.out.println("Log in view opened.");
    }

    private Parent loadFXML(String path) throws IOException, NotBoundException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        System.out.println("Loading FXML from path: " + path);
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        System.out.println("FXML loaded.");

        ViewController ctrl = loader.getController();
        ctrl.init();
        return root;
    }
}
