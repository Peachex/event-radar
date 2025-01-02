package by.klevitov.eventmanager.manager.service.impl;

import by.klevitov.eventmanager.manager.client.EventPersistorClient;
import by.klevitov.eventmanager.manager.service.EventPersistorClientService;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static by.klevitov.eventradarcommon.dto.EventSourceType.BYCARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
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
    public void test_findEvents() {
        when(mockedPersistorClient.findAll())
                .thenReturn(new ArrayList<>());
        List<AbstractEventDTO> expected = new ArrayList<>();
        List<AbstractEventDTO> actual = persistorClientService.findEvents();
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).findAll();
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
    public void test_deleteEvent() {
        persistorClientService.deleteEvent(anyString());
        verify(mockedPersistorClient, times(1)).delete(anyString());
    }

    @Test
    public void test_deleteEvents() {
        when(mockedPersistorClient.findAll())
                .thenReturn(List.of(
                        AfishaRelaxEventDTO.builder().id("id1").title("title").sourceType(AFISHA_RELAX).build(),
                        ByCardEventDTO.builder().id("id2").title("title").sourceType(BYCARD).build())
                );
        persistorClientService.deleteEvents();
        verify(mockedPersistorClient, times(1)).findAll();
        verify(mockedPersistorClient, times(2)).delete(anyString());
    }
}
