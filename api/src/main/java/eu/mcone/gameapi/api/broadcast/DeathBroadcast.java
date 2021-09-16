/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.broadcast;

import eu.mcone.coresystem.api.bukkit.chat.Broadcast;
import eu.mcone.coresystem.api.bukkit.chat.BroadcastMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class DeathBroadcast extends Broadcast {

    private UUID died;

    public DeathBroadcast(Player died) {
        this(died, 0);
    }

    public DeathBroadcast(Player died, int coins) {
        super(new BroadcastMessage(
                "game.death.msg",
                new Object[]{died.getName()},
                KillBroadcast.getRestOfPlayers(died)
        ));

        this.died = died.getUniqueId();

        if (coins != 0) {
            addAdditionalMessage(
                    "game.death.msg.died.coins",
                    new Object[]{coins},
                    died
            );
        } else {
            addAdditionalMessage(
                    "game.death.msg.died",
                    died
            );
        }
    }

}
