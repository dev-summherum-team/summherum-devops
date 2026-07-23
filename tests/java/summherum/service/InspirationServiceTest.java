package summherum.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InspirationServiceTest {

    @Test
    public void shouldReturnWarmDestinationForWarmVibe() {
        InspirationService service = new InspirationService();

        String destination = service.getRandomDestination("warm");

        assertNotNull(destination);
        assertFalse(destination.isBlank());
        assertTrue(
                destination.equals("Bali")
                        || destination.equals("Havanna")
                        || destination.equals("Barcelona")
                        || destination.equals("Phuket")
                        || destination.equals("Malaga")
        );
    }

    @Test
    public void shouldReturnColdDestinationForColdVibe() {
        InspirationService service = new InspirationService();

        String destination = service.getRandomDestination("kalt");

        assertNotNull(destination);
        assertFalse(destination.isBlank());
        assertTrue(
                destination.equals("Reykjavik")
                        || destination.equals("Oslo")
                        || destination.equals("Tromsø")
                        || destination.equals("Patagonien")
                        || destination.equals("Helsinki")
        );
    }

    @Test
    public void shouldReturnAdventureDestinationForUnknownVibe() {
        InspirationService service = new InspirationService();

        String destination = service.getRandomDestination("unbekannt");

        assertNotNull(destination);
        assertFalse(destination.isBlank());
        assertTrue(
                destination.equals("Mexiko")
                        || destination.equals("Tokio")
                        || destination.equals("Sydney")
                        || destination.equals("Österreich")
                        || destination.equals("Kapstadt")
        );
    }
}