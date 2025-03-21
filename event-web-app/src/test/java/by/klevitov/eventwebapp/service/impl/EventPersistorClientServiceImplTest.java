package by.klevitov.eventwebapp.service.impl;

import by.klevitov.eventradarcommon.client.EventPersistorClient;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageResponseDTO;
import by.klevitov.eventradarcommon.pagination.dto.SearchByFieldsRequestDTO;
import by.klevitov.eventwebapp.service.EventPersistorClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventradarcommon.dto.EventSourceType.AFISHA_RELAX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
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
    public void test_findEventsByFields_withoutPagination() {
        when(mockedPersistorClient.findByFields(anyMap(), anyBoolean()))
                .thenReturn(new ArrayList<>());
        List<AbstractEventDTO> expected = new ArrayList<>();
        List<AbstractEventDTO> actual = persistorClientService.findEventsByFields(
                Map.of("fieldName", "fieldTitle"),
                false
        );
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).findByFields(anyMap(), anyBoolean());
    }

    @Test
    public void test_findEventsByFields_withPagination() {
        when(mockedPersistorClient.findByFields(any(SearchByFieldsRequestDTO.class)))
                .thenReturn(new PageResponseDTO<>(new PageImpl<AbstractEventDTO>(new ArrayList<>()), new ArrayList<>()));
        PageResponseDTO<AbstractEventDTO> expected = new PageResponseDTO<>(
                new PageImpl<AbstractEventDTO>(new ArrayList<>()),
                new ArrayList<>());
        PageResponseDTO<AbstractEventDTO> actual = persistorClientService.findEventsByFields(new SearchByFieldsRequestDTO());
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).findByFields(any(SearchByFieldsRequestDTO.class));
    }

    @Test
    public void test_findAllEvents_withoutPagination() {
        when(mockedPersistorClient.findAll())
                .thenReturn(new ArrayList<>());
        List<AbstractEventDTO> expected = new ArrayList<>();
        List<AbstractEventDTO> actual = persistorClientService.findAllEvents();
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).findAll();
    }

    @Test
    public void test_findAllEvents_withPagination() {
        when(mockedPersistorClient.findAll(any(PageRequestDTO.class)))
                .thenReturn(new PageResponseDTO<>(new PageImpl<AbstractEventDTO>(new ArrayList<>()), new ArrayList<>()));
        PageResponseDTO<AbstractEventDTO> expected = new PageResponseDTO<>(
                new PageImpl<AbstractEventDTO>(new ArrayList<>()),
                new ArrayList<>());
        PageResponseDTO<AbstractEventDTO> actual = persistorClientService.findAllEvents(new PageRequestDTO());
        assertEquals(expected, actual);
        verify(mockedPersistorClient, times(1)).findAll(any(PageRequestDTO.class));
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

    @Test
    public void test_deleteEvents() {
        persistorClientService.deleteEvents();
        verify(mockedPersistorClient, times(1)).deleteAll();
    }
}
