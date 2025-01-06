package by.klevitov.eventwebapp.eventapp.web.controller;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventwebapp.eventapp.service.EventPersistorClientService;
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
    private final EventPersistorClientService clientService;

    @Autowired
    public EventController(EventPersistorClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public AbstractEventDTO create(@RequestBody final AbstractEventDTO eventDTO) {
        return clientService.createEvent(eventDTO);
    }

    @PostMapping("/multiple")
    public List<AbstractEventDTO> create(@RequestBody final List<AbstractEventDTO> eventsDTO) {
        return clientService.createEvents(eventsDTO);
    }

    @GetMapping
    public List<AbstractEventDTO> findAll() {
        return clientService.findAllEvents();
    }

    @PostMapping("/search")
    public List<AbstractEventDTO> findByFields(@RequestBody final Map<String, Object> fields) {
        return clientService.findEventsByFields(fields);
    }

    @GetMapping("/{id}")
    public AbstractEventDTO findById(@PathVariable final String id) {
        return clientService.findEventById(id);
    }

    @PutMapping
    public AbstractEventDTO update(@RequestBody final AbstractEventDTO eventDTO) {
        return clientService.updateEvent(eventDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final String id) {
        clientService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
