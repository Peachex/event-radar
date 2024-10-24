package by.klevitov.eventpersistor.persistor.web.controller;

import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping
    public Location create(@RequestBody final Location location) {
        return service.create(location);
    }

    @PostMapping("/multiple")
    public List<Location> create(@RequestBody final List<Location> locations) {
        return service.create(locations);
    }

    @GetMapping
    public List<Location> findAll() {
        return service.findAll();
    }

    @PostMapping("/search")
    public List<Location> findByFields(@RequestBody final Map<String, Object> fields) {
        return service.findByFields(fields);
    }

    @GetMapping("/{id}")
    public Location findById(@PathVariable final String id) {
        return service.findById(id);
    }

    @PutMapping
    public Location update(@RequestBody final Location location) {
        return service.update(location);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
