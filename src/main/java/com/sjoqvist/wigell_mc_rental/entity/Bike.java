package com.sjoqvist.wigell_mc_rental.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "bike")
public class Bike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(nullable = false, length = 100)
    private String manufacturer;

    @Column(name = "daily_rate_sek", nullable = false, precision = 1)
    private Double dailyRateSek;

    @OneToMany(mappedBy = "bike", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BikeStatus status;

    protected Bike() {}

    public Bike(String model, String manufacturer, Double dailyRateSek, BikeStatus status) {
        this.model = model;
        this.manufacturer = manufacturer;
        this.dailyRateSek = dailyRateSek;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Double getDailyRateSek() {
        return dailyRateSek;
    }

    public void setDailyRateSek(Double dailyRateSek) {
        this.dailyRateSek = dailyRateSek;
    }

    public BikeStatus getStatus() {
        return status;
    }

    public void setStatus(BikeStatus status) {
        this.status = status;
    }
}
