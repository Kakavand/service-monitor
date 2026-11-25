
package com.example.service_monitor.service;
import java.util.Optional;
import com.example.service_monitor.repository.MonitoredServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import com.example.service_monitor.model.MonitoredService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.example.service_monitor.model.MonitoredService;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class MonitoredServiceServiceTest {

    @Mock
    private MonitoredServiceRepository repository;

    @InjectMocks
    private MonitoredServiceService service;

    @Test
    void createService_whenNameIsEmpty_throwsException() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.createService("", "https://example.com")
        );

        assertEquals(
                "Service name cannot be empty",
                exception.getReason()
        );

        verify(repository, never()).save(
                org.mockito.ArgumentMatchers.any()
        );
    }

    @Test
    void createService_whenUrlIsInvalid_throwsException() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.createService("Google", "invalid-url")
        );

        assertEquals(
                "URL must start with http:// or https://",
                exception.getReason()
        );

        verify(repository, never()).save(
                org.mockito.ArgumentMatchers.any()
        );
    }

    @Test
    void createService_withValidData_savesService() {

        service.createService("Google", "https://google.com");

        verify(repository).save(
                org.mockito.ArgumentMatchers.argThat(service ->
                        service.getName().equals("Google") &&
                                service.getUrl().equals("https://google.com")
                )
        );
    }

    @Test
    void updateService_withValidData_updatesAndSavesService() {

        MonitoredService existingService =
                new MonitoredService("Old name", "https://old.com");

        org.mockito.Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existingService));

        service.updateService(1L, "Google", "https://google.com");

        assertEquals("Google", existingService.getName());
        assertEquals("https://google.com", existingService.getUrl());

        verify(repository).save(existingService);
    }

    @Test
    void deleteService_whenServiceExists_deletesService() {

        MonitoredService existingService =
                new MonitoredService("Google", "https://google.com");

        org.mockito.Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(existingService));

        service.deleteService(1L);

        verify(repository).delete(existingService);
    }

    @Test
    void deleteService_whenServiceDoesNotExist_throwsException() {

        org.mockito.Mockito.when(repository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.deleteService(999L)
        );

        assertEquals("Service not found", exception.getReason());

        verify(repository, never()).delete(
                org.mockito.ArgumentMatchers.any()
        );
    }

    @Test
    void getAllServices_returnsAllServices() {

        List<MonitoredService> services = List.of(
                new MonitoredService("Google", "https://google.com"),
                new MonitoredService("GitHub", "https://github.com")
        );

        org.mockito.Mockito.when(repository.findAll())
                .thenReturn(services);

        List<MonitoredService> result = service.getAllServices();

        assertEquals(2, result.size());
        assertEquals("Google", result.get(0).getName());
        assertEquals("GitHub", result.get(1).getName());

        verify(repository).findAll();
    }
}

