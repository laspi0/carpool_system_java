package com.groupeisi.rent.Config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewUtils {
    public static void switchToView(String fxmlPath, Stage currentStage) {
        try {
            double width = currentStage.getScene().getWidth();
            double height = currentStage.getScene().getHeight();

            Parent view = FXMLLoader.load(ViewUtils.class.getResource(fxmlPath));

            Scene scene = new Scene(view, width, height);

            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
