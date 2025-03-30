package by.klevitov.eventwebapp.web.controller;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageResponseDTO;
import by.klevitov.eventradarcommon.pagination.dto.SearchByFieldsRequestDTO;
import by.klevitov.eventwebapp.service.EventPersistorClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/all")
    public PageResponseDTO<AbstractEventDTO> findAll(@RequestBody final PageRequestDTO pageRequestDTO) {
        return clientService.findAllEvents(pageRequestDTO);
    }

    @PostMapping("/search")
    public List<AbstractEventDTO> findByFields(@RequestBody final Map<String, Object> fields,
                                               @RequestParam final boolean isCombinedMatch) {
        return clientService.findEventsByFields(fields, isCombinedMatch);
    }

    @PostMapping("/search/pagination")
    public PageResponseDTO<AbstractEventDTO> findByFields(@RequestBody final SearchByFieldsRequestDTO requestDTO) {
        return clientService.findEventsByFields(requestDTO);
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

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        clientService.deleteEvents();
        return ResponseEntity.noContent().build();
    }
}
