package summherum;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.micrometer.MicrometerPlugin;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import summherum.model.TravelEntry;
import summherum.service.DatabaseService;
import summherum.service.ExternalApiService;
import summherum.service.PackingListService;
import summherum.service.InspirationService;

/**
 * Die Hauptklasse unseres Backends. Hier startet der Webserver
 * und die Routen (Endpoints) für das Frontend werden definiert.
 */
public class Main {
    public static void main(String[] args) {

        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerPlugin micrometerPlugin = new MicrometerPlugin(cfg -> cfg.registry = registry);

        // 1. Verbindung zur DB aufbauen
        DatabaseService dbService = new DatabaseService(registry);

        // Unseren API-Agenten erschaffen
        ExternalApiService apiService = new ExternalApiService();

        // Unsere neuen Arbeiter für Packlisten und Zufallsgenerator erschaffen
        PackingListService packingService = new PackingListService();
        InspirationService inspirationService = new InspirationService();

 
        // 2. Den Webserver (Javalin) konfigurieren und starten
        Javalin app = Javalin.create(config -> {
            // CORS aktivieren, damit unser Frontend später (egal von welcher URL)
            // Anfragen an dieses Backend schicken darf.
            config.registerPlugin(micrometerPlugin);
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });

            // FRONTEND: Javalin sagen, wo unsere Webseite liegt
            config.staticFiles.add("public", Location.CLASSPATH);

            // HEALTH-CHECK FÜR GITHUB ACTIONS
            config.routes.get("/health/db", ctx -> {
                String mongoUri = System.getenv("MONGODB_URI");

                if (mongoUri == null || mongoUri.isBlank()) {
                    mongoUri = "mongodb://mongodb:27017";
                }

                try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
                    MongoDatabase database = mongoClient.getDatabase("admin");
                    Document ping = database.runCommand(new Document("ping", 1));

                    Number ok = ping.get("ok", Number.class);

                    if (ok != null && ok.intValue() == 1) {
                        ctx.status(200).result("OK");
                    } else {
                        ctx.status(500).result("MongoDB antwortet nicht korrekt");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Schreibt den Fehler ins Container-Log
                    ctx.status(500).result(e.getMessage());
                }
            });

            // Prometheus-Metriken
            config.routes.get("/prometheus", ctx -> {
            ctx.contentType("text/plain; version=0.0.4; charset=utf-8");
            ctx.result(registry.scrape());
            });

            // ROUTE 1: Alle Einträge abrufen (Laden für die Timeline)
            config.routes.get("/api/entries", ctx -> {
                // Wir holen die Liste vom Service und geben sie direkt als JSON zurück
                ctx.json(dbService.getAllEntries());
            });

            // ROUTE 2: Einen neuen Eintrag speichern
            config.routes.post("/api/entries", ctx -> {
                // 1. JSON in ein Objekt verwandeln
                TravelEntry newEntry = ctx.bodyAsClass(TravelEntry.class);

                // 2. MAGIE: Die fehlenden Daten aus dem Internet laden!
                apiService.enrichTravelEntry(newEntry);

                // 3. Erst jetzt in die Datenbank speichern
                dbService.saveEntry(newEntry);

                ctx.status(201);
                ctx.result("Eintrag erfolgreich gespeichert und mit API-Daten angereichert!");
            });

            // ROUTE 3: Reiseeintrag löschen
            config.routes.delete("/api/entries/{id}", ctx -> {
                String id = ctx.pathParam("id");

                dbService.deleteEntry(id);

                ctx.status(204);
            });

            // ROUTE 4: Reiseeintrag bearbeiten
            config.routes.put("/api/entries/{id}", ctx -> {
                String id = ctx.pathParam("id");
                TravelEntry updatedEntry = ctx.bodyAsClass(TravelEntry.class);

                apiService.enrichTravelEntry(updatedEntry);

                dbService.updateEntry(id, updatedEntry);

                ctx.status(200);
                ctx.result("Eintrag erfolgreich aktualisiert!");
            });

            // --------------------------------------------------------
            // NEUE ROUTEN FÜR PACKLISTEN UND ZUFALL
            // --------------------------------------------------------

            // ROUTE 5: Packlisten-Vorlagen abrufen
            // URL zum Testen: http://localhost:7070/api/packing-templates
            config.routes.get("/api/packing-templates", ctx -> {
                ctx.json(packingService.getAllTemplates());
            });

            // ROUTE 6: Zufalls-Inspiration
            // Das Frontend schickt den Vibe als Parameter mit
            // URL zum Testen: http://localhost:7070/api/inspiration?vibe=warm
            config.routes.get("/api/inspiration", ctx -> {
                // Wir lesen aus der URL den Parameter "vibe" aus
                String vibe = ctx.queryParamAsClass("vibe", String.class).getOrDefault("abenteuer");

                // Holen uns das Zufallsziel
                String destination = inspirationService.getRandomDestination(vibe);

                // Schicken es zurück ans Frontend
                ctx.result(destination);
            });
        }).start(7070); // Der Server lauscht auf Port 7070

        System.out.println("Backend gestartet auf http://localhost:7070");
    }
}