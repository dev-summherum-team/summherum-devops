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
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


// Kommunikation mit MongoDB 
public class DatabaseService {

    // Dokumente von TravelEntry speichern
    private final MongoClient mongoClient;
    private final MongoCollection<TravelEntry> collection;
    private final Counter savedEntriesCounter;

    public DatabaseService(PrometheusMeterRegistry registry) {
        // POJO-Support
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        // Verbindung herstellen
        String uri = System.getenv("MONGODB_URI");
        if (uri == null || uri.isBlank()) {
            uri = "mongodb://mongodb:27017";
        }

        String dbName = System.getenv("MONGODB_DB");
        if (dbName == null || dbName.isBlank()) {
            dbName = "travel_journal";
        }

        this.mongoClient = MongoClients.create(uri);

          MongoDatabase database = mongoClient.getDatabase(dbName)
                .withCodecRegistry(pojoCodecRegistry);

        this.collection = database.getCollection("entries", TravelEntry.class);

        // Anzahl gespeicherte Einträge
        savedEntriesCounter = Counter.builder("travel_entries_saved_total")
        .description("Anzahl gespeicherter Reiseeinträge")
        .register(registry);
    }

    // nimmt TravelEntry Objekt & speichert es
    public void saveEntry(TravelEntry entry) {
        collection.insertOne(entry);
        savedEntriesCounter.increment();
    }

    public List<TravelEntry> getAllEntries() {
        List<TravelEntry> entries = new ArrayList<>();
        // find() holt alle Einträge, into() packt sie in unsere Liste
        collection.find().into(entries);
        return entries;
    }

    // löscht Eintrag anhand von MongoDB-ID
    public void deleteEntry(String id) {
        collection.deleteOne(Filters.eq("_id", new org.bson.types.ObjectId(id)));
    }

    // bearbeitet Eintrag anhand von MongoDB-ID
    public void updateEntry(String id, TravelEntry updatedEntry) {
        // ersetzt alten Eintrag vollständig 
        collection.replaceOne(
            Filters.eq("_id", new org.bson.types.ObjectId(id)),
            updatedEntry
        );
    }
}