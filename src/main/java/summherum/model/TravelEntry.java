package summherum.model;

import java.util.List;
import java.util.ArrayList;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.bson.BsonType;

/**
 * Diese Klasse ist unser Haupt-Bauplan (Modell) für eine Reise.
 * Jedes Mal, wenn ein User auf "Speichern" drückt, wird aus seinen
 * Eingaben ein Objekt dieser Klasse erstellt und als JSON in die MongoDB geschoben & gespeichert.
 */
public class TravelEntry {

    // --------------------------------------------------------
    // 1. DIE BASICS (Direkte Eingaben & generierte Werte)
    // --------------------------------------------------------

    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;             // Die eindeutige ID. MongoDB braucht immer eine "_id". 
    private String title;          // z.B. "Sommerurlaub 2026"
    private String description;    // Der eigentliche Tagebuch-Text
    private String destination;    // z.B. "Barcelona"
    private String country;        // Wird später von der API gefüllt
    private String continent;      // Wird später von der API gefüllt
    private String startDate;      // Als Text (z.B. "2026-07-01") der Einfachheit halber
    private String endDate;        // Als Text (z.B. "2026-07-14")
    private String author;         // Der lowkey Benutzername (z.B. "an-otter")
    
    // Eine Liste für die optionalen Vibes (z.B. "#strand", "#party")
    private List<String> tags = new ArrayList<>();


    // --------------------------------------------------------
    // 2. VERSCHACHTELTE OBJEKTE (Die Stärke von MongoDB)
    // --------------------------------------------------------

    private Coordinates coordinates;
    private Weather weather;
    private List<PackingItem> packingList = new ArrayList<>();


    // --------------------------------------------------------
    // 3. PFLICHT-KONSTRUKTOR FÜR JACKSON (JSON-Wandler)
    // --------------------------------------------------------
    // Jackson (unser Werkzeug, das Java in JSON verwandelt) MUSS immer 
    // einen komplett leeren Konstruktor haben, um Objekte bauen zu können.
    public TravelEntry() {
    }


    // --------------------------------------------------------
    // 4. INNERE KLASSEN FÜR DIE STRUKTUR
    // --------------------------------------------------------
    // Um nicht für jede Kleinigkeit eine extra Datei anlegen zu müssen,
    // definieren wir unsere Hilfs-Baupläne direkt hier "statisch" mit rein.

    public static class Coordinates {
        public double latitude;  // Breitengrad (z.B. 41.3851)
        public double longitude; // Längengrad (z.B. 2.1734)

        // Leerer Konstruktor für Jackson
        public Coordinates() {}
    }

    public static class Weather {
        public double temperature;  // z.B. 28.5
        public String condition;    // z.B. "Sunny" oder "Rain"

        public Weather() {}
    }

    public static class PackingItem {
        public String name;       // z.B. "Sonnencreme"
        public boolean isPacked;  // true (ja) oder false (nein)

        public PackingItem() {}
        
        // Praktischer Konstruktor zum schnellen Erstellen im Code
        public PackingItem(String name, boolean isPacked) {
            this.name = name;
            this.isPacked = isPacked;
        }
    }

    // --------------------------------------------------------
    // 5. GETTER & SETTER
    // --------------------------------------------------------
 
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }

    public Weather getWeather() { return weather; }
    public void setWeather(Weather weather) { this.weather = weather; }

    public List<PackingItem> getPackingList() { return packingList; }
    public void setPackingList(List<PackingItem> packingList) { this.packingList = packingList; }
}
