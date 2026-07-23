package summherum.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TravelEntryTest {
// prüft ob titel, Ziel, Autor und Beschreibung gespeichert & gelesen werden können
    @Test
    void shouldStoreBasicTravelEntryData() {
        TravelEntry entry = new TravelEntry();

        entry.setTitle("Sommerurlaub");
        entry.setDestination("Rom");
        entry.setAuthor("Otter");
        entry.setDescription("Schöne Reise");

        assertEquals("Sommerurlaub", entry.getTitle());
        assertEquals("Rom", entry.getDestination());
        assertEquals("Otter", entry.getAuthor());
        assertEquals("Schöne Reise", entry.getDescription());
    }
// prüft Start-, Enddatum und Bewertung
    @Test
    void shouldStoreTravelDatesAndRating() {
        TravelEntry entry = new TravelEntry();

        entry.setStartDate("2026-07-01");
        entry.setEndDate("2026-07-14");
        entry.setRating(5);

        assertEquals("2026-07-01", entry.getStartDate());
        assertEquals("2026-07-14", entry.getEndDate());
        assertEquals(5, entry.getRating());
    }
// prüft alles was mit der Packliste zu tun hat
    @Test
    void shouldStorePackingItemPackedStatus() {
        TravelEntry.PackingItem item = new TravelEntry.PackingItem("Sonnencreme", false);

        assertEquals("Sonnencreme", item.name);
        assertFalse(item.isPacked);

        item.isPacked = true;

        assertTrue(item.isPacked);
    }
// prüft ID, Land und Kontinent
    @Test
    void shouldStoreIdCountryAndContinent() {
        TravelEntry entry = new TravelEntry();

        entry.setId("507f1f77bcf86cd799439011");
        entry.setCountry("Deutschland");
        entry.setContinent("Europa");

        assertEquals("507f1f77bcf86cd799439011", entry.getId());
        assertEquals("Deutschland", entry.getCountry());
        assertEquals("Europa", entry.getContinent());
    }

// prüft Koordinaten
    @Test
    void shouldStoreCoordinates() {
        TravelEntry entry = new TravelEntry();

        TravelEntry.Coordinates coordinates = new TravelEntry.Coordinates();
        coordinates.latitude = 53.5511;
        coordinates.longitude = 9.9937;

        entry.setCoordinates(coordinates);

        assertNotNull(entry.getCoordinates());
        assertEquals(53.5511, entry.getCoordinates().latitude);
        assertEquals(9.9937, entry.getCoordinates().longitude);
    }

// prüft Wetterdaten
    @Test
    void shouldStoreWeather() {
        TravelEntry entry = new TravelEntry();

        TravelEntry.Weather weather = new TravelEntry.Weather();
        weather.temperature = 22.5;
        weather.condition = "Sonnig/Klar";

        entry.setWeather(weather);

        assertNotNull(entry.getWeather());
        assertEquals(22.5, entry.getWeather().temperature);
        assertEquals("Sonnig/Klar", entry.getWeather().condition);
    }

// prüft Packliste
    @Test
    void shouldStorePackingList() {
        TravelEntry entry = new TravelEntry();

        TravelEntry.PackingItem item1 = new TravelEntry.PackingItem("Powerbank", false);
        TravelEntry.PackingItem item2 = new TravelEntry.PackingItem("Kamera", true);

        List<TravelEntry.PackingItem> packingList = List.of(item1, item2);

        entry.setPackingList(packingList);

        assertNotNull(entry.getPackingList());
        assertEquals(2, entry.getPackingList().size());

        assertEquals("Powerbank", entry.getPackingList().get(0).name);
        assertFalse(entry.getPackingList().get(0).isPacked);

        assertEquals("Kamera", entry.getPackingList().get(1).name);
        assertTrue(entry.getPackingList().get(1).isPacked);
    }

// prüft, ob die Packliste standardmäßig leer, aber nicht null ist
    @Test
    void defaultPackingListShouldNotBeNull() {
        TravelEntry entry = new TravelEntry();

        assertNotNull(entry.getPackingList());
        assertTrue(entry.getPackingList().isEmpty());
    }


}