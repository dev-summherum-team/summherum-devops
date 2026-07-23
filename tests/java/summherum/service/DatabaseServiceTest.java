package summherum.service;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;
import summherum.model.TravelEntry;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    public class DatabaseServiceTest {

    private DatabaseService createService() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        return new DatabaseService(registry);
    }

    private TravelEntry createTestEntry(String title) {
        TravelEntry entry = new TravelEntry();
        entry.setTitle(title);
        entry.setDestination("Hamburg");
        entry.setDescription("Testeintrag für DatabaseService");
        entry.setAuthor("JUnit");
        entry.setRating(5);
        entry.setStartDate("2026-07-01");
        entry.setEndDate("2026-07-02");
        return entry;
    }

    @Test
    public void shouldSaveAndReadEntry() {
        DatabaseService service = createService();

        TravelEntry entry = createTestEntry("DatabaseService Test Save");

        service.saveEntry(entry);

        List<TravelEntry> entries = service.getAllEntries();

        TravelEntry savedEntry = entries.stream()
                .filter(e -> "DatabaseService Test Save".equals(e.getTitle()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedEntry);
        assertNotNull(savedEntry.getId());
        assertEquals("Hamburg", savedEntry.getDestination());
        assertEquals("Testeintrag für DatabaseService", savedEntry.getDescription());
        assertEquals("JUnit", savedEntry.getAuthor());
        assertEquals(5, savedEntry.getRating());

        service.deleteEntry(savedEntry.getId());
    }

    @Test
    public void shouldDeleteEntry() {
        DatabaseService service = createService();

        TravelEntry entry = createTestEntry("DatabaseService Test Delete");

        service.saveEntry(entry);

        TravelEntry savedEntry = service.getAllEntries().stream()
                .filter(e -> "DatabaseService Test Delete".equals(e.getTitle()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedEntry);

        service.deleteEntry(savedEntry.getId());

        boolean stillExists = service.getAllEntries().stream()
                .anyMatch(e -> "DatabaseService Test Delete".equals(e.getTitle()));

        assertFalse(stillExists);
    }

    @Test
    public void shouldUpdateEntry() {
        DatabaseService service = createService();

        TravelEntry entry = createTestEntry("DatabaseService Test Update");

        service.saveEntry(entry);

        TravelEntry savedEntry = service.getAllEntries().stream()
                .filter(e -> "DatabaseService Test Update".equals(e.getTitle()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedEntry);

        TravelEntry updatedEntry = createTestEntry("DatabaseService Test Updated");
        updatedEntry.setDescription("Eintrag wurde aktualisiert");
        updatedEntry.setDestination("Berlin");
        updatedEntry.setRating(4);

        service.updateEntry(savedEntry.getId(), updatedEntry);

        TravelEntry changedEntry = service.getAllEntries().stream()
                .filter(e -> "DatabaseService Test Updated".equals(e.getTitle()))
                .findFirst()
                .orElse(null);

        assertNotNull(changedEntry);
        assertEquals("Eintrag wurde aktualisiert", changedEntry.getDescription());
        assertEquals("Berlin", changedEntry.getDestination());
        assertEquals(4, changedEntry.getRating());

        service.deleteEntry(changedEntry.getId());
    }
}