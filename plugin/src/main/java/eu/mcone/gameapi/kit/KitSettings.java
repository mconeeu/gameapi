package eu.mcone.gameapi.kit;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.kit.Kit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KitSettings {

    private String currentKit;
    private boolean autoBuyKit;

    public Kit calculateCurrentKit() {
        return GamePlugin.getGamePlugin().getKitManager().getKit(currentKit);
    }

}
