package by.klevitov.eventradarcommon.client;

import by.klevitov.eventradarcommon.client.exception.EventPersistorClientException;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageResponseDTO;
import by.klevitov.eventradarcommon.pagination.dto.SearchByFieldsRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/events/all")
    PageResponseDTO<AbstractEventDTO> findAll(@RequestBody final PageRequestDTO pageRequestDTO) throws EventPersistorClientException;

    @GetMapping("/events/{id}")
    AbstractEventDTO findById(@PathVariable("id") final String id) throws EventPersistorClientException;

    @PostMapping("/events/search")
    List<AbstractEventDTO> findByFields(@RequestBody final Map<String, Object> fields,
                                        @RequestParam("isCombinedMatch") final boolean isCombinedMatch)
            throws EventPersistorClientException;

    @PostMapping("/events/search/pagination")
    PageResponseDTO<AbstractEventDTO> findByFields(@RequestBody final SearchByFieldsRequestDTO requestDTO)
            throws EventPersistorClientException;

    @PostMapping("/events")
    AbstractEventDTO create(@RequestBody final AbstractEventDTO eventDTO) throws EventPersistorClientException;

    @PostMapping("/events/multiple")
    List<AbstractEventDTO> create(@RequestBody final List<AbstractEventDTO> eventsDTO)
            throws EventPersistorClientException;

    @PutMapping("/events")
    AbstractEventDTO update(@RequestBody final AbstractEventDTO eventDTO) throws EventPersistorClientException;

    @DeleteMapping("/events/{id}")
    void delete(@PathVariable("id") final String id) throws EventPersistorClientException;

    @DeleteMapping("/events")
    void delete(@RequestBody final List<String> ids) throws EventPersistorClientException;

    @DeleteMapping("/events/all")
    void deleteAll() throws EventPersistorClientException;

    @GetMapping("/healthcheck")
    void healthCheck() throws EventPersistorClientException;
}
