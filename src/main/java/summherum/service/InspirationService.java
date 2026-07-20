package summherum.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

// Zufallsgenerator für nächste Reise
public class InspirationService {
    
    // Katalog nach Kategorien
    private final Map<String, List<String>> ideas = Map.of(
        "warm", List.of("Bali", "Havanna", "Barcelona", "Phuket", "Malaga"),
        "kalt", List.of("Reykjavik", "Oslo", "Tromsø", "Patagonien", "Helsinki"),
        "abenteuer", List.of("Mexiko", "Tokio", "Sydney", "Österreich", "Kapstadt")
    );
    
    private final Random random = new Random();

    // wählt zufällig aus Kategorie
    public String getRandomDestination(String vibe) {
        // Fallback zu abenteuer
        List<String> category = ideas.getOrDefault(vibe.toLowerCase(), ideas.get("abenteuer")); 
        
        int index = random.nextInt(category.size());
        
        return category.get(index);
    }
}