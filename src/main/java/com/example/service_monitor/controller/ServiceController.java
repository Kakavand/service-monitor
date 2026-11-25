
package com.example.service_monitor.controller;

import com.example.service_monitor.model.MonitoredService;
import com.example.service_monitor.service.MonitoredServiceService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/services")


public class ServiceController {

    private final MonitoredServiceService service;

    public ServiceController(MonitoredServiceService service) {
        this.service = service;
    }

    @PostMapping
    public MonitoredService createService(@RequestBody ServiceRequest request) {
        return service.createService(request.name(), request.url());
    }

    @GetMapping
    public List<MonitoredService> getAllServices() {
        return service.getAllServices();
    }

    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        service.deleteService(id);
    }

    @PutMapping("/{id}")
    public MonitoredService updateService(
            @PathVariable Long id,
            @RequestBody ServiceRequest request) {

        return service.updateService(id, request.name(), request.url());
    }

    public record ServiceRequest(String name, String url) {
    }
}
