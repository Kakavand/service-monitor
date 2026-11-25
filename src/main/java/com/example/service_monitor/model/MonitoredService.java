package com.example.service_monitor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitored_services")
public class MonitoredService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
    private LocalDateTime createdAt;
    private String status;

    // Constructors
    public MonitoredService() {
    }

    public MonitoredService(String name, String url) {
        this.name = name;
        this.url = url;
        this.createdAt = LocalDateTime.now();
        this.status = "UNKNOWN";
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}