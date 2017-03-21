package examples.gui;

import com.martinweigel.joptoforce.OptoPackage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class Controller {
    private double SCALE_XY = 1.0/50;
    private double SCALE_Z = 1.0/100;
    @FXML
    private Circle circle;

    void onSensorEvent(final OptoPackage op) {
        Platform.runLater(() -> {
            circle.setCenterX(op.getX() * SCALE_XY);
            circle.setCenterY(op.getY() * SCALE_XY);
            circle.setRadius(op.getZ() * SCALE_Z);
        });
    }
}
