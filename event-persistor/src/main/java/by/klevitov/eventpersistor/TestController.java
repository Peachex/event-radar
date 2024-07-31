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
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.EventDate;
import by.klevitov.eventradarcommon.dto.EventPrice;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
public class TestController {
    //todo Delete this class.
    @Autowired
    EventRepository eventRepository;

    @Autowired
    LocationRepository locationRepository;

    @GetMapping("/events")
    public List<AbstractEvent> findAllEvents() {
        List<AbstractEvent> events = eventRepository.findAll();
        return events;
    }

    @GetMapping("/events/{sourceType}")
    public List<AbstractEvent> findAllEvents(@PathVariable EventSourceType sourceType) {
        return eventRepository.findBySourceType(sourceType);
    }

    @PostMapping("/events")
    public void createEvents() throws EventParserServiceException {
        EventParserService parserService = new EventParserServiceImpl();
        Map<by.klevitov.eventradarcommon.dto.EventSourceType, EventParser> availableParsers = parserService.retrieveAvailableParsers();
        List<AbstractEvent> events = new ArrayList<>();
        Set<Location> locations = new LinkedHashSet<>();
        for (EventParser parser : availableParsers.values()) {
            List<AbstractEventDTO> eventsDTO = parserService.retrieveEvents(parser);
            for (AbstractEventDTO abstractEventDTO : eventsDTO) {
                AbstractEvent event = convertEventDTO(abstractEventDTO);
                event.setLocation(new Location("testCountry", "testCity"));
                Optional<Location> existingLocation = locationRepository.findByCountryAndCity(event.getLocation().getCountry()
                        , event.getLocation().getCity());
                if (existingLocation.isEmpty()) {
                    existingLocation = Optional.of(locationRepository.save(event.getLocation()));
                }
                event.setLocation(existingLocation.get());
                events.add(event);
            }
        }

        Set<Location> existingLocations = new LinkedHashSet<>(locationRepository.findAll());
        locations.removeAll(existingLocations);
        locationRepository.saveAll(locations);
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
