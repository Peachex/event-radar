package by.klevitov.eventpersistor.persistor.web.controller;

import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.ConverterService;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final LocationService locationService;
    private final ConverterService<Location, LocationDTO> converterService;

    @Autowired
    public LocationController(LocationService locationService,
                              ConverterService<Location, LocationDTO> converterService) {
        this.locationService = locationService;
        this.converterService = converterService;
    }

    @PostMapping
    public LocationDTO create(@RequestBody final LocationDTO locationDTO) {
        Location location = converterService.convertFromDTO(locationDTO);
        return converterService.convertToDTO(locationService.create(location));
    }

    @PostMapping("/multiple")
    public List<LocationDTO> create(@RequestBody final List<LocationDTO> locationsDTO) {
        List<Location> locations = converterService.convertFromDTO(locationsDTO);
        return converterService.convertToDTO(locationService.create(locations));
    }

    @GetMapping
    public List<LocationDTO> findAll() {
        return converterService.convertToDTO(locationService.findAll());
    }

    @PostMapping("/search")
    public List<LocationDTO> findByFields(@RequestBody final Map<String, Object> fields) {
        return converterService.convertToDTO(locationService.findByFields(fields));
    }

    @GetMapping("/{id}")
    public LocationDTO findById(@PathVariable final String id) {
        return converterService.convertToDTO(locationService.findById(id));
    }

    @PutMapping
    public LocationDTO update(@RequestBody final LocationDTO locationDTO) {
        Location location = converterService.convertFromDTO(locationDTO);
        return converterService.convertToDTO(locationService.update(location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final String id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
