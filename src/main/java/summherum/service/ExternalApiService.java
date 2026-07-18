package summherum.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import summherum.model.TravelEntry;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// für externe APIs
public class ExternalApiService {

    // vorhandene HTTP-Client
    private final HttpClient httpClient;
    
    // Jackson-Werkzeug
    private final ObjectMapper objectMapper;

    public ExternalApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // fügt Reiseort automatisch hinzu
    public void enrichTravelEntry(TravelEntry entry) {
        String destination = entry.getDestination();
        
        if (destination == null || destination.trim().isEmpty()) {
            return;
        }

        try {
            // Geocoding Text -> Koordinaten
            // Leerzeichen im Städtenamen durch '%20' für die URL
            String formattedCity = destination.replace(" ", "%20");
            String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + formattedCity + "&count=1&language=de";

            HttpRequest geoRequest = HttpRequest.newBuilder().uri(URI.create(geoUrl)).GET().build();
            HttpResponse<String> geoResponse = httpClient.send(geoRequest, HttpResponse.BodyHandlers.ofString());

            // JSON auslesen
            JsonNode geoJson = objectMapper.readTree(geoResponse.body());
            JsonNode results = geoJson.path("results");

            // Wenn API den Ort gefunden hat, ist Liste nicht leer
            if (results.isArray() && !results.isEmpty()) {
                JsonNode locationData = results.get(0); // erstbesten Treffer nehmen

                // in TravelEntry Objekt speichern
                double lat = locationData.path("latitude").asDouble();
                double lng = locationData.path("longitude").asDouble();
                
                TravelEntry.Coordinates coords = new TravelEntry.Coordinates();
                coords.latitude = lat;
                coords.longitude = lng;
                entry.setCoordinates(coords);

                // Land und Kontinent abgreifen, API liefert die Zeitzone
                entry.setCountry(locationData.path("country").asText());
                String timezone = locationData.path("timezone").asText();
                if (timezone.contains("/")) {
                    entry.setContinent(timezone.split("/")[0]); 
                }

                // Wetter abrufen
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lng + "&current_weather=true";
                
                HttpRequest weatherRequest = HttpRequest.newBuilder().uri(URI.create(weatherUrl)).GET().build();
                HttpResponse<String> weatherResponse = httpClient.send(weatherRequest, HttpResponse.BodyHandlers.ofString());

                JsonNode weatherJson = objectMapper.readTree(weatherResponse.body());
                JsonNode currentWmo = weatherJson.path("current_weather");

                TravelEntry.Weather weather = new TravelEntry.Weather();
                weather.temperature = currentWmo.path("temperature").asDouble();
                
                // einfache Interpretationen in Sonne/Regen
                int wCode = currentWmo.path("weathercode").asInt();
                weather.condition = (wCode == 0 || wCode == 1) ? "Sonnig/Klar" : "Wolkig/Niederschlag";
                
                entry.setWeather(weather);
            }
            
        } catch (Exception e) {
            // speichert Eintrag ohne Extra-Daten & verhindert Absturz
            System.err.println("Fehler beim API-Abruf: " + e.getMessage());
        }
    }
}