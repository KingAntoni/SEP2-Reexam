package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import views.ViewController;

import java.io.IOException;
import java.sql.SQLException;
import java.rmi.NotBoundException;

public class ViewHandler {

    private Stage stage;
    private Scene createFacilityScene;
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
        stage.getIcons().add(new Image("file:carrotIcon.png"));
        System.out.println("Starting application...");
        openCreateFacility();
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
