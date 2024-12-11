package by.klevitov.eventmanager.manager.client;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "${feign.client.config.event-persistor.name}",
        url = "${feign.client.config.event-persistor.url}",
        path = "${feign.client.config.event-persistor.context-path}"
)
public interface EventPersistorClient {
    @GetMapping("/events")
    List<AbstractEventDTO> getEvents();

    @GetMapping("/events/{id}")
    AbstractEventDTO getById(@PathVariable final String id);

    //todo Add exception handling.
}
