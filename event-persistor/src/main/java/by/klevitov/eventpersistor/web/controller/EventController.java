package by.klevitov.eventpersistor.web.controller;

import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.service.EntityConverterService;
import by.klevitov.eventpersistor.service.EventService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageResponseDTO;
import by.klevitov.eventradarcommon.pagination.dto.SearchByFieldsRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private final EventService eventService;
    private final EntityConverterService<AbstractEvent, AbstractEventDTO> converterService;

    @Autowired
    public EventController(EventService eventService,
                           EntityConverterService<AbstractEvent, AbstractEventDTO> converterService) {
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

    @PostMapping("/all")
    public PageResponseDTO<AbstractEventDTO> findAll(@RequestBody final PageRequestDTO pageRequestDTO) {
        Page<AbstractEvent> entityResultPage = eventService.findAll(pageRequestDTO);
        List<AbstractEventDTO> eventsDTO = converterService.convertToDTO(entityResultPage.getContent());
        return new PageResponseDTO<>(entityResultPage, eventsDTO);
    }

    @PostMapping("/search")
    public List<AbstractEventDTO> findByFields(@RequestBody final Map<String, Object> fields,
                                               @RequestParam final boolean isCombinedMatch) {
        return converterService.convertToDTO(eventService.findByFields(fields, isCombinedMatch));
    }

    @PostMapping("/search/pagination")
    public PageResponseDTO<AbstractEventDTO> findByFields(@RequestBody final SearchByFieldsRequestDTO requestDTO) {
        Page<AbstractEvent> entityResultPage = eventService.findByFields(requestDTO.getFields(),
                requestDTO.isCombinedMatch(), requestDTO.getPageRequestDTO());
        List<AbstractEventDTO> eventsDTO = converterService.convertToDTO(entityResultPage.getContent());
        return new PageResponseDTO<>(entityResultPage, eventsDTO);
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

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody final List<String> ids) {
        eventService.delete(ids);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        eventService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
