/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.team;

import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Team {

    String getName();

    String getLabel();

    int getSize();

    void setSize(int size);

    int getPriority();

    boolean isAlive();

    ChatColor getColor();

    ItemStack getItem();

    String getSpawnLocation();

    void setSpawnLocation(String spawnLocation);

    String getNpcLocation();

    void setNpcLocation(String npcLocation);

    String getRespawnBlockLocation();

    void setRespawnBlockLocation(String respawnBlockLocation);

    List<GamePlayer> getPlayers();

    int getKills();

    int getDeaths();

    int getGoals();
}
