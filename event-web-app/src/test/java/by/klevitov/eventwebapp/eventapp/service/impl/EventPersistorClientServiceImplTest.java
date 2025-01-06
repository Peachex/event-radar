package by.klevitov.eventwebapp.eventapp.service.impl;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventwebapp.eventapp.client.EventPersistorClient;
import by.klevitov.eventwebapp.eventapp.service.EventPersistorClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventPersistorClientServiceImplTest {
    private EventPersistorClientService persistorClientService;
    private EventPersistorClient mockedPersistorClient;

    @BeforeEach
    public void setUp() {
        mockedPersistorClient = Mockito.mock(EventPersistorClient.class);
        persistorClientService = new EventPersistorClientServiceImpl(mockedPersistorClient);
    }

    @Test
    public void test_createEvent() {
        AbstractEventDTO event = AfishaRelaxEventDTO.builder().id("id").title("title").sourceType(AFISHA_RELAX).build();
        when(mockedPersistorClient.create(any(AbstractEventDTO.class)))
                .thenReturn(event);
        AbstractEventDTO expected = event;
        AbstractEventDTO actual = persistorClientService.createEvent(AfishaRelaxEventDTO.builder().title("title")
                .sourceType(AFISHA_RELAX).build());
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).create(any(AbstractEventDTO.class));
    }

    @Test
    public void test_createEvents() {
        when(mockedPersistorClient.create(anyList()))
                .thenReturn(new ArrayList<>());
        List<AbstractEventDTO> expected = new ArrayList<>();
        List<AbstractEventDTO> actual = persistorClientService.createEvents(new ArrayList<>());
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).create(anyList());
    }

    @Test
    public void test_findEventById() {
        AbstractEventDTO event = AfishaRelaxEventDTO.builder().id("id").title("title").sourceType(AFISHA_RELAX).build();
        when(mockedPersistorClient.findById(anyString()))
                .thenReturn(event);
        AbstractEventDTO expected = event;
        AbstractEventDTO actual = persistorClientService.findEventById("id");
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).findById(anyString());
    }

    @Test
    public void test_findEventsByFields() {
        when(mockedPersistorClient.findByFields(anyMap()))
                .thenReturn(new ArrayList<>());
        List<AbstractEventDTO> expected = new ArrayList<>();
        List<AbstractEventDTO> actual = persistorClientService.findEventsByFields(Map.of("fieldName", "fieldTitle"));
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).findByFields(anyMap());
    }

    @Test
    public void test_findAllEvents() {
        when(mockedPersistorClient.findAll())
                .thenReturn(new ArrayList<>());
        List<AbstractEventDTO> expected = new ArrayList<>();
        List<AbstractEventDTO> actual = persistorClientService.findAllEvents();
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).findAll();
    }

    @Test
    public void test_updateEvent() {
        AbstractEventDTO updatedEvent = AfishaRelaxEventDTO.builder().id("id").title("updatedTitle")
                .sourceType(AFISHA_RELAX).build();
        when(mockedPersistorClient.update(any(AbstractEventDTO.class)))
                .thenReturn(updatedEvent);
        AbstractEventDTO expected = updatedEvent;
        AbstractEventDTO actual = persistorClientService.updateEvent(AfishaRelaxEventDTO.builder().id("id")
                .title("updatedTitle").build());
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).update(any(AbstractEventDTO.class));
    }

    @Test
    public void test_deleteEvent() {
        persistorClientService.deleteEvent(anyString());
        verify(mockedPersistorClient, times(1)).delete(anyString());
    }
}
