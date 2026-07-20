package summherum.model;

import java.util.List;
import java.util.ArrayList;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.bson.BsonType;

// Bauplan für Einträge
public class TravelEntry {

    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;             // MongoDB braucht immer eine "_id". 
    private String title;         
    private String description;    
    private String destination;   
    private String country;        // wird später von API gefüllt
    private String continent;      // wird später von API gefüllt
    private String startDate;     
    private String endDate;       
    private int rating;        
    private String author;         

    // verschachtelte Objekte
    private Coordinates coordinates;
    private Weather weather;
    private List<PackingItem> packingList = new ArrayList<>();

    // Jackson MUSS immer einen leeren Konstruktor haben, um Objekte bauen zu können
    public TravelEntry() {
    }

// innere Klassen
    public static class Coordinates {
        public double latitude;  
        public double longitude; 

        public Coordinates() {}
    }

    public static class Weather {
        public double temperature;  
        public String condition;   

        public Weather() {}
    }

    public static class PackingItem {
        public String name;      
        public boolean isPacked;  

        public PackingItem() {}
        
        public PackingItem(String name, boolean isPacked) {
            this.name = name;
            this.isPacked = isPacked;
        }
    }

// Getter & Setter 
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

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }

    public Weather getWeather() { return weather; }
    public void setWeather(Weather weather) { this.weather = weather; }

    public List<PackingItem> getPackingList() { return packingList; }
    public void setPackingList(List<PackingItem> packingList) { this.packingList = packingList; }
}
