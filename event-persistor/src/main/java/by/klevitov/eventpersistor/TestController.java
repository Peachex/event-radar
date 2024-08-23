package by.klevitov.eventpersistor;

import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventparser.service.impl.EventParserServiceImpl;
import by.klevitov.eventpersistor.messaging.test.TestProducer;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.persistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.repository.EventRepository;
import by.klevitov.eventpersistor.persistor.repository.LocationRepository;
import by.klevitov.eventpersistor.persistor.service.EventService;
import by.klevitov.eventpersistor.persistor.service.LocationService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventDate;
import by.klevitov.eventradarcommon.dto.EventPrice;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    //todo Delete this class.
    @Autowired
    EventRepository eventRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private TestProducer testProducer;

    @GetMapping("/events")
    public List<AbstractEvent> findEvents() {
        return eventService.findAll();
    }

//    @PostMapping("/events")
//    public AbstractEvent createEvent(@RequestBody AbstractEvent event) {
//        return eventService.create(event);
//    }

    @PostMapping("/events/multiple")
    public List<AbstractEvent> createEvents(@RequestBody List<AbstractEvent> events) {
        return eventService.create(events);
    }

    @PutMapping("/events")
    public AbstractEvent updateEvent() throws EventParserServiceException {
        EventParserService parserService = new EventParserServiceImpl();
        Map<EventSourceType, EventParser> availableParsers = parserService.retrieveAvailableParsers();
        List<AbstractEvent> events = new ArrayList<>();

        for (EventParser parser : availableParsers.values()) {
            List<AbstractEventDTO> eventsDTO = parserService.retrieveEvents(parser);
            for (AbstractEventDTO abstractEventDTO : eventsDTO) {
                AbstractEvent event = convertEventDTO(abstractEventDTO);
                events.add(event);
            }
        }

        AbstractEvent eventToUpdate = events.get(0);
        eventToUpdate = eventService.create(eventToUpdate);
        eventToUpdate.setTitle("Test title after updating 2.");
        eventToUpdate.setLocation(new Location(null, "Vitebsk"));
        eventToUpdate.setDate(null);
        return eventService.update(eventToUpdate);
    }

    @DeleteMapping("/events/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.delete(id);
    }

    @GetMapping("/events/{id}")
    public AbstractEvent findById(@PathVariable String id) {
        return eventService.findById(id);
    }

    @GetMapping("/locations")
    public List<Location> findAllLocations() {
        return locationService.findAll();
    }

    @GetMapping("/locations/{id}")
    public Location findAllLocations(@PathVariable String id) {
        return locationService.findById(id);
    }

    @PostMapping("/locations")
    public Location createLocation(@RequestBody Location location) {
        return locationService.create(location);
    }

    @PostMapping("/locations/multiple")
    public List<Location> createMultipleLocations(@RequestBody List<Location> locations) {
        return locationService.create(locations);
    }

    @PutMapping("/locations")
    public Location updateLocation(@RequestBody Location updatedLocation) {
        return locationService.update(updatedLocation);
    }

    @DeleteMapping("/locations/{id}")
    public void deleteLocation(@PathVariable String id) {
        locationService.delete(id);
    }


