package com.groupeisi.rent.controllers.trip;

import com.groupeisi.rent.DAO.TripDAO;
import com.groupeisi.rent.DAO.VehicleDAO;
import com.groupeisi.rent.entities.Trip;
import com.groupeisi.rent.entities.Vehicle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class TripController {

    @FXML private TextField departureCityField;
    @FXML private TextField arrivalCityField;
    @FXML private DatePicker departureDatePicker;
    @FXML private TextField availableSeatsField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Vehicle> vehicleComboBox;
    @FXML private TableView<Trip> tripTableView;
    @FXML private TableColumn<Trip, String> departureCityColumn;
    @FXML private TableColumn<Trip, String> arrivalCityColumn;
    @FXML private TableColumn<Trip, LocalDate> departureDateColumn;
    @FXML private TableColumn<Trip, Integer> availableSeatsColumn;
    @FXML private TableColumn<Trip, Double> priceColumn;
    @FXML private TableColumn<Trip, String> vehicleColumn;

    private final TripDAO tripDAO = new TripDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    @FXML
    public void initialize() {
        initializeTableView();
        loadVehicles();
    }

    private void initializeTableView() {
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        arrivalCityColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalCity"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        vehicleColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVehicle().getRegistration()));
        refreshTableView();
    }

    private void loadVehicles() {
        vehicleComboBox.setItems(FXCollections.observableArrayList(vehicleDAO.getAllVehicles()));
        vehicleComboBox.setConverter(new StringConverter<Vehicle>() {
            @Override
            public String toString(Vehicle vehicle) {
                return vehicle != null ? vehicle.getRegistration() : "";
            }

            @Override
            public Vehicle fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void addTrip() {
        if (validateInput()) {
            Trip newTrip = createTripFromInput();
            tripDAO.save(newTrip);
            clearFields();
            refreshTableView();
        }
    }

    @FXML
    private void updateTrip() {
        Trip selectedTrip = tripTableView.getSelectionModel().getSelectedItem();
        if (selectedTrip != null && validateInput()) {
            updateTripFromInput(selectedTrip);
            tripDAO.update(selectedTrip);
            clearFields();
            refreshTableView();
        } else {
            showAlert(Alert.AlertType.WARNING, "Select a trip", "Please select a trip to update.");
        }
    }

    @FXML
    private void deleteTrip() {
        Trip selectedTrip = tripTableView.getSelectionModel().getSelectedItem();
        if (selectedTrip != null) {
            tripDAO.delete(selectedTrip);
            clearFields();
            refreshTableView();
        } else {
            showAlert(Alert.AlertType.WARNING, "Select a trip", "Please select a trip to delete.");
        }
    }

    private boolean validateInput() {
        if (departureCityField.getText().isEmpty() ||
                arrivalCityField.getText().isEmpty() ||
                departureDatePicker.getValue() == null ||
                availableSeatsField.getText().isEmpty() ||
                priceField.getText().isEmpty() ||
                vehicleComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill all fields.");
            return false;
        }
        return true;
    }

    private Trip createTripFromInput() {
        try {
            int seats = Integer.parseInt(availableSeatsField.getText());
            double price = Double.parseDouble(priceField.getText());
            return new Trip(
                    departureCityField.getText(),
                    arrivalCityField.getText(),
                    departureDatePicker.getValue(),
                    seats,
                    price,
                    vehicleComboBox.getValue()
            );
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numeric values for seats and price.");
            return null;
        }
    }

    private void updateTripFromInput(Trip trip) {
        try {
            trip.setDepartureCity(departureCityField.getText());
            trip.setArrivalCity(arrivalCityField.getText());
            trip.setDepartureDate(departureDatePicker.getValue());
            trip.setAvailableSeats(Integer.parseInt(availableSeatsField.getText()));
            trip.setPrice(Double.parseDouble(priceField.getText()));
            trip.setVehicle(vehicleComboBox.getValue());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numeric values for seats and price.");
        }
    }

    private void clearFields() {
        departureCityField.clear();
        arrivalCityField.clear();
        departureDatePicker.setValue(null);
        availableSeatsField.clear();
        priceField.clear();
        vehicleComboBox.getSelectionModel().clearSelection();
    }

    private void refreshTableView() {
        tripTableView.setItems(FXCollections.observableArrayList(tripDAO.getAllTrips()));
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}