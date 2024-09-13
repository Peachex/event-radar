package by.klevitov.eventpersistor;

import by.klevitov.eventpersistor.messaging.service.MessageService;
import by.klevitov.eventpersistor.messaging.test.TestProducer;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventDate;
import by.klevitov.eventradarcommon.dto.EventPrice;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityType;
import by.klevitov.eventradarcommon.messaging.request.MessageRequest;
import by.klevitov.eventradarcommon.messaging.request.RequestType;
import by.klevitov.eventradarcommon.messaging.request.data.SingleEventData;
import by.klevitov.eventradarcommon.messaging.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static by.klevitov.eventparser.constant.EventField.CATEGORY;
import static by.klevitov.eventparser.constant.EventField.DATE_STR;
import static by.klevitov.eventparser.constant.EventField.END_DATE;
import static by.klevitov.eventparser.constant.EventField.EVENT_LINK;
import static by.klevitov.eventparser.constant.EventField.IMAGE_LINK;
import static by.klevitov.eventparser.constant.EventField.LOCATION_CITY;
import static by.klevitov.eventparser.constant.EventField.LOCATION_COUNTRY;
import static by.klevitov.eventparser.constant.EventField.SOURCE_TYPE;
import static by.klevitov.eventparser.constant.EventField.START_DATE;
import static by.klevitov.eventparser.constant.EventField.TITLE;


@RestController
public class TestController {
    //todo Delete this class.
    @Autowired
    private TestProducer testProducer;

    @Autowired
    private MessageService messageService;


//    @PostMapping("/events/search")
//    public List<AbstractEvent> findByFields(@RequestBody Map<String, Object> searchFields) {
//        return eventService.findByFields(searchFields);
//    }

    @GetMapping("/events/test1")
    public MessageResponse createSingleEvent() {
        Map<String, String> fields = Map.of(
                TITLE, "title",
                LOCATION_COUNTRY, "country",
                LOCATION_CITY, "city",
                CATEGORY, "category",
                SOURCE_TYPE, "AFISHA_RELAX",
                DATE_STR, "dateStr",
                START_DATE, "startDate",
                END_DATE, "endDate",
                EVENT_LINK, "eventLink",
                IMAGE_LINK, "imageLink"
        );

        AbstractEventDTO eventDTO = AfishaRelaxEventDTO.builder()
                .title(fields.get(TITLE))
                .location(new LocationDTO(fields.get(LOCATION_COUNTRY), fields.get(LOCATION_CITY)))
                .category(fields.get(CATEGORY))
                .sourceType(EventSourceType.valueOf(fields.get(SOURCE_TYPE)))
                .dateStr(fields.get(DATE_STR))
                .date(new EventDate(LocalDate.of(2024, 7, 13), LocalDate.of(2024, 7, 16)))
                .eventLink(fields.get(EVENT_LINK))
                .imageLink(fields.get(IMAGE_LINK))
                .build();

        MessageRequest request = MessageRequest.builder()
                .entityType(EntityType.EVENT)
                .requestType(RequestType.CREATE_SINGLE)
                .entityData(new SingleEventData(eventDTO))
                .build();

        return messageService.processAndRetrieveResult(request);
    }

    @GetMapping("/events/test2")
    public MessageResponse createSingleEvent2() {
        Map<String, String> fields = Map.of(
                TITLE, "title2",
                LOCATION_COUNTRY, "country",
                LOCATION_CITY, "city",
                CATEGORY, "category2",
                SOURCE_TYPE, "BYCARD",
                DATE_STR, "dateStr2",
                START_DATE, "startDate2",
                END_DATE, "endDate2",
                EVENT_LINK, "eventLink2",
                IMAGE_LINK, "imageLink2"
        );

        AbstractEventDTO eventDTO = ByCardEventDTO.builder()
                .title(fields.get(TITLE))
                .location(new LocationDTO(fields.get(LOCATION_COUNTRY), fields.get(LOCATION_CITY)))
                .category(fields.get(CATEGORY))
                .sourceType(EventSourceType.valueOf(fields.get(SOURCE_TYPE)))
                .dateStr(fields.get(DATE_STR))
                .date(new EventDate(LocalDate.of(2024, 7, 13), LocalDate.of(2024, 7, 16)))
                .eventLink(fields.get(EVENT_LINK))
                .imageLink(fields.get(IMAGE_LINK))
                .price(new EventPrice(new BigDecimal(1), new BigDecimal(11)))
                .build();

        MessageRequest request = MessageRequest.builder()
                .entityType(EntityType.EVENT)
                .requestType(RequestType.CREATE_SINGLE)
                .entityData(new SingleEventData(eventDTO))
                .build();

        return messageService.processAndRetrieveResult(request);
    }


}
