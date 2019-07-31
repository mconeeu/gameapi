package eu.mcone.gamesystem.api.game.manager.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Setter
@Getter
@AllArgsConstructor
public class MapItem {
    private String displayName;
    private Material material;
    private String[] lore;
}
