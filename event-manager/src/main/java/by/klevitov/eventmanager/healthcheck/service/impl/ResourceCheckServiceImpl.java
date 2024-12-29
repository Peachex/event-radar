package by.klevitov.eventmanager.healthcheck.service.impl;

import by.klevitov.eventmanager.healthcheck.service.ResourceCheckService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckExceptionMessage.MESSAGE_BROKER_IS_NOT_AVAILABLE;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_DOWN_VALUE;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;
import static by.klevitov.eventmanager.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_UP_VALUE;

@Log4j2
@Service
public class ResourceCheckServiceImpl implements ResourceCheckService {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public ResourceCheckServiceImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Pair<String, String> checkMessageBrokerAvailabilityAndGetResult() {
        String messageBrokerStatus = isMessageBrokerAvailable() ? MESSAGE_BROKER_HEALTH_UP_VALUE
                : MESSAGE_BROKER_HEALTH_DOWN_VALUE;
        return Pair.of(MESSAGE_BROKER_HEALTH_KEY, messageBrokerStatus);
    }

    private boolean isMessageBrokerAvailable() {
        boolean isAvailable = true;
        try {
            connectionFactory.createConnection().close();
        } catch (Exception e) {
            isAvailable = false;
            log.error(String.format(
                    MESSAGE_BROKER_IS_NOT_AVAILABLE,
                    connectionFactory.getHost(),
                    connectionFactory.getPort()));
        }
        return isAvailable;
    }
}
