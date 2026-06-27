package summherum.service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import summherum.model.TravelEntry;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


// Dieser Service sich ausschließlich um die Kommunikation mit der MongoDB Datenbank.
public class DatabaseService {

    // Die Collection ist vergleichbar mit einer "Tabelle" in normalen Datenbanken.
    // Hier speichern wir speziell Dokumente vom Typ TravelEntry.
    private final MongoCollection<TravelEntry> collection;

    public DatabaseService() {
        // 1. Der "Übersetzer": Wir sagen MongoDB, dass es unsere Java-Klasse 
        // automatisch in JSON umwandeln darf (das nennt sich POJO-Support).
        CodecRegistry pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        // 2. Verbindung herstellen: Sollte die Environment Variable existieren,
        // wird eine Verbindung zu der dort hinterlegten Datenbank aufgebaut.
        // Ansonsten heißt der Server
        // einfach "mongodb" (genau wie in der docker-compose.yml definiert).
        String uri = System.getenv("MONGODB_URI");
        if (uri == null || uri.isBlank()) {
            uri = "mongodb://mongodb:27017";
        }
        MongoClient mongoClient = MongoClients.create(uri);
        
        // 3. Datenbank auswählen (wird von MongoDB automatisch erstellt, 
        // sobald der erste Eintrag gespeichert wird).
        MongoDatabase database = mongoClient.getDatabase("travel_journal")
                                            .withCodecRegistry(pojoCodecRegistry);
        
        // 4. Die Collection (den "Ordner" für die Einträge) auswählen.
        this.collection = database.getCollection("entries", TravelEntry.class);
    }

    /**
     * Nimmt ein fertiges TravelEntry Objekt und speichert es in der Datenbank.
     */
    public void saveEntry(TravelEntry entry) {
        collection.insertOne(entry);
    }

    /**
     * Geht in die Datenbank, holt alle Dokumente aus der Collection "entries",
     * wandelt sie zurück in Java-Objekte und packt sie in eine Liste.
     */
    public List<TravelEntry> getAllEntries() {
        List<TravelEntry> entries = new ArrayList<>();
        // find() holt alle Einträge, into() packt sie direkt in unsere Liste
        collection.find().into(entries);
        return entries;
    }

    /**
     * Löscht einen Reiseeintrag anhand seiner MongoDB-ID.
     * Die ID kommt später aus der URL, z. B. /api/entries/123...
     */
    public void deleteEntry(String id) {
        collection.deleteOne(Filters.eq("_id", new org.bson.types.ObjectId(id)));
    }

    /**
     * Bearbeitet einen vorhandenen Reiseeintrag anhand seiner MongoDB-ID.
     * replaceOne ersetzt den alten Eintrag vollständig durch den neuen Eintrag.
     */
    public void updateEntry(String id, TravelEntry updatedEntry) {
        collection.replaceOne(
            Filters.eq("_id", new org.bson.types.ObjectId(id)),
            updatedEntry
        );
    }
}