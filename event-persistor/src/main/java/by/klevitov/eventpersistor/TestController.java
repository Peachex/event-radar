package by.klevitov.eventpersistor;

import by.klevitov.eventpersistor.messaging.service.MessageService;
import by.klevitov.eventpersistor.messaging.test.TestProducer;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.messaging.request.EntityType;
import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.request.RequestType;
import by.klevitov.eventradarcommon.messaging.request.data.SearchFieldsData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
public class TestController {
    //todo Delete this class.
    @Autowired
    private TestProducer testProducer;

    @Autowired
    private MessageService messageService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private EventService eventService;


//    @PostMapping("/events/search")
//    public List<AbstractEvent> findByFields(@RequestBody Map<String, Object> searchFields) {
//        return eventService.findByFields(searchFields);
//    }

    @GetMapping("/events/test1")
    public MessageResponse createMultipleEvent() throws Exception {
        MessageRequest request = MessageRequest.builder()
                .entityType(EntityType.EVENT)
                .requestType(RequestType.SEARCH_BY_FIELDS)
                .entityData(new SearchFieldsData(Map.of("category", "Кино", "sourceType", "BYCard")))
                .build();

        MessageResponse response = testProducer.sendAndReceiveMessage(request);
        return response;
    }

    @PostMapping("/events/test2")
    public List<Location> findLocations(@RequestBody Map<String, Object> fields) {
        return locationService.findByFields(fields);
    }

    @PostMapping("/events/test3")
    public List<AbstractEvent> findEvents(@RequestBody Map<String, Object> fields) {
        return eventService.findByFields(fields);
    }
}
