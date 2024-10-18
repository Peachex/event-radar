package by.klevitov.eventpersistor.persistor.web.controller;

import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("locations")
public class LocationController {
    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Location> findAll() {
        return service.findAll();
    }

    @PostMapping
    public List<Location> findByFields(@RequestBody final Map<String, Object> fields) {
        return service.findByFields(fields);
    }

    @GetMapping("/{id}")
    public Location findById(@PathVariable final String id) {
        return service.findById(id);
    }
}
