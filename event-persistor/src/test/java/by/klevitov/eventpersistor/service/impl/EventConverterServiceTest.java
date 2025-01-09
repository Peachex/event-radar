package by.klevitov.eventpersistor.service.impl;

import by.klevitov.eventpersistor.converter.EntityConverter;
import by.klevitov.eventpersistor.entity.AbstractEvent;
import by.klevitov.eventpersistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.service.EntityConverterService;
import by.klevitov.eventpersistor.service.impl.EventConverterService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EventConverterServiceTest {
    private EntityConverterService<AbstractEvent, AbstractEventDTO> service;
    private EntityConverterFactory mockedConverterFactory;
    private EntityConverter mockedConverter;

    @BeforeEach
    public void setUp() {
        mockedConverterFactory = Mockito.mock(EntityConverterFactory.class);
        service = new EventConverterService(mockedConverterFactory);
        mockedConverter = Mockito.mock(EntityConverter.class);
    }

    @Test
    public void test_convertFromDTO_withSingleEvent() {
        AbstractEventDTO eventDTO = AfishaRelaxEventDTO.builder()
                .title("testTitle")
                .sourceType(AFISHA_RELAX)
                .build();
        AbstractEvent expected = AfishaRelaxEvent.builder()
                .title(eventDTO.getTitle())
                .sourceType(eventDTO.getSourceType())
                .build();

        when(mockedConverterFactory.getConverter(eventDTO.getSourceType()))
                .thenReturn(mockedConverter);
        when(mockedConverter.convertFromDTO(eventDTO))
                .thenReturn(expected);

        AbstractEvent actual = service.convertFromDTO(eventDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void test_convertFromDTO_withMultipleEvents() {
        AbstractEventDTO afishaRelaxEventDTO = AfishaRelaxEventDTO.builder()
                .title("testTitle")
                .sourceType(AFISHA_RELAX)
                .build();
        AbstractEventDTO bycardEventDTO = ByCardEventDTO.builder()
                .title("testTitle")
                .sourceType(BYCARD)
                .build();
        List<AbstractEventDTO> eventsDTO = List.of(afishaRelaxEventDTO, bycardEventDTO);

        List<AbstractEvent> expected = List.of(
                AfishaRelaxEvent.builder()
                        .title(eventsDTO.get(0).getTitle()).
                        sourceType(eventsDTO.get(0).getSourceType())
                        .build(),
                ByCardEvent.builder()
                        .title(eventsDTO.get(1).getTitle())
                        .sourceType(eventsDTO.get(1).getSourceType())
                        .build()
        );

        when(mockedConverterFactory.getConverter(eventsDTO.get(0).getSourceType()))
                .thenReturn(mockedConverter);
        when(mockedConverter.convertFromDTO(eventsDTO.get(0)))
                .thenReturn(expected.get(0));
        when(mockedConverterFactory.getConverter(eventsDTO.get(1).getSourceType()))
                .thenReturn(mockedConverter);
        when(mockedConverter.convertFromDTO(eventsDTO.get(1)))
                .thenReturn(expected.get(1));

        List<AbstractEvent> actual = service.convertFromDTO(eventsDTO);
        assertEquals(expected, actual);
    }
}
