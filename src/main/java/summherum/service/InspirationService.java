package summherum.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Der Zufallsgenerator für die nächste Reise.
 * Wählt basierend auf einem "Vibe" (Kalt, Warm, Abenteuer) 
 * ein zufälliges Reiseziel aus.
 */
public class InspirationService {
    
    // Unser Katalog an Zielen, sortiert nach Kategorien
    private final Map<String, List<String>> ideas = Map.of(
        "warm", List.of("Bali", "Havanna", "Barcelona", "Phuket", "Malaga"),
        "kalt", List.of("Reykjavik", "Oslo", "Tromsø", "Patagonien", "Helsinki"),
        "abenteuer", List.of("Mexiko", "Tokio", "Sydney", "Österreich", "Kapstadt")
    );
    
    private final Random random = new Random();

    /**
     * Sucht ein zufälliges Ziel aus der gewählten Kategorie.
     */
    public String getRandomDestination(String vibe) {
        // Wenn das Frontend Quatsch schickt, nehmen wir als Fallback "abenteuer"
        List<String> category = ideas.getOrDefault(vibe.toLowerCase(), ideas.get("abenteuer")); 
        
        // Zufällige Zahl generieren, die so groß ist wie die Liste
        int index = random.nextInt(category.size());
        
        return category.get(index);
    }
}