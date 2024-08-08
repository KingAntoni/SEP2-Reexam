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
    private Scene scheduleScene;
    private Scene facilityMenuScene;
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
        openFacilityMenu();
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

    public void openFacilityMenu() {
        try {
            System.out.println("Opening Facility Menu view...");
            Parent root = loadFXML("/views/facilityMenu/FacilityMenuView.fxml");
            facilityMenuScene = new Scene(root);
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
        }

        stage.setTitle("Facility Menu");
        stage.setScene(facilityMenuScene);
        stage.show();
        System.out.println("Facility Menu view opened.");
    }

    private Parent loadFXML(String path) throws IOException, NotBoundException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        System.out.println("Loading FXML from path: " + path);
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();
        System.out.println("FXML loaded.");

        ViewController ctrl = loader.getController();
        ctrl.init();  // Initial init call
        if (ctrl instanceof views.Menu.FacilityMenuViewController) {
            ((views.Menu.FacilityMenuViewController) ctrl).init(ViewModelFactory.getInstance(), this);
        }
        return root;
    }
}
