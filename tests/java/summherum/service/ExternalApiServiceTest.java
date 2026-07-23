package summherum.service;

import org.junit.jupiter.api.Test;
import summherum.model.TravelEntry;

import static org.junit.jupiter.api.Assertions.*;

public class ExternalApiServiceTest {

    @Test
    public void shouldDoNothingWhenDestinationIsNull() {
        ExternalApiService service = new ExternalApiService();

        TravelEntry entry = new TravelEntry();
        entry.setDestination(null);

        service.enrichTravelEntry(entry);

        assertNull(entry.getCoordinates());
        assertNull(entry.getCountry());
        assertNull(entry.getContinent());
        assertNull(entry.getWeather());
    }

    @Test
    public void shouldDoNothingWhenDestinationIsEmpty() {
        ExternalApiService service = new ExternalApiService();

        TravelEntry entry = new TravelEntry();
        entry.setDestination("");

        service.enrichTravelEntry(entry);

        assertNull(entry.getCoordinates());
        assertNull(entry.getCountry());
        assertNull(entry.getContinent());
        assertNull(entry.getWeather());
    }

    @Test
    public void shouldDoNothingWhenDestinationContainsOnlySpaces() {
        ExternalApiService service = new ExternalApiService();

        TravelEntry entry = new TravelEntry();
        entry.setDestination("   ");

        service.enrichTravelEntry(entry);

        assertNull(entry.getCoordinates());
        assertNull(entry.getCountry());
        assertNull(entry.getContinent());
        assertNull(entry.getWeather());
    }
}