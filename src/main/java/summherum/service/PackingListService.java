package summherum.service;

import summherum.model.TravelEntry.PackingItem;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Packlisten
public class PackingListService {

    // Map die Liste von Dingen zuweist
    private final Map<String, List<PackingItem>> templates = new HashMap<>();

    public PackingListService() {
        // Strandurlaub
        templates.put("Strand", List.of(
            new PackingItem("Sonnencreme", false),
            new PackingItem("Badehose/Bikini", false),
            new PackingItem("Sonnenbrille", false),
            new PackingItem("Handtuch", false)
        ));

        // Städtetrip
        templates.put("Stadt", List.of(
            new PackingItem("Bequeme Schuhe", false),
            new PackingItem("Powerbank", false),
            new PackingItem("Regenschirm", false),
            new PackingItem("Kamera", false)
        ));
        
        // Winter & Berge
        templates.put("Winter", List.of(
            new PackingItem("Dicke Jacke", false),
            new PackingItem("Handschuhe", false),
            new PackingItem("Wanderschuhe", false),
            new PackingItem("Thermoskanne", false)
        ));
    }

    // für Dropdown-Menü
    public Map<String, List<PackingItem>> getAllTemplates() {
        return templates;
    }
}