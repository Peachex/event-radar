package by.klevitov.coordinateresolver.service.impl;

import by.klevitov.coordinateresolver.client.GeocodingClient;
import by.klevitov.coordinateresolver.client.impl.OpenStreetMapGeocodingClient;
import by.klevitov.coordinateresolver.model.GeoCoordinates;
import by.klevitov.coordinateresolver.service.CoordinateResolverService;
import by.klevitov.eventradarcommon.dto.LocationDTO;

import java.util.List;
import java.util.Optional;

public class CoordinateResolverServiceImpl implements CoordinateResolverService {
    private final GeocodingClient client;

    public CoordinateResolverServiceImpl() {
        this.client = new OpenStreetMapGeocodingClient();
    }

    @Override
    public LocationDTO resolve(LocationDTO location) {
        if (location == null) return null;
        String rawAddress = location.getRawAddress();
        if (rawAddress == null || rawAddress.isBlank()) return location;

        // Step 1. Normalize and enrich the address
        String normalized = normalizeAddress(rawAddress.trim(), location.getCity());

        // Step 2. Call the geocoder
        Optional<GeoCoordinates> maybeCoords = client.fetchCoordinates(normalized);

        // Step 3. Apply coordinates if found
        maybeCoords.ifPresent(coords -> {
            location.setLatitude(coords.latitude());
            location.setLongitude(coords.longitude());
        });

        return location;
    }

    @Override
    public List<LocationDTO> resolve(List<LocationDTO> locations) {
        locations.forEach(this::resolve);
        return locations;
    }

    private String normalizeAddress(String raw, String city) {
        String result = raw;

        // Step 0: Remove "г." or "г " prefixes entirely
        result = result.replaceAll("(?i)\\bг\\.?\\s*", "");

        // Step 1: Expand common Russian abbreviations
        result = result.replaceAll("(?i)\\bул\\.?\\s*", "улица ");
        result = result.replaceAll("(?i)\\bпр-т\\b", "проспект ");
        result = result.replaceAll("(?i)\\bпросп\\.\\b", "проспект ");
        result = result.replaceAll("(?i)\\bпер\\.?\\b", "переулок ");
        result = result.replaceAll("(?i)\\bпл\\.?\\b", "площадь ");

        // Step 2: Remove apartment / office / floor info
// Step 2: Remove apartment / office / floor info (they confuse Nominatim)
        result = result.replaceAll(
                "(?i),?\\s*(эт(аж)?\\.?|пом(ещение)?\\.?|оф(ис)?\\.?|кв(артира)?\\.?|комн(ата)?\\.?|каб(инет)?\\.?)\\s*\\d+[а-яА-ЯA-Za-z0-9\\-]*",
                ""
        );
        result = result.replaceAll("(?i),?\\s*(офис|комн(ата)?|этаж|помещение|кабинет)\\b.*$", "");


        // Step 3: Remove all commas
        result = result.replace(",", " ");

        // Step 4: Clean up spaces
        result = result.replaceAll("\\s+", " ").trim();

        // Step 5: Ensure city name is present at the beginning
        if (city != null && !city.isBlank()) {
            String lower = result.toLowerCase();
            if (!lower.contains(city.toLowerCase())) {
                result = city.trim() + " " + result;
            }
        }

        return result;
    }
}
