package by.klevitov.coordinateresolver.client.impl;

import by.klevitov.coordinateresolver.client.GeocodingClient;
import by.klevitov.coordinateresolver.model.GeoCoordinates;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Log4j2
public class OpenStreetMapGeocodingClient implements GeocodingClient {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private static final String USER_AGENT_HEADER_VALUE = "event-coordinate-resolver/1.0";
    private static final String URL_FORMAT_PATTERN = "%s?q=%s&format=json&limit=1";
    private static final String LATITUDE_RESPONSE_KEY = "lat";
    private static final String LONGITUDE_RESPONSE_KEY = "lon";

    private final OkHttpClient httpClient;

    public OpenStreetMapGeocodingClient() {
        this(new OkHttpClient());
    }

    public OpenStreetMapGeocodingClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<GeoCoordinates> fetchCoordinates(final String rawAddress) {
        if (StringUtils.isBlank(rawAddress)) {
            return Optional.empty();
        }

        final Request request = buildRequest(rawAddress);
        Optional<GeoCoordinates> geoCoordinates;

        try (Response response = httpClient.newCall(request).execute()) {
            if (isNotValidResponse(response)) {
                return Optional.empty();
            }
            String jsonBody = Objects.requireNonNull(response.body()).string();
            geoCoordinates = parseResponse(jsonBody);
        } catch (Exception e) {
            log.error("Error fetching coordinates for rawAddress={}.", rawAddress, e);
            geoCoordinates = Optional.empty();
        }

        return geoCoordinates;
    }

    private Request buildRequest(final String rawAddress) {
        String encodedAddress = URLEncoder.encode(rawAddress, StandardCharsets.UTF_8);
        String url = String.format(URL_FORMAT_PATTERN, NOMINATIM_URL, encodedAddress);

        return new Request.Builder()
                .url(url)
                .header(USER_AGENT_HEADER_NAME, USER_AGENT_HEADER_VALUE)
                .get()
                .build();
    }

    private boolean isNotValidResponse(Response response) {
        return (response == null || !response.isSuccessful() || response.body() == null);
    }

    private Optional<GeoCoordinates> parseResponse(final String body) {
        JsonArray results = JsonParser.parseString(body).getAsJsonArray();

        if (results.isEmpty()) {
            return Optional.empty();
        }

        JsonObject obj = results.get(0).getAsJsonObject();
        double lat = Double.parseDouble(obj.get(LATITUDE_RESPONSE_KEY).getAsString());
        double lon = Double.parseDouble(obj.get(LONGITUDE_RESPONSE_KEY).getAsString());

        return Optional.of(new GeoCoordinates(lat, lon));
    }
}
