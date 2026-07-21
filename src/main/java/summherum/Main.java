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

public class Main {
    public static void main(String[] args) {

        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerPlugin micrometerPlugin = new MicrometerPlugin(cfg -> cfg.registry = registry);

        // 1. Verbindung zur DB aufbauen
        DatabaseService dbService = new DatabaseService(registry);

        // APIs einbinden
        ExternalApiService apiService = new ExternalApiService();
        PackingListService packingService = new PackingListService();
        InspirationService inspirationService = new InspirationService();

        // 2. Javalin konfigurieren & starten
        Javalin app = Javalin.create(config -> {
            // CORS aktivieren, damit Frontend Anfragen an Backend schicken darf
            config.registerPlugin(micrometerPlugin);
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });

            // Frontend Pfad
            config.staticFiles.add("public", Location.CLASSPATH);

            // health check
            config.routes.get("/health/db", ctx -> {
                String mongoUri = System.getenv("MONGODB_URI");

                if (mongoUri == null || mongoUri.isBlank()) {
                    mongoUri = "mongodb://localhost:27017";
                }

                try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
                    MongoDatabase database = mongoClient.getDatabase("admin");
                    Document ping = database.runCommand(new Document("ping", 1));

                    Number ok = ping.get("ok", Number.class);

                    if (ok != null && ok.intValue() == 1) {
                        ctx.status(200).result("OK");
                    } else {
                        ctx.status(500).result("MongoDB antwortet nicht");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // loggen
                    ctx.status(500).result(e.getMessage());
                }
            });

            // Prometheus-Metriken
            config.routes.get("/prometheus", ctx -> {
            ctx.contentType("text/plain; version=0.0.4; charset=utf-8");
            ctx.result(registry.scrape());
            });

            // Alle Einträge abrufen 
            config.routes.get("/api/entries", ctx -> {
                ctx.json(dbService.getAllEntries());
            });

            // neuen Eintrag speichern
            config.routes.post("/api/entries", ctx -> {
                TravelEntry newEntry = ctx.bodyAsClass(TravelEntry.class);

                // mit API Daten
                apiService.enrichTravelEntry(newEntry);

                // jetzt in Datenbank speichern
                dbService.saveEntry(newEntry);
                ctx.status(201);
                ctx.result("Eintrag gespeichert und mit API-Daten angereichert!");
            });

            // Eintrag löschen
            config.routes.delete("/api/entries/{id}", ctx -> {
                String id = ctx.pathParam("id");
                dbService.deleteEntry(id);
                ctx.status(204);
            });

            // Eintrag bearbeiten
            config.routes.put("/api/entries/{id}", ctx -> {
                String id = ctx.pathParam("id");
                TravelEntry updatedEntry = ctx.bodyAsClass(TravelEntry.class);
                apiService.enrichTravelEntry(updatedEntry);
                dbService.updateEntry(id, updatedEntry);
                ctx.status(200);
                ctx.result("Eintrag aktualisiert!");
            });

            // Packlisten-Vorlagen abrufen
            config.routes.get("/api/packing-templates", ctx -> {
                ctx.json(packingService.getAllTemplates());
            });

            // Zufalls-Inspiration
            config.routes.get("/api/inspiration", ctx -> {
                // lesen aus URL den Parameter "vibe" aus
                String vibe = ctx.queryParamAsClass("vibe", String.class).getOrDefault("abenteuer");
                String destination = inspirationService.getRandomDestination(vibe);
                ctx.result(destination);
            });
        }).start(7070); // Port 7070

        System.out.println("Backend gestartet auf Port 7070");
    }
}