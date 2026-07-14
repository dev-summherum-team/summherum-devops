package summherum;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.javalin.Javalin;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import summherum.service.InspirationService;


public class SetupTest {

    @Test
    public void testJavalinDependency() {
        // Prüft, ob die Javalin-Bibliothek fehlerfrei geladen wurde
        Javalin app = Javalin.create();
        assertNotNull(app, "Javalin Instanz konnte nicht erstellt werden!");
        System.out.println("✅ Javalin Dependency funktioniert.");
    }

    @Test
    public void testJacksonDependency() {
        // Prüft, ob der Jackson ObjectMapper (für die JSON-Umwandlung) bereit ist
        ObjectMapper mapper = new ObjectMapper();
        assertNotNull(mapper, "Jackson ObjectMapper konnte nicht erstellt werden!");
        System.out.println("✅ Jackson (JSON) Dependency funktioniert.");
    }

    @Test
    public void testMongoDbConnection() {
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", "mongodb://localhost:27017");
        // Prüft, ob der Container die Datenbank über das Docker-Netzwerk erreicht
        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
            MongoDatabase database = mongoClient.getDatabase("admin");
            
            // Ein simpler "Ping" Befehl direkt an die Datenbank
            Document ping = database.runCommand(new Document("ping", 1));
            
            assertNotNull(ping, "Die Ping-Antwort der Datenbank war leer.");
            assertEquals(1.0, ping.getDouble("ok"), "Datenbank hat nicht mit 'ok: 1' geantwortet.");
            
            System.out.println("✅ MongoDB Verbindung steht perfekt!");
        } catch (Exception e) {
            fail("❌ Datenbankverbindung fehlgeschlagen: " + e.getMessage());
        }
    }
    @Test
    public void testSaveReadAndDeleteTravelEntryInMongoDb() {
        String mongoUri = System.getenv().getOrDefault("MONGO_URI", "mongodb://localhost:27017");

        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {

            MongoDatabase database = mongoClient.getDatabase("summherum_test");
            MongoCollection<Document> entries = database.getCollection("travel_entries");

            // Testdaten vorher aufräumen
            entries.deleteMany(new Document("title", "Integrationstest Eintrag"));

            // Arrange: Test-Eintrag erstellen
            Document entry = new Document("title", "Integrationstest Eintrag")
                .append("location", "Hamburg")
                .append("text", "Das ist ein Testeintrag aus einem Integrationstest.")
                .append("rating", 5);

            // Act: Eintrag speichern
            entries.insertOne(entry);

            // Act: Eintrag wieder auslesen
            Document savedEntry = entries.find(
                new Document("title", "Integrationstest Eintrag")
            ).first();

            // Assert: prüfen, ob der Eintrag wirklich gespeichert wurde
            assertNotNull(savedEntry, "Der gespeicherte Eintrag wurde nicht gefunden.");
            assertEquals("Integrationstest Eintrag", savedEntry.getString("title"));
            assertEquals("Hamburg", savedEntry.getString("location"));
            assertEquals("Das ist ein Testeintrag aus einem Integrationstest.", savedEntry.getString("text"));
            assertEquals(5, savedEntry.getInteger("rating"));

            // Cleanup: Testdaten wieder löschen
            entries.deleteMany(new Document("title", "Integrationstest Eintrag"));
    }
}

    @Test
public void testJavalinInspirationRouteReturnsResponse() throws Exception {
    InspirationService inspirationService = new InspirationService();

    Javalin app = Javalin.create(config -> {
        config.routes.get("/api/inspiration", ctx -> {
            String vibe = ctx.queryParamAsClass("vibe", String.class).getOrDefault("abenteuer");
            String destination = inspirationService.getRandomDestination(vibe);

            ctx.result(destination);
        });
    });

    app.start(7071);

    try {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:7071/api/inspiration?vibe=warm"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode(), "Die Route sollte HTTP 200 zurückgeben.");
        assertNotNull(response.body(), "Die Antwort darf nicht null sein.");
        assertFalse(response.body().isBlank(), "Die Antwort darf nicht leer sein.");

    } finally {
        app.stop();
    }
}

}
