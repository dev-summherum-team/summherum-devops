package summherum.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import summherum.model.TravelEntry;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Dieser Service funkt externe APIs an,
 * um fehlende Daten (Koordinaten, Land, Wetter) automatisch auszufüllen.
 */
public class ExternalApiService {

    // Der eingebaute Java HTTP-Client (unser "Browser" im Code)
    private final HttpClient httpClient;
    
    // Unser Jackson-Werkzeug, um den API-Text (JSON) in einen durchsuchbaren Baum zu verwandeln
    private final ObjectMapper objectMapper;

    public ExternalApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Nimmt einen frischen Reise-Eintrag, schnappt sich das 'destination' (Reiseziel)
     * und lädt alle extra Infos aus dem Internet herunter.
     */
    public void enrichTravelEntry(TravelEntry entry) {
        String destination = entry.getDestination();
        
        // Wenn kein Ort angegeben wurde, können wir auch nichts suchen.
        if (destination == null || destination.trim().isEmpty()) {
            return;
        }

        try {
            // ---------------------------------------------------------
            // SCHRITT 1: Geocoding (Text in Koordinaten umwandeln)
            // ---------------------------------------------------------
            // Wir ersetzen Leerzeichen im Städtenamen durch '%20' für die URL
            String formattedCity = destination.replace(" ", "%20");
            String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + formattedCity + "&count=1&language=de";

            HttpRequest geoRequest = HttpRequest.newBuilder().uri(URI.create(geoUrl)).GET().build();
            HttpResponse<String> geoResponse = httpClient.send(geoRequest, HttpResponse.BodyHandlers.ofString());

            // JSON auslesen
            JsonNode geoJson = objectMapper.readTree(geoResponse.body());
            JsonNode results = geoJson.path("results");

            // Wenn die API den Ort gefunden hat, ist die Liste nicht leer
            if (results.isArray() && !results.isEmpty()) {
                JsonNode locationData = results.get(0); // Den ersten (besten) Treffer nehmen

                // Daten auslesen und direkt in unser TravelEntry Objekt speichern
                double lat = locationData.path("latitude").asDouble();
                double lng = locationData.path("longitude").asDouble();
                
                TravelEntry.Coordinates coords = new TravelEntry.Coordinates();
                coords.latitude = lat;
                coords.longitude = lng;
                entry.setCoordinates(coords);

                // Land und Kontinent abgreifen (Die API liefert die Zeitzone, z.B. "Europe/Berlin")
                entry.setCountry(locationData.path("country").asText());
                String timezone = locationData.path("timezone").asText();
                if (timezone.contains("/")) {
                    entry.setContinent(timezone.split("/")[0]); // Schneidet "Europe" aus
                }

                // ---------------------------------------------------------
                // SCHRITT 2: Aktuelles Wetter für diese Koordinaten abrufen
                // ---------------------------------------------------------
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lng + "&current_weather=true";
                
                HttpRequest weatherRequest = HttpRequest.newBuilder().uri(URI.create(weatherUrl)).GET().build();
                HttpResponse<String> weatherResponse = httpClient.send(weatherRequest, HttpResponse.BodyHandlers.ofString());

                JsonNode weatherJson = objectMapper.readTree(weatherResponse.body());
                JsonNode currentWmo = weatherJson.path("current_weather");

                TravelEntry.Weather weather = new TravelEntry.Weather();
                weather.temperature = currentWmo.path("temperature").asDouble();
                
                // Ein bisschen Entwickler-Magie für das Wetter (0 = Sonnig, alles andere bewölkt/Regen)
                int wCode = currentWmo.path("weathercode").asInt();
                weather.condition = (wCode == 0 || wCode == 1) ? "Sonnig/Klar" : "Wolkig/Niederschlag";
                
                entry.setWeather(weather);
            }
            
        } catch (Exception e) {
            // Wenn das Internet weg ist oder die API streikt, stürzt das Programm nicht ab.
            // Es speichert den Eintrag dann einfach ohne die Extra-Daten.
            System.err.println("Fehler beim API-Abruf: " + e.getMessage());
        }
    }
}