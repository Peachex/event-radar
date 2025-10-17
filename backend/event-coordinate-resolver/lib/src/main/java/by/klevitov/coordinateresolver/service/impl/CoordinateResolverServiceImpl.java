package by.klevitov.coordinateresolver.service.impl;

import by.klevitov.coordinateresolver.client.GeocodingClient;
import by.klevitov.coordinateresolver.exception.CoordinateResolverServiceException;
import by.klevitov.coordinateresolver.service.CoordinateResolverService;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Log4j2
public class CoordinateResolverServiceImpl implements CoordinateResolverService {
    private final GeocodingClient client;
    private long delayInMillis = 3000L;

    public CoordinateResolverServiceImpl(GeocodingClient client) {
        this.client = client;
    }

    @Override
    public LocationDTO resolve(LocationDTO location) {
        if (location == null) {
            return null;
        }

        String rawAddress = location.getRawAddress();
        if (StringUtils.isBlank(rawAddress)) {
            return location;
        }

        String normalizedAddress = normalizeAddress(rawAddress.trim(), location.getCity());

        client.fetchCoordinates(normalizedAddress).ifPresent(coordinates -> {
            location.setLatitude(coordinates.latitude());
            location.setLongitude(coordinates.longitude());
        });

        pauseBetweenRequests();
        return location;
    }

    @Override
    public List<LocationDTO> resolve(List<LocationDTO> locations) {
        if (CollectionUtils.isNotEmpty(locations)) {
            locations.forEach(this::resolve);
            return locations;
        }

        return Collections.emptyList();
    }

    @Override
    public CoordinateResolverServiceImpl withDelay(long delayInMillis) {
        if (delayInMillis < 0) {
            throw new CoordinateResolverServiceException("Delay must be non-negative.");
        }
        this.delayInMillis = delayInMillis;
        return this;
    }

    private String normalizeAddress(String rawAddress, String city) {
        String normalizedAddress = rawAddress;

        // Remove "г." or "г " prefixes
        normalizedAddress = normalizedAddress.replaceAll("(?iu)\\bг\\.?\\s*", EMPTY);

        // Expand common Russian abbreviations safely
        normalizedAddress = normalizedAddress
                .replaceAll("(?iu)\\bул\\.(?![а-я])\\s*", "улица ")
                .replaceAll("(?iu)\\bул(?!ица)\\b\\s*", "улица ")
                .replaceAll("(?iu)\\bпр-т\\b", "проспект ")
                .replaceAll("(?iu)\\bпросп\\.\\b", "проспект ")
                .replaceAll("(?iu)\\bпер\\.?\\b", "переулок ")
                .replaceAll("(?iu)\\bпл\\.?\\b", "площадь ");

        // Remove apartment / office / floor info (confuses Nominatim)
        normalizedAddress = normalizedAddress
                .replaceAll("(?iu),?\\s*(эт(аж)?\\.?|пом(ещение)?\\.?|оф(ис)?\\.?|кв(артира)?\\.?|комн(ата)?\\.?|каб(инет)?\\.?)\\s*\\d+[а-яa-z0-9\\-]*", EMPTY)
                .replaceAll("(?iu),?\\s*(офис|комн(ата)?|этаж|помещение|кабинет)\\b.*$", "");

        // Cleanup formatting
        normalizedAddress = normalizedAddress.replace(",", " ").replaceAll("\\s+", " ").trim();

        // Ensure city is present at the start
        if (StringUtils.isNotBlank(city) && !normalizedAddress.toLowerCase().contains(city.toLowerCase())) {
            normalizedAddress = city.trim() + " " + normalizedAddress;
        }

        return normalizedAddress;
    }

    private void pauseBetweenRequests() throws RuntimeException {
        try {
            Thread.sleep(delayInMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted during delay between requests. More details: {}", e.getMessage(), e);
            throw new CoordinateResolverServiceException(e);
        }
    }
}
