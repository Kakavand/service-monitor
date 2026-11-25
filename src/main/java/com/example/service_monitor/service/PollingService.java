package com.example.service_monitor.service;

import com.example.service_monitor.model.MonitoredService;
import com.example.service_monitor.repository.MonitoredServiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class PollingService {

    private final MonitoredServiceRepository repository;

    private final HttpClient httpClient;

    public PollingService(MonitoredServiceRepository repository) {
        this.repository = repository;

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public void checkService(MonitoredService service) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(service.getUrl()))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() >= 200 && response.statusCode() < 400) {
                service.setStatus("UP");
            } else {
                service.setStatus("DOWN");
            }

        } catch (Exception e) {
            service.setStatus("DOWN");
        }

        repository.save(service);
    }
    public void checkAllServices() {
        List<MonitoredService> services = repository.findAll();

        for (MonitoredService service : services) {
            checkService(service);
        }
    }

    @Scheduled(fixedRate = 30000)
    public void scheduledPolling() {
        checkAllServices();
    }
}