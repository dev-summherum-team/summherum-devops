package summherum.service;

import summherum.model.TravelEntry.PackingItem;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Dieser Service stellt unsere vorgefertigten Packlisten bereit.
 * Das Frontend kann diese Vorlagen abrufen und dem User als 
 * Checkboxen anzeigen.
 */
public class PackingListService {

    // Eine "Map" (Wörterbuch), die einem Namen (z.B. "Strand") eine Liste von Dingen zuweist
    private final Map<String, List<PackingItem>> templates = new HashMap<>();

    public PackingListService() {
        // Vorlage 1: Strandurlaub
        templates.put("Strand", List.of(
            new PackingItem("Sonnencreme", false),
            new PackingItem("Badehose/Bikini", false),
            new PackingItem("Sonnenbrille", false),
            new PackingItem("Handtuch", false)
        ));

        // Vorlage 2: Städtetrip
        templates.put("Stadt", List.of(
            new PackingItem("Bequeme Schuhe", false),
            new PackingItem("Powerbank", false),
            new PackingItem("Regenschirm", false),
            new PackingItem("Kamera", false)
        ));
        
        // Vorlage 3: Winter & Berge
        templates.put("Winter", List.of(
            new PackingItem("Dicke Jacke", false),
            new PackingItem("Handschuhe", false),
            new PackingItem("Wanderschuhe", false),
            new PackingItem("Thermoskanne", false)
        ));
    }

    /**
     * Gibt alle verfügbaren Vorlagen zurück, damit das Frontend daraus 
     * ein Dropdown-Menü bauen kann.
     */
    public Map<String, List<PackingItem>> getAllTemplates() {
        return templates;
    }
}