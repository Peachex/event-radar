package by.klevitov.eventmanager.manager.service;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;

import java.util.List;

public interface EventFetcherService {
    List<AbstractEventDTO> fetch() throws Exception;
}
