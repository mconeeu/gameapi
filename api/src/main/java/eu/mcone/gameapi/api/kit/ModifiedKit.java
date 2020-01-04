package eu.mcone.gameapi.api.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter @Setter
public class ModifiedKit {

    private long lastUpdated;
    private String name;
    private Map<Integer, Integer> customItems;

}
