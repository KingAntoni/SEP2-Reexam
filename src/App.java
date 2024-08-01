
import client.core.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ViewHandler vh = ViewHandler.getInstance();
        vh.start();
    }

}