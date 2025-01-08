package by.klevitov.eventradarcommon.healthcheck.checker.impl;

import by.klevitov.eventradarcommon.healthcheck.checker.ResourceChecker;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckExceptionMessage.MESSAGE_BROKER_IS_NOT_AVAILABLE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_DOWN_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.HEALTH_UP_VALUE;
import static by.klevitov.eventradarcommon.healthcheck.constant.HealthCheckMessage.MESSAGE_BROKER_HEALTH_KEY;

@Log4j2
public class RabbitMQResourceChecker implements ResourceChecker {
    private final ConnectionFactory connectionFactory;

    public RabbitMQResourceChecker(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Pair<String, String> checkResourceAvailabilityAndGetResult() {
        String messageBrokerStatus = isMessageBrokerAvailable() ? HEALTH_UP_VALUE : HEALTH_DOWN_VALUE;
        return Pair.of(MESSAGE_BROKER_HEALTH_KEY, messageBrokerStatus);
    }

    private boolean isMessageBrokerAvailable() {
        boolean isAvailable = true;
        try {
            connectionFactory.resetConnection();
            connectionFactory.createConnection().close();
        } catch (Exception e) {
            isAvailable = false;
            log.error(String.format(MESSAGE_BROKER_IS_NOT_AVAILABLE, connectionFactory.getHost(),
                    connectionFactory.getPort()));
        }
        return isAvailable;
    }
}
