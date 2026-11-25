package com.example.service_monitor.service;

import com.example.service_monitor.model.MonitoredService;
import com.example.service_monitor.repository.MonitoredServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class MonitoredServiceService {

    private final MonitoredServiceRepository repository;

    public MonitoredServiceService(MonitoredServiceRepository repository) {
        this.repository = repository;
    }

    public MonitoredService createService(String name, String url) {
        validateUrl(url);
        validateName(name);
        MonitoredService service = new MonitoredService(name, url);
        return repository.save(service);
    }

    public List<MonitoredService> getAllServices() {
        return repository.findAll();
    }

    public void deleteService(Long id) {

        MonitoredService service = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Service not found"
                ));

        repository.delete(service);
    }

    public MonitoredService updateService(Long id, String name, String url) {

        validateUrl(url);
        validateName(name);
        MonitoredService service = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Service not found"
                ));

        service.setName(name);
        service.setUrl(url);

        return repository.save(service);
    }

    private void validateUrl(String url) {

        if (url == null || url.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "URL cannot be empty"
            );
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "URL must start with http:// or https://"
            );
        }

        try {
            java.net.URI.create(url).toURL();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid URL format"
            );
        }
    }
    private void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Service name cannot be empty"
            );
        }
    }
}