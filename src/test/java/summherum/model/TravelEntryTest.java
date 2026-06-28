package summherum.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TravelEntryTest {
// prüft ob titel, Ziel, Autor und Beschreibung korrekt gespeichert und gelesen werden können
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
// prüft Startdatum, Enddatum und Bewertung
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
}