package eu.mcone.gameapi.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GamePlayerSettings {

    private boolean enableGadgets = true, enableTraiding = true;

}
