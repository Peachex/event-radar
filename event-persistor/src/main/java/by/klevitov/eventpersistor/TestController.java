package by.klevitov.eventpersistor;

import by.klevitov.eventparser.exception.EventParserServiceException;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.service.EventParserService;
import by.klevitov.eventparser.service.impl.EventParserServiceImpl;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.persistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.persistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.repository.EventRepository;
import by.klevitov.eventpersistor.persistor.repository.LocationRepository;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class TestController {
    //todo Delete this class.
    @Autowired
    EventRepository eventRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @GetMapping("/events")
    public List<AbstractEvent> findAllEvents() {
        List<AbstractEvent> events = eventRepository.findAll();
        return events;
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
        return locationService.createMultiple(locations);
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
    public void createEvents() throws EventParserServiceException {
        EventParserService parserService = new EventParserServiceImpl();
        Map<by.klevitov.eventradarcommon.dto.EventSourceType, EventParser> availableParsers = parserService.retrieveAvailableParsers();
        List<AbstractEvent> events = new ArrayList<>();
        Set<Location> uniqueLocationsFromEvents = new LinkedHashSet<>();

        for (EventParser parser : availableParsers.values()) {
            List<AbstractEventDTO> eventsDTO = parserService.retrieveEvents(parser);
            for (AbstractEventDTO abstractEventDTO : eventsDTO) {
                AbstractEvent event = convertEventDTO(abstractEventDTO);
                event.setLocation(new Location("testCountry", "testCity"));
                uniqueLocationsFromEvents.add(event.getLocation());
                events.add(event);
            }
        }

        List<Location> existingLocations = locationRepository.findAll();
        List<Location> locationsNeedsToBeSaved = new ArrayList<>(uniqueLocationsFromEvents);
        locationsNeedsToBeSaved.removeAll(existingLocations);
        existingLocations.addAll(locationRepository.saveAll(locationsNeedsToBeSaved));

        Map<String, Location> locationsWithIdAndKey = new HashMap<>();
        existingLocations.forEach(l -> locationsWithIdAndKey.put(l.getCountry() + l.getCity(), l));

        for (AbstractEvent event : events) {
            Location locationWithId = locationsWithIdAndKey.get(event.getLocation().getCountry() + event.getLocation().getCity());
            event.setLocation(locationWithId);
        }

        eventRepository.saveAll(events);
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
}