//    @PostMapping("/events")
//    public void createEvents() throws EventParserServiceException {
//        EventParserService parserService = new EventParserServiceImpl();
//        Map<by.klevitov.eventradarcommon.dto.EventSourceType, EventParser> availableParsers = parserService.retrieveAvailableParsers();
//        List<AbstractEvent> events = new ArrayList<>();
//        for (EventParser parser : availableParsers.values()) {
//            List<AbstractEventDTO> eventsDTO = parserService.retrieveEvents(parser);
//            for (AbstractEventDTO abstractEventDTO : eventsDTO) {
//                AbstractEvent event = convertEventDTO(abstractEventDTO);
//                event.setLocation(new Location("testCountry", "testCity"));
//                Optional<Location> existingLocation = locationRepository.findByCountryAndCity(event.getLocation().getCountry()
//                        , event.getLocation().getCity());
//                if (existingLocation.isEmpty()) {
//                    existingLocation = Optional.of(locationRepository.save(event.getLocation()));
//                }
//                event.setLocation(existingLocation.get());
//                events.add(event);
//            }
//        }
//        eventRepository.saveAll(events);
//    }

    @PostMapping("/events")
    public List<AbstractEvent> createEvents() throws EventParserServiceException {
        EventParserService parserService = new EventParserServiceImpl();
        Map<EventSourceType, EventParser> availableParsers = parserService.retrieveAvailableParsers();
        List<AbstractEvent> events = new ArrayList<>();

        for (EventParser parser : availableParsers.values()) {
            List<AbstractEventDTO> eventsDTO = parserService.retrieveEvents(parser);
            for (AbstractEventDTO abstractEventDTO : eventsDTO) {
                AbstractEvent event = convertEventDTO(abstractEventDTO);
                events.add(event);
            }
        }
        return eventService.create(events);
    }

    @PostMapping("/events/single/custom")
    public AbstractEvent createEvent(@RequestBody AfishaRelaxEvent event) throws EventParserServiceException {
        return eventService.create(event);
    }

    @PostMapping("/events/single")
    public AbstractEvent createEvent() throws EventParserServiceException {
        EventParserService parserService = new EventParserServiceImpl();
        Map<EventSourceType, EventParser> availableParsers = parserService.retrieveAvailableParsers();
        List<AbstractEvent> events = new ArrayList<>();

        for (EventParser parser : availableParsers.values()) {
            List<AbstractEventDTO> eventsDTO = parserService.retrieveEvents(parser);
            for (AbstractEventDTO abstractEventDTO : eventsDTO) {
                AbstractEvent event = convertEventDTO(abstractEventDTO);
                events.add(event);
            }
        }
        AbstractEvent eventToCreate = events.get(0);
        return eventService.create(eventToCreate);
    }

    private AbstractEvent convertEventDTO(AbstractEventDTO abstractEventDTO) {
        return switch (abstractEventDTO.getSourceType()) {
            case AFISHA_RELAX -> {
                AfishaRelaxEventDTO afishaRelaxEventDTO = (AfishaRelaxEventDTO) abstractEventDTO;
                yield AfishaRelaxEvent.builder()
                        .title(afishaRelaxEventDTO.getTitle())
                        .location(new Location(afishaRelaxEventDTO.getLocation().getCountry(),
                                afishaRelaxEventDTO.getLocation().getCity()))
                        .dateStr(afishaRelaxEventDTO.getDateStr())
                        .date(new EventDate(afishaRelaxEventDTO.getDate().getStartDate(),
                                afishaRelaxEventDTO.getDate().getEndDate()))
                        .category(afishaRelaxEventDTO.getCategory())
                        .sourceType(EventSourceType.valueOf(afishaRelaxEventDTO.getSourceType().name()))
                        .eventLink(afishaRelaxEventDTO.getEventLink())
                        .imageLink(afishaRelaxEventDTO.getImageLink())
                        .build();
            }
            case BYCARD -> {
                ByCardEventDTO byCardEventDTO = (ByCardEventDTO) abstractEventDTO;
                yield ByCardEvent.builder()
                        .title(byCardEventDTO.getTitle())
                        .location(new Location(byCardEventDTO.getLocation().getCountry(),
                                byCardEventDTO.getLocation().getCity()))
                        .dateStr(byCardEventDTO.getDateStr())
                        .date(new EventDate(byCardEventDTO.getDate().getStartDate(),
                                byCardEventDTO.getDate().getEndDate()))
                        .category(byCardEventDTO.getCategory())
                        .sourceType(EventSourceType.valueOf(byCardEventDTO.getSourceType().name()))
                        .priceStr(byCardEventDTO.getPriceStr())
                        .price(new EventPrice(byCardEventDTO.getPrice().getMinPrice(),
                                byCardEventDTO.getPrice().getMaxPrice()))
                        .eventLink(byCardEventDTO.getEventLink())
                        .imageLink(byCardEventDTO.getImageLink())
                        .build();
            }
        };
    }

    @GetMapping("/message/send")
    public String sendMessageToQueueAndReceiveResponse(@RequestParam String text) {
        return testProducer.sendAndReceiveMessage(text);
    }

}
