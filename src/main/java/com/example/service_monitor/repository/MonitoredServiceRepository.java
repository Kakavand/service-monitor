package com.example.service_monitor.repository;

import com.example.service_monitor.model.MonitoredService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoredServiceRepository extends JpaRepository<MonitoredService, Long> {
}