package com.groupeisi.rent.controllers.vehicle;

import com.groupeisi.rent.DAO.UserDAO;
import com.groupeisi.rent.DAO.VehicleDAO;
import com.groupeisi.rent.entities.User;
import com.groupeisi.rent.entities.Vehicle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable {

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField registrationField;

    @FXML
    private ComboBox<User> driverComboBox;

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDrivers();
    }

    private void loadDrivers() {
        List<User> drivers = userDAO.findDrivers();
        driverComboBox.setItems(FXCollections.observableArrayList(drivers));
        driverComboBox.setCellFactory((comboBox) -> {
            return new javafx.scene.control.ListCell<User>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getFirstName() + " " + item.getLastName());
                    }
                }
            };
        });
        driverComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user == null) {
                    return null;
                } else {
                    return user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")";
                }
            }

            @Override
            public User fromString(String string) {
                return null; // Not used
            }
        });
    }


    @FXML
    private void addVehicle() {
        String brand = brandField.getText().trim();
        String model = modelField.getText().trim();
        String registration = registrationField.getText().trim();
        User selectedDriver = driverComboBox.getValue();

        if (brand.isEmpty() || model.isEmpty() || registration.isEmpty() || selectedDriver == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        Vehicle newVehicle = new Vehicle();
        newVehicle.setBrand(brand);
        newVehicle.setModel(model);
        newVehicle.setRegistration(registration);
        newVehicle.setDriver(selectedDriver);

        vehicleDAO.save(newVehicle);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Le véhicule a été ajouté avec succès.");
        clearFields();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        brandField.clear();
        modelField.clear();
        registrationField.clear();
        driverComboBox.getSelectionModel().clearSelection();
    }

}
