package by.klevitov.coordinateresolver.client.impl;

import by.klevitov.coordinateresolver.client.GeocodingClient;
import by.klevitov.coordinateresolver.model.GeoCoordinates;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class OpenStreetMapGeocodingClient implements GeocodingClient {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private final OkHttpClient httpClient = new OkHttpClient();


    //todo: Think about object creation for all classes.
    public OpenStreetMapGeocodingClient() {
    }

    @Override
    public Optional<GeoCoordinates> fetchCoordinates(final String rawAddress) {
        if (rawAddress == null || rawAddress.isBlank()) {
            return Optional.empty();
        }

        String encoded = URLEncoder.encode(rawAddress, StandardCharsets.UTF_8);
        String url = String.format("%s?q=%s&format=json&limit=1", NOMINATIM_URL, encoded);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "event-coordinate-resolver/1.0")
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return Optional.empty();
            }

            String body = response.body().string();
            JsonArray results = JsonParser.parseString(body).getAsJsonArray();

            if (results.isEmpty()) return Optional.empty();

            JsonObject obj = results.get(0).getAsJsonObject();
            double lat = Double.parseDouble(obj.get("lat").getAsString());
            double lon = Double.parseDouble(obj.get("lon").getAsString());

            return Optional.of(new GeoCoordinates(lat, lon));

        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
