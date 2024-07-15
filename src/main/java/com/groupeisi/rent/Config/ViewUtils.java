package com.groupeisi.rent.Config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewUtils {
    public static void switchToView(String fxmlPath, Stage currentStage) {
        try {
            // Sauvegarder la taille actuelle de la scène
            double width = currentStage.getScene().getWidth();
            double height = currentStage.getScene().getHeight();

            // Charger la vue depuis le fichier FXML spécifié
            Parent view = FXMLLoader.load(ViewUtils.class.getResource(fxmlPath));

            // Créer une nouvelle scène avec la même taille que la scène actuelle
            Scene scene = new Scene(view, width, height);

            // Changer la scène de la fenêtre actuelle
            currentStage.setScene(scene);
            currentStage.show(); // Assurez-vous que show() est appelé après setScene()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
