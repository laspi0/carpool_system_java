package com.groupeisi.rent.controllers.trip;

import com.groupeisi.rent.DAO.TripDAO;
import com.groupeisi.rent.DAO.VehicleDAO;
import com.groupeisi.rent.entities.Trip;
import com.groupeisi.rent.entities.Vehicle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Logger;

public class TripController {
    private static final Logger LOGGER = Logger.getLogger(TripController.class.getName());

    @FXML private ComboBox<String> departureCityComboBox;
    @FXML private ComboBox<String> arrivalCityComboBox;
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
    @FXML private HBox buttonContainer;
    @FXML private Button addButton;


    private final TripDAO tripDAO = new TripDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private Label addLabel;
    private Label editLabel;
    private Label deleteLabel;


    @FXML
    public void initialize() {
        LOGGER.info("Initializing TripController");
        initializeTableView();
        loadVehicles();
        initializeCityComboBoxes();
        createButtons();

        tripTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFieldsForUpdate(newSelection);
                addLabel.setDisable(true);
                editLabel.setDisable(false);
                deleteLabel.setDisable(false);
            } else {
                clearFields();
                addLabel.setDisable(false);
                editLabel.setDisable(true);
                deleteLabel.setDisable(true);
            }
        });
    }

    private void createButtons() {
        addLabel = new Label("Ajouter");
        addLabel.setOnMouseClicked(event -> addTrip());

        editLabel = new Label("Modifier");
        editLabel.setOnMouseClicked(event -> updateTrip());
        editLabel.setDisable(true);

        deleteLabel = new Label("Supprimer");
        deleteLabel.setOnMouseClicked(event -> deleteTrip());
        deleteLabel.setDisable(true);

        buttonContainer.getChildren().addAll(addLabel, editLabel, deleteLabel);

        // Style pour les labels
        String labelStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 15 8 15; -fx-cursor: hand;";
        String disabledStyle = labelStyle + " -fx-opacity: 0.5;";

        addLabel.setStyle(labelStyle);
        editLabel.setStyle(disabledStyle);
        deleteLabel.setStyle(disabledStyle);

        addLabel.disabledProperty().addListener((obs, oldVal, newVal) ->
                addLabel.setStyle(newVal ? disabledStyle : labelStyle));
        editLabel.disabledProperty().addListener((obs, oldVal, newVal) ->
                editLabel.setStyle(newVal ? disabledStyle : labelStyle));
        deleteLabel.disabledProperty().addListener((obs, oldVal, newVal) ->
                deleteLabel.setStyle(newVal ? disabledStyle : labelStyle));
    }

    private void addTrip() {
        LOGGER.info("Adding new trip");
        if (validateInput()) {
            Trip newTrip = createTripFromInput();
            if (newTrip != null) {
                tripDAO.save(newTrip);
                LOGGER.info("New trip added successfully");
                clearFields();
                refreshTableView();
            }
        }
    }

    private void updateTrip() {
        LOGGER.info("Updating trip");
        Trip selectedTrip = tripTableView.getSelectionModel().getSelectedItem();
        if (selectedTrip != null && validateInput()) {
            updateTripFromInput(selectedTrip);
            tripDAO.update(selectedTrip);
            LOGGER.info("Trip updated successfully");
            clearFields();
            refreshTableView();
            tripTableView.getSelectionModel().clearSelection();
        } else {
            showAlert(Alert.AlertType.WARNING, "Select a trip", "Please select a trip to update.");
        }
    }

    private void deleteTrip() {
        LOGGER.info("Deleting trip");
        Trip selectedTrip = tripTableView.getSelectionModel().getSelectedItem();
        if (selectedTrip != null) {
            tripDAO.delete(selectedTrip);
            LOGGER.info("Trip deleted successfully");
            clearFields();
            refreshTableView();
            tripTableView.getSelectionModel().clearSelection();
        } else {
            showAlert(Alert.AlertType.WARNING, "Select a trip", "Please select a trip to delete.");
        }
    }

    private void clearFields() {
        departureCityComboBox.getSelectionModel().clearSelection();
        arrivalCityComboBox.getSelectionModel().clearSelection();
        departureDatePicker.setValue(null);
        availableSeatsField.clear();
        priceField.clear();
        vehicleComboBox.getSelectionModel().clearSelection();
    }
    private void initializeTableView() {
        LOGGER.info("Initializing TableView");
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        arrivalCityColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalCity"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        vehicleColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVehicle().getModel()));
        refreshTableView();
    }

    private void loadVehicles() {
        LOGGER.info("Loading vehicles");
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        LOGGER.info("Loaded " + vehicles.size() + " vehicles");
        vehicleComboBox.setItems(FXCollections.observableArrayList(vehicles));
        vehicleComboBox.setConverter(new StringConverter<Vehicle>() {
            @Override
            public String toString(Vehicle vehicle) {
                return vehicle != null ? vehicle.getModel() : "";
            }

            @Override
            public Vehicle fromString(String string) {
                return null;
            }
        });
    }

    private void initializeCityComboBoxes() {
        List<String> senegalCities = Arrays.asList(
                "Dakar", "Thiès", "Rufisque", "Kaolack", "Saint-Louis", "Ziguinchor", "Touba", "Diourbel",
                "Louga", "Tambacounda", "Kolda", "Mbour", "Richard Toll", "Fatick", "Kaffrine", "Kédougou"
        );
        departureCityComboBox.setItems(FXCollections.observableArrayList(senegalCities));
        arrivalCityComboBox.setItems(FXCollections.observableArrayList(senegalCities));
    }


    private boolean validateInput() {
        if (departureCityComboBox.getValue() == null ||
                arrivalCityComboBox.getValue() == null ||
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
                    departureCityComboBox.getValue(),
                    arrivalCityComboBox.getValue(),
                    departureDatePicker.getValue(),
                    seats,
                    price,
                    vehicleComboBox.getValue()
            );
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid input: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numeric values for seats and price.");
            return null;
        }
    }

    private void updateTripFromInput(Trip trip) {
        try {
            trip.setDepartureCity(departureCityComboBox.getValue());
            trip.setArrivalCity(arrivalCityComboBox.getValue());
            trip.setDepartureDate(departureDatePicker.getValue());
            trip.setAvailableSeats(Integer.parseInt(availableSeatsField.getText()));
            trip.setPrice(Double.parseDouble(priceField.getText()));
            trip.setVehicle(vehicleComboBox.getValue());
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid input during update: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numeric values for seats and price.");
        }
    }

    private void populateFieldsForUpdate(Trip trip) {
        departureCityComboBox.setValue(trip.getDepartureCity());
        arrivalCityComboBox.setValue(trip.getArrivalCity());
        departureDatePicker.setValue(trip.getDepartureDate());
        availableSeatsField.setText(String.valueOf(trip.getAvailableSeats()));
        priceField.setText(String.valueOf(trip.getPrice()));
        vehicleComboBox.setValue(trip.getVehicle());
    }



    private void refreshTableView() {
        LOGGER.info("Refreshing TableView");
        List<Trip> trips = tripDAO.getAllTrips();
        LOGGER.info("Loaded " + trips.size() + " trips");
        tripTableView.setItems(FXCollections.observableArrayList(trips));
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}