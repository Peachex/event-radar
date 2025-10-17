package by.klevitov.eventmanager.util;

import by.klevitov.coordinateresolver.service.CoordinateResolverService;
import by.klevitov.eventmanager.constant.ManagerExceptionMessage;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Log4j2
public final class TaskExecutorUtil {
    private TaskExecutorUtil() {
    }

    public static void logException(final Exception e, final String taskName) {
        final String exceptionMessage = String.format(ManagerExceptionMessage.TASK_EXECUTION_ERROR, taskName, e.getMessage());
        log.error(exceptionMessage, e);
    }

    public static void resolveCoordinatesIfNeeded(final List<AbstractEventDTO> events,
                                                  final CoordinateResolverService coordinateResolver) {
        if (CollectionUtils.isNotEmpty(events)) {
            events.stream()
                    .map(AbstractEventDTO::getLocation)
                    .filter(Objects::nonNull)
                    .filter(l -> l.getLatitude() == 0.0 && l.getLongitude() == 0.0)
                    .forEach(coordinateResolver::resolve);
        }
    }
}
