/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

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
