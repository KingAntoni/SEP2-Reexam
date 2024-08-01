
package client.core;


import client.views.ViewController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ViewHandler {

    private Stage stage;

    private Scene createFacilityScene;

    private static ViewHandler instance;

    private ViewHandler()
    {
    }
    public static ViewHandler getInstance()
    {
        if(instance==null)
        {
            instance=new ViewHandler();
        }
        return instance;

    }
    public void start() throws IOException, SQLException, NotBoundException
    {
        stage = new Stage();
        stage.getIcons().add(new Image("file:carrotIcon.png"));
        openCreateFacility();
    }

    public void openCreateFacility() {

        try {
            Parent root = loadFXML("../views/CreateFacility/createFacility.fxml");


            createFacilityScene = new Scene(root);
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
        }

        stage.setTitle("Create Facility");
        stage.setScene(createFacilityScene);
        stage.show();
    }

    private Parent loadFXML(String path)
            throws IOException, NotBoundException, SQLException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent root = loader.load();

        ViewController ctrl = loader.getController();
        ctrl.init();
        return root;
    }



}
