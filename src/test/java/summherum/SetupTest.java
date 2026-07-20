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
}
