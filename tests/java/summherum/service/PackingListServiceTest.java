package summherum.service;

import org.junit.jupiter.api.Test;
import summherum.model.TravelEntry.PackingItem;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PackingListServiceTest {

    @Test
    public void shouldReturnAllPackingTemplates() {
        PackingListService service = new PackingListService();

        Map<String, List<PackingItem>> templates = service.getAllTemplates();

        assertNotNull(templates);
        assertEquals(3, templates.size());
        assertTrue(templates.containsKey("Strand"));
        assertTrue(templates.containsKey("Stadt"));
        assertTrue(templates.containsKey("Winter"));
    }

    @Test
    public void shouldContainBeachPackingItems() {
        PackingListService service = new PackingListService();

        List<PackingItem> beachItems = service.getAllTemplates().get("Strand");

        assertNotNull(beachItems);
        assertEquals(4, beachItems.size());

        assertTrue(beachItems.stream().anyMatch(item -> item.name.equals("Sonnencreme")));
        assertTrue(beachItems.stream().anyMatch(item -> item.name.equals("Badehose/Bikini")));
        assertTrue(beachItems.stream().anyMatch(item -> item.name.equals("Sonnenbrille")));
        assertTrue(beachItems.stream().anyMatch(item -> item.name.equals("Handtuch")));
    }

    @Test
    public void shouldContainCityPackingItems() {
        PackingListService service = new PackingListService();

        List<PackingItem> cityItems = service.getAllTemplates().get("Stadt");

        assertNotNull(cityItems);
        assertEquals(4, cityItems.size());

        assertTrue(cityItems.stream().anyMatch(item -> item.name.equals("Bequeme Schuhe")));
        assertTrue(cityItems.stream().anyMatch(item -> item.name.equals("Powerbank")));
        assertTrue(cityItems.stream().anyMatch(item -> item.name.equals("Regenschirm")));
        assertTrue(cityItems.stream().anyMatch(item -> item.name.equals("Kamera")));
    }

    @Test
    public void shouldContainWinterPackingItems() {
        PackingListService service = new PackingListService();

        List<PackingItem> winterItems = service.getAllTemplates().get("Winter");

        assertNotNull(winterItems);
        assertEquals(4, winterItems.size());

        assertTrue(winterItems.stream().anyMatch(item -> item.name.equals("Dicke Jacke")));
        assertTrue(winterItems.stream().anyMatch(item -> item.name.equals("Handschuhe")));
        assertTrue(winterItems.stream().anyMatch(item -> item.name.equals("Wanderschuhe")));
        assertTrue(winterItems.stream().anyMatch(item -> item.name.equals("Thermoskanne")));
    }

    
}