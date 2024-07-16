package com.groupeisi.rent.controllers.trip;

import com.groupeisi.rent.DAO.TripDAO;
import com.groupeisi.rent.entities.Trip;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class MyTripController {
    @FXML
    private TableView<Trip> tripTable;
    @FXML
    private TableColumn<Trip, String> departureCityColumn;
    @FXML
    private TableColumn<Trip, String> arrivalCityColumn;
    @FXML
    private TableColumn<Trip, LocalDate> departureDateColumn;
    @FXML
    private TableColumn<Trip, Integer> availableSeatsColumn;
    @FXML
    private TableColumn<Trip, Double> priceColumn;

    private TripDAO tripDAO = new TripDAO();

    public void initialize() {
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        arrivalCityColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalCity"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tripTable.setItems(FXCollections.observableArrayList(tripDAO.getAllTrips()));
    }
}
