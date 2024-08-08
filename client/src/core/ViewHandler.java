package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.ViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.rmi.NotBoundException;

public class ViewHandler {

    private Stage stage;
    private Scene createFacilityScene;
    private Scene ScheduleScene;
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
        //openCreateFacility();
        //openFacilitySchedule();
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
            ScheduleScene = new Scene(root);
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
        }

        stage.setTitle("Schedule");
        stage.setScene(ScheduleScene);
        stage.show();
        System.out.println("Schedule Scene view opened.");
    }

    public void openLoginView() {
        try {
            System.out.println("Opening Logging view...");
            Parent root = loadFXML("/views/login/LoginView.fxml");
            loginScene = new Scene(root);
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
        }

        stage.setTitle("Log in");
        stage.setScene(loginScene);
        stage.show();
        System.out.println("log in view opened.");
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
