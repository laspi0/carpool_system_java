package com.groupeisi.rent.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;

public class ChartController {

    @FXML
    private VBox chartContainer;

    @FXML
    public void initialize() {
        // Graphique en barres
        BarChart<String, Number> barChart = createBarChart();

        // Graphique en lignes
        LineChart<String, Number> lineChart = createLineChart();

        // Graphique circulaire
        PieChart pieChart = createPieChart();

        chartContainer.getChildren().addAll(barChart, lineChart, pieChart);
    }

    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Ventes par produit");
        xAxis.setLabel("Produit");
        yAxis.setLabel("Ventes");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2023");
        series.getData().add(new XYChart.Data<>("Produit A", 50));
        series.getData().add(new XYChart.Data<>("Produit B", 80));
        series.getData().add(new XYChart.Data<>("Produit C", 65));

        barChart.getData().add(series);
        barChart.setPrefSize(400, 300);

        return barChart;
    }

    private LineChart<String, Number> createLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Évolution des ventes");
        xAxis.setLabel("Mois");
        yAxis.setLabel("Ventes");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2023");
        series.getData().add(new XYChart.Data<>("Jan", 23));
        series.getData().add(new XYChart.Data<>("Fév", 14));
        series.getData().add(new XYChart.Data<>("Mar", 15));
        series.getData().add(new XYChart.Data<>("Avr", 24));
        series.getData().add(new XYChart.Data<>("Mai", 34));

        lineChart.getData().add(series);
        lineChart.setPrefSize(400, 300);

        return lineChart;
    }

    private PieChart createPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Répartition des revenus");

        PieChart.Data slice1 = new PieChart.Data("Produit A", 30);
        PieChart.Data slice2 = new PieChart.Data("Produit B", 45);
        PieChart.Data slice3 = new PieChart.Data("Produit C", 25);

        pieChart.getData().addAll(slice1, slice2, slice3);
        pieChart.setPrefSize(400, 300);

        return pieChart;
    }
}