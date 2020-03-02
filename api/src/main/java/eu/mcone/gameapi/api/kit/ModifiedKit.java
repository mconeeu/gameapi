package eu.mcone.gameapi.api.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class ModifiedKit {

    private transient Map<Integer, Integer> items;

    private long lastUpdated;
    private String name;
    private Map<String, Integer> customItems;

    public ModifiedKit(long lastUpdated, String name, Map<Integer, Integer> customItems) {
        this.lastUpdated = lastUpdated;
        this.name = name;

        Map<String, Integer> items = new HashMap<>();
        customItems.forEach((k, v) -> items.put(String.valueOf(k), v));
        this.customItems = items;
    }

    public Map<Integer, Integer> calculateCustomItems() {
        if (items == null) {
            items = new HashMap<>();
            customItems.forEach((k, v) -> items.put(Integer.parseInt(k), v));
        }

        return items;
    }

}
