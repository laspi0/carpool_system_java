package com.groupeisi.rent.controllers;

import com.groupeisi.rent.entities.Reservation;
import com.groupeisi.rent.entities.Trip;
import com.groupeisi.rent.entities.Vehicle;
import com.groupeisi.rent.DAO.ReservationDAO;
import com.groupeisi.rent.DAO.TripDAO;
import com.groupeisi.rent.DAO.VehicleDAO;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartController {

    @FXML
    private VBox chartContainer;

    private ReservationDAO reservationDao = new ReservationDAO();
    private TripDAO tripDAO = new TripDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();

    private final List<String> senegalCities = List.of("Dakar", "Thiès", "Saint-Louis", "Ziguinchor", "Kaolack");

    @FXML
    public void initialize() {
        BarChart<String, Number> tripsByCity = createTripsByCityChart();
        PieChart vehicleTypes = createVehicleTypesChart();
        LineChart<String, Number> reservationsOverTime = createReservationsOverTimeChart();
        BarChart<String, Number> averagePriceByCity = createAveragePriceByCityChart();

        chartContainer.getChildren().addAll(tripsByCity, vehicleTypes, reservationsOverTime, averagePriceByCity);
    }

    private BarChart<String, Number> createTripsByCityChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Nombre de trajets par ville de départ");
        xAxis.setLabel("Ville de départ");
        yAxis.setLabel("Nombre de trajets");

        List<Trip> trips = tripDAO.getAllTrips();
        Map<String, Long> tripCounts = trips.stream()
                .collect(Collectors.groupingBy(Trip::getDepartureCity, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Trajets");
        for (String city : senegalCities) {
            series.getData().add(new XYChart.Data<>(city, tripCounts.getOrDefault(city, 0L)));
        }

        barChart.getData().add(series);
        barChart.setPrefSize(500, 400);

        return barChart;
    }

    private PieChart createVehicleTypesChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Répartition des marques de véhicules");

        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        Map<String, Long> vehicleBrandCounts = vehicles.stream()
                .collect(Collectors.groupingBy(Vehicle::getBrand, Collectors.counting()));

        for (Map.Entry<String, Long> entry : vehicleBrandCounts.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        pieChart.setPrefSize(500, 400);

        return pieChart;
    }

    private LineChart<String, Number> createReservationsOverTimeChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Évolution du nombre de réservations");
        xAxis.setLabel("Date");
        yAxis.setLabel("Nombre de réservations");

        List<Reservation> reservations = reservationDao.getAll();
        Map<LocalDate, Long> reservationCounts = reservations.stream()
                .collect(Collectors.groupingBy(Reservation::getDateReservation, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Réservations");

        LocalDate startDate = LocalDate.now().minusDays(6);
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            series.getData().add(new XYChart.Data<>(date.toString(), reservationCounts.getOrDefault(date, 0L)));
        }

        lineChart.getData().add(series);
        lineChart.setPrefSize(500, 400);

        return lineChart;
    }

    private BarChart<String, Number> createAveragePriceByCityChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Prix moyen des trajets par ville d'arrivée");
        xAxis.setLabel("Ville d'arrivée");
        yAxis.setLabel("Prix moyen (FCFA)");

        List<Trip> trips = tripDAO.getAllTrips();
        Map<String, Double> avgPrices = trips.stream()
                .collect(Collectors.groupingBy(Trip::getArrivalCity,
                        Collectors.averagingDouble(Trip::getPrice)));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Prix moyen");
        for (String city : senegalCities) {
            series.getData().add(new XYChart.Data<>(city, avgPrices.getOrDefault(city, 0.0)));
        }

        barChart.getData().add(series);
        barChart.setPrefSize(500, 400);

        return barChart;
    }
}