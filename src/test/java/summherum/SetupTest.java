package summherum;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.javalin.Javalin;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SetupTest {

    @Test
    public void testJavalinDependency() {
        // Prüft, ob Javalin-Bibliothek fehlerfrei geladen wurde
        Javalin app = Javalin.create();
        assertNotNull(app, "Javalin Instanz konnte nicht erstellt werden!");
        System.out.println("✅ Javalin Dependency funktioniert.");
    }

    @Test
    public void testJacksonDependency() {
        // Prüft, ob Jackson ObjectMapper bereit ist
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
            
            // ping an DB
            Document ping = database.runCommand(new Document("ping", 1));
            
            assertNotNull(ping, "Die Ping-Antwort der Datenbank war leer.");
            assertEquals(1.0, ping.getDouble("ok"), "Datenbank hat nicht mit 'ok: 1' geantwortet.");
            
            System.out.println("✅ MongoDB Verbindung steht perfekt!");
        } catch (Exception e) {
            fail("❌ Datenbankverbindung fehlgeschlagen: " + e.getMessage());
        }
    }
}
