package by.klevitov.coordinateresolver.client.impl;

import by.klevitov.coordinateresolver.model.GeoCoordinates;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpenStreetMapGeocodingClientTest {
    private OkHttpClient mockedHttpClient;
    private Call mockedCall;
    private Response mockedResponse;
    private ResponseBody mockedBody;
    private OpenStreetMapGeocodingClient client;

    @BeforeEach
    void setUp() {
        mockedHttpClient = mock(OkHttpClient.class);
        mockedCall = mock(Call.class);
        mockedResponse = mock(Response.class);
        mockedBody = mock(ResponseBody.class);
        client = new OpenStreetMapGeocodingClient(mockedHttpClient);
    }

    @Test
    void fetchCoordinates_shouldReturnEmpty_whenAddressIsBlank() {
        assertTrue(client.fetchCoordinates(" ").isEmpty());
        assertTrue(client.fetchCoordinates(null).isEmpty());
    }

    @Test
    void fetchCoordinates_shouldReturnEmpty_whenResponseUnsuccessful() throws IOException {
        when(mockedHttpClient.newCall(any(Request.class))).thenReturn(mockedCall);
        when(mockedCall.execute()).thenReturn(mockedResponse);
        when(mockedResponse.isSuccessful()).thenReturn(false);

        Optional<GeoCoordinates> result = client.fetchCoordinates("rawAddress");
        assertTrue(result.isEmpty());
        verify(mockedResponse, never()).body();
    }

    @Test
    void fetchCoordinates_shouldReturnEmpty_whenResponseBodyNull() throws IOException {
        when(mockedHttpClient.newCall(any(Request.class))).thenReturn(mockedCall);
        when(mockedCall.execute()).thenReturn(mockedResponse);
        when(mockedResponse.isSuccessful()).thenReturn(true);
        when(mockedResponse.body()).thenReturn(null);

        Optional<GeoCoordinates> result = client.fetchCoordinates("rawAddress");
        assertTrue(result.isEmpty());
    }

    @Test
    void fetchCoordinates_shouldReturnCoordinates_whenResponseValid() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("lat", "12.3");
        jsonObject.addProperty("lon", "45.6789");

        JsonArray array = new JsonArray();
        array.add(jsonObject);

        when(mockedHttpClient.newCall(any(Request.class))).thenReturn(mockedCall);
        when(mockedCall.execute()).thenReturn(mockedResponse);
        when(mockedResponse.isSuccessful()).thenReturn(true);
        when(mockedResponse.body()).thenReturn(mockedBody);
        when(mockedBody.string()).thenReturn(array.toString());

        Optional<GeoCoordinates> result = client.fetchCoordinates("rawAddress");

        assertTrue(result.isPresent());
        GeoCoordinates coordinates = result.get();
        assertEquals(12.3, coordinates.latitude());
        assertEquals(45.6789, coordinates.longitude());
    }

    @Test
    void fetchCoordinates_shouldReturnEmpty_whenIOExceptionOccurs() throws IOException {
        when(mockedHttpClient.newCall(any(Request.class))).thenReturn(mockedCall);
        when(mockedCall.execute()).thenThrow(new IOException("Network error"));
        Optional<GeoCoordinates> result = client.fetchCoordinates("rawAddress");
        assertTrue(result.isEmpty());
    }

    @Test
    void fetchCoordinates_shouldReturnEmpty_whenJsonArrayEmpty() throws IOException {
        when(mockedHttpClient.newCall(any(Request.class))).thenReturn(mockedCall);
        when(mockedCall.execute()).thenReturn(mockedResponse);
        when(mockedResponse.isSuccessful()).thenReturn(true);
        when(mockedResponse.body()).thenReturn(mockedBody);
        when(mockedBody.string()).thenReturn("[]");

        Optional<GeoCoordinates> result = client.fetchCoordinates("rawAddress");
        assertTrue(result.isEmpty());
    }
}
