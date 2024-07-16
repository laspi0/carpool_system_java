package com.groupeisi.rent.entities;

import com.groupeisi.rent.entities.User;
import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "registration", nullable = false, unique = true)
    private String registration;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User driver;

    // Constructeurs, getters et setters
    // ...

    public Vehicle() {
    }

    public Vehicle(String brand, String model, String registration, User driver) {
        this.brand = brand;
        this.model = model;
        this.registration = registration;
        this.driver = driver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }
}
