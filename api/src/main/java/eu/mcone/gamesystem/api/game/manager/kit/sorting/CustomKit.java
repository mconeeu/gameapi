package eu.mcone.gamesystem.api.game.manager.kit.sorting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomKit {

    private long lastUpdate;
    private Map<String, Double> customItems;

}
