/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.player;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.Stats;
import eu.mcone.gamesystem.api.game.Team;
import org.bukkit.entity.Player;

import java.util.List;

public interface IGamePlayer {

    Team getTeam();

    String getName();

    CorePlayer getCorePlayer();

    Player getBukkitPlayer();

    void setSpectator(final boolean var);

    void setPlaying(final boolean var);

    boolean isSpectator();

    boolean isPlaying();

    void setTeam(final Team team);

    void updateTeamAlive(final boolean var);

    void setTeamSize(final int size);

    void addTeamSize(final int size);

    void removeTeamSize(final int size);

    int getTeamSize();

    int getPlayingSize();

    List<Player> getTeamChat();

    void removeFromGame();

    void removeFromTeamSelection();

    void destroy();

    void addCoins(int coins);

    void addRoundKill();

    void addRoundKill(int var);

    void addRoundDeath();

    void addRoundDeath(int var);

    void addGoal();

    void addGoals(int var);

    int getRoundKills();

    int getRoundDeaths();

    int getRoundCoins();

    int getRoundGoals();

    Stats getStats();

    double getRoundKD();
}
