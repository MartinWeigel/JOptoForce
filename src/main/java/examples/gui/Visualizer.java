package examples.gui;

import com.martinweigel.joptoforce.AutoDAQ;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jssc.SerialPortList;

public class Visualizer extends Application {
    private AutoDAQ optoforce;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Visualizer.fxml"));
        Parent root = (Parent) loader.load();
        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        Controller controller = (Controller) loader.getController();

        if(SerialPortList.getPortNames().length >= 1) {
            String serialPort = SerialPortList.getPortNames()[0];
            optoforce = new AutoDAQ(serialPort);
            optoforce.zeroValues();
            optoforce.inform = controller::onSensorEvent;
        } else {
            throw new RuntimeException("OptoForce sensor is not connected!");
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        optoforce.stop();
    }
}
