package by.klevitov.eventpersistor.persistor.web.controller;

import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.service.ConverterService;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
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
@RequestMapping("events")
public class EventController {
    private final EventService eventService;
    private final ConverterService<AbstractEvent, AbstractEventDTO> converterService;

    @Autowired
    public EventController(EventService eventService,
                           ConverterService<AbstractEvent, AbstractEventDTO> converterService) {
        this.eventService = eventService;
        this.converterService = converterService;
    }

    @PostMapping
    public AbstractEventDTO create(@RequestBody final AbstractEventDTO eventDTO) {
        AbstractEvent event = converterService.convertFromDTO(eventDTO);
        return converterService.convertToDTO(eventService.create(event));
    }

    @PostMapping("/multiple")
    public List<AbstractEventDTO> create(@RequestBody final List<AbstractEventDTO> eventsDTO) {
        List<AbstractEvent> events = converterService.convertFromDTO(eventsDTO);
        return converterService.convertToDTO(eventService.create(events));
    }

    @GetMapping
    public List<AbstractEventDTO> findAll() {
        return converterService.convertToDTO(eventService.findAll());
    }

    @PostMapping("/search")
    public List<AbstractEventDTO> findByFields(@RequestBody final Map<String, Object> fields) {
        return converterService.convertToDTO(eventService.findByFields(fields));
    }

    @GetMapping("/{id}")
    public AbstractEventDTO findById(@PathVariable final String id) {
        return converterService.convertToDTO(eventService.findById(id));
    }

    @PutMapping
    public AbstractEventDTO update(@RequestBody final AbstractEventDTO eventDTO) {
        AbstractEvent event = converterService.convertFromDTO(eventDTO);
        return converterService.convertToDTO(eventService.update(event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
