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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class KillBroadcast extends Broadcast {

    private UUID killer, victim;

    public KillBroadcast(Player killer, Player victim) {
        this(killer, victim, true);
    }

    public KillBroadcast(Player killer, Player victim, boolean notifyAllPlayers) {
        this(killer, victim, 0, 0, notifyAllPlayers);
    }

    public KillBroadcast(Player killer, Player victim, int coinsKiller, int coinsVictim) {
        this(killer, victim, coinsKiller, coinsVictim, true);
    }

    public KillBroadcast(Player killer, Player victim, int coinsKiller, int coinsVictim, boolean notifyAll) {
        super(new BroadcastMessage(
                "game.kill.msg",
                new Object[]{victim.getName(), killer.getName()},
                notifyAll ? getRestOfPlayers(killer, victim) : new Player[]{killer}
        ));

        this.killer = killer.getUniqueId();
        this.victim = victim.getUniqueId();

        if (coinsVictim != 0) {
            addAdditionalMessage(
                    "game.kill.msg.died.coins",
                    new Object[]{killer.getName(), coinsVictim},
                    victim
            );
        } else {
            addAdditionalMessage(
                    "game.kill.msg.died",
                    new Object[]{killer.getName()},
                    victim
            );
        }

        if (coinsKiller != 0) {
            addAdditionalMessage(
                    "game.kill.msg.killed.coins",
                    new Object[]{victim.getName(), coinsKiller},
                    killer
            );
        } else {
            addAdditionalMessage(
                    "game.kill.msg.killed",
                    new Object[]{victim.getName()},
                    killer
            );
        }
    }

    static Player[] getRestOfPlayers(Player... players) {
        Player[] result = new Player[Bukkit.getOnlinePlayers().size()-players.length];

        if (result.length > 0) {
            int i = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!Arrays.asList(players).contains(player)) {
                    result[i] = player;
                    i++;
                }
            }
        }

        return result;
    }

}
