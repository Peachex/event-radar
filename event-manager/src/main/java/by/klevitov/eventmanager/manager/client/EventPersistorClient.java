package by.klevitov.eventmanager.manager.client;

import by.klevitov.eventmanager.manager.exception.EventPersistorClientException;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "${feign.client.config.event-persistor.name}",
        url = "${feign.client.config.event-persistor.url}",
        path = "${feign.client.config.event-persistor.context-path}"
)
public interface EventPersistorClient {
    @GetMapping("/events")
    List<AbstractEventDTO> findAll() throws EventPersistorClientException;

    @GetMapping("/events/{id}")
    AbstractEventDTO findById(@PathVariable final String id) throws EventPersistorClientException;

    @PostMapping("/events/search")
    List<AbstractEventDTO> findByFields(@RequestBody final Map<String, Object> fields)
            throws EventPersistorClientException;

    @PostMapping("/events")
    AbstractEventDTO create(@RequestBody final AbstractEventDTO eventDTO) throws EventPersistorClientException;

    @PostMapping("/events/multiple")
    List<AbstractEventDTO> create(@RequestBody final List<AbstractEventDTO> eventsDTO)
            throws EventPersistorClientException;

    @PutMapping("/events")
    AbstractEventDTO update(@RequestBody final AbstractEventDTO eventDTO) throws EventPersistorClientException;

    @DeleteMapping("/events/{id}")
    void delete(@PathVariable final String id) throws EventPersistorClientException;
}
