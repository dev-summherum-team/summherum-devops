package summherum;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import io.javalin.Javalin;
import summherum.model.TravelEntry;
import summherum.service.DatabaseService;
import summherum.service.ExternalApiService;
import summherum.service.PackingListService;
import summherum.service.InspirationService;
import io.javalin.http.staticfiles.Location;

/**
 * Die Hauptklasse unseres Backends. Hier startet der Webserver
 * und die Routen (Endpoints) für das Frontend werden definiert.
 */
public class Main {
    public static void main(String[] args) {

        // 1. Verbindung zur DB aufbauen
        DatabaseService dbService = new DatabaseService();

        // Unseren API-Agenten erschaffen
        ExternalApiService apiService = new ExternalApiService();

        // Unsere neuen Arbeiter für Packlisten und Zufallsgenerator erschaffen
        PackingListService packingService = new PackingListService();
        InspirationService inspirationService = new InspirationService();

        // 2. Den Webserver (Javalin) konfigurieren und starten
        Javalin app = Javalin.create(config -> {
            // CORS aktivieren, damit unser Frontend später (egal von welcher URL)
            // Anfragen an dieses Backend schicken darf.
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
            // FRONTEND: Javalin sagen, wo unsere Webseite liegt
        config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070); // Der Server lauscht auf Port 7070

        System.out.println("🚀 Backend gestartet auf http://localhost:7070");

        // --------------------------------------------------------
        // 3. DIE ROUTEN (API Endpoints)
        // --------------------------------------------------------

        // HEALTH-CHECK FÜR GITHUB ACTIONS
    app.get("/health/db", ctx -> {
        String mongoUri = System.getenv("MONGO_URI");

        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
            MongoDatabase database = mongoClient.getDatabase("admin");
            Document ping = database.runCommand(new Document("ping", 1));

            if (ping.getDouble("ok") == 1.0) {
                ctx.status(200).result("OK");
            } else {
                ctx.status(500).result("MongoDB antwortet nicht korrekt");
            }
        } catch (Exception e) {
            ctx.status(500).result("MongoDB nicht erreichbar");
        }
});

        // ROUTE 1: Alle Einträge abrufen (Laden für die Timeline)
        app.get("/api/entries", ctx -> {
            // Wir holen die Liste vom Service und geben sie direkt als JSON zurück
            ctx.json(dbService.getAllEntries());
        });

        // ROUTE 2: Einen neuen Eintrag speichern
        app.post("/api/entries", ctx -> {
            // 1. JSON in ein Objekt verwandeln
            TravelEntry newEntry = ctx.bodyAsClass(TravelEntry.class);
            
            // 2. MAGIE: Die fehlenden Daten aus dem Internet laden!
            apiService.enrichTravelEntry(newEntry);
            
            // 3. Erst jetzt in die Datenbank speichern
            dbService.saveEntry(newEntry);
            
            ctx.status(201);
            ctx.result("Eintrag erfolgreich gespeichert und mit API-Daten angereichert!");
        });
        
        // --------------------------------------------------------
        // NEUE ROUTEN FÜR PACKLISTEN UND ZUFALL
        // --------------------------------------------------------

        // ROUTE 3: Packlisten-Vorlagen abrufen
        // URL zum Testen: http://localhost:7070/api/packing-templates
        app.get("/api/packing-templates", ctx -> {
            ctx.json(packingService.getAllTemplates());
        });

        // ROUTE 4: Zufalls-Inspiration (Das Frontend schickt den Vibe als Parameter mit)
        // URL zum Testen: http://localhost:7070/api/inspiration?vibe=warm
        app.get("/api/inspiration", ctx -> {
            // Wir lesen aus der URL den Parameter "vibe" aus
            String vibe = ctx.queryParamAsClass("vibe", String.class).getOrDefault("abenteuer");
            
            // Holen uns das Zufallsziel
            String destination = inspirationService.getRandomDestination(vibe);
            
            // Schicken es zurück ans Frontend
            ctx.result(destination);
        });

    }
}