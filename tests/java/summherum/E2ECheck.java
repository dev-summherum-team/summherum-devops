package summherum;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class E2ECheck {

    @Test
    public void testRealRunningAppInspirationEndpoint() throws Exception {
        String baseUrl = System.getenv().getOrDefault("URL", "http://localhost:7070");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/inspiration?vibe=warm"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode(), "Die echte App sollte HTTP 200 zurückgeben.");
        assertNotNull(response.body(), "Die Antwort der echten App darf nicht null sein.");
        assertFalse(response.body().isBlank(), "Die Antwort der echten App darf nicht leer sein.");
    }

    @Test
    public void testSystemInspirationEndpointWithDifferentVibes() throws Exception {
        String baseUrl = System.getenv().getOrDefault("URL", "http://localhost:7070");

        HttpClient client = HttpClient.newHttpClient();

        String[] vibes = {"warm", "kalt", "abenteuer"};

        for (String vibe : vibes) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/inspiration?vibe=" + vibe))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            assertEquals(200, response.statusCode(), "Die App sollte für vibe=" + vibe + " HTTP 200 zurückgeben.");
            assertNotNull(response.body(), "Die Antwort darf nicht null sein.");
            assertFalse(response.body().isBlank(), "Die Antwort darf nicht leer sein.");
        }
    }

    @Test
    public void testSystemPackingTemplatesEndpoint() throws Exception {
        String baseUrl = System.getenv().getOrDefault("URL", "http://localhost:7070");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/packing-templates"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode(), "Die Packlisten-Route sollte HTTP 200 zurückgeben.");
        assertNotNull(response.body(), "Die Antwort darf nicht null sein.");
        assertFalse(response.body().isBlank(), "Die Antwort darf nicht leer sein.");
    }
}