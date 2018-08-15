/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.manager;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.core.player.GlobalCorePlayer;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.ecxeptions.Team.TeamNotFoundEcxeption;
import eu.mcone.gamesystem.api.manager.team.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.*;

public abstract class TeamManager {

    @Getter
    private List<Player> playing = new ArrayList<>();
    @Getter
    private Map<UUID, String> teams = new HashMap<>();
    @Getter
    private Map<Team, List<Player>> chats = new HashMap<>();

    @Getter
    private List<Player> spectators = new ArrayList<>();

    @Getter
    private List<Player> red = new ArrayList<>();
    @Getter
    private List<Player> blue = new ArrayList<>();
    @Getter
    private List<Player> violet = new ArrayList<>();
    @Getter
    private List<Player> yellow = new ArrayList<>();
    @Getter
    private List<Player> green = new ArrayList<>();
    @Getter
    private List<Player> gray = new ArrayList<>();
    @Getter
    private List<Player> white = new ArrayList<>();
    @Getter
    private List<Player> black = new ArrayList<>();

    @Getter
    private int numberOfTeams;
    @Getter
    private int playersPerTeam;
    @Getter
    @Setter
    private int roundKills;
    @Getter
    @Setter
    private Team winner_team;

    public TeamManager(final int numberOfTeams, final int playersPerTeam) {
        this.numberOfTeams = numberOfTeams;
        this.playersPerTeam = playersPerTeam;
    }

    public void updateBedAlive(final Team team, final boolean value) {
        team.setBedAlive(value);
    }

    public int getTeamsize(final Team team) {
        return team.getValue();
    }

    public int getPlayingSize() {
        return playing.size();
    }

    public void setupBed() {
        for (Team team : Team.values()) {
            if (team.getValue() > 0) {
                team.setBedAlive(true);
            }
        }
    }

    public void setSize(final Team team, final int size) {
        GameSystemAPI.getInstance().sendConsoleMessage("set new size(" + size + ") for team " + team);
        if (size < -1) {
            team.setValue(size);
        }
    }

    public void addSize(final Team team, final int size) {
        GameSystemAPI.getInstance().sendConsoleMessage("add size(" + size + ") to team " + team);
        if (team.getValue() == 0) {
            team.setValue(size);
        } else {
            int var = team.getValue() + size;
            team.setValue(var);
        }
    }

    public void removeSize(final Team team, final int size) {
        if (team.getValue() == 0) {
            GameSystemAPI.getInstance().sendConsoleMessage("§c[TeamManager] Error team size is null");
        } else if (team.getValue() == 1) {
            team.setValue(0);
        } else if (team.getValue() < size) {
            int var = team.getValue() - size;
            team.setValue(var);
        } else {
            GameSystemAPI.getInstance().sendConsoleMessage("§cError by subtraction forms a negative number \n" +
                    "team name: " + team.getString() + "\n" +
                    "team value: " + team.getValue() + "\n" +
                    "subtract int: " + size);
        }
    }

    public Team getTeam(final UUID uuid) {
        try {
            if (teams.get(uuid) != null) {
                if (teams.get(uuid).equalsIgnoreCase("red")) {
                    return Team.RED;
                } else if (teams.get(uuid).equalsIgnoreCase("blue")) {
                    return Team.BLUE;
                } else if (teams.get(uuid).equalsIgnoreCase("violet")) {
                    return Team.VIOLET;
                } else if (teams.get(uuid).equalsIgnoreCase("yellow")) {
                    return Team.YELLOW;
                } else if (teams.get(uuid).equalsIgnoreCase("green")) {
                    return Team.GREEN;
                } else if (teams.get(uuid).equalsIgnoreCase("gray")) {
                    return Team.GRAY;
                } else if (teams.get(uuid).equalsIgnoreCase("white")) {
                    return Team.WHITE;
                } else if (teams.get(uuid).equalsIgnoreCase("black")) {
                    return Team.BLACK;
                } else {
                    throw new TeamNotFoundEcxeption("The team could not be found (Player-UUID: `" + uuid + "`)");
                }
            } else {
                return Team.ERROR;
            }
        } catch (TeamNotFoundEcxeption e) {
            e.printStackTrace();
        }
        return Team.ERROR;
    }

    public Team getTeam(final String string) {
        try {
            if (string.equalsIgnoreCase(Team.RED.getString())) {
                return Team.RED;
            } else if (string.equalsIgnoreCase(Team.BLUE.getString())) {
                return Team.BLUE;
            } else if (string.equalsIgnoreCase(Team.VIOLET.getString())) {
                return Team.VIOLET;
            } else if (string.equalsIgnoreCase(Team.YELLOW.getString())) {
                return Team.YELLOW;
            } else if (string.equalsIgnoreCase(Team.GREEN.getString())) {
                return Team.GREEN;
            } else if (string.equalsIgnoreCase(Team.GRAY.getString())) {
                return Team.GRAY;
            } else if (string.equalsIgnoreCase(Team.WHITE.getString())) {
                return Team.WHITE;
            } else if (string.equalsIgnoreCase(Team.BLACK.getString())) {
                return Team.BLACK;
            } else {
                throw new TeamNotFoundEcxeption("The team with specified string `" + string + "` could not be found.");
            }
        } catch (TeamNotFoundEcxeption e) {
            e.printStackTrace();
        }
        return Team.ERROR;
    }

    public void addChat(final Team team, final Player player) {
        try {
            if (team.equals(Team.RED)) {
                red.add(player);
            } else if (team.equals(Team.BLUE)) {
                blue.add(player);
            } else if (team.equals(Team.VIOLET)) {
                violet.add(player);
            } else if (team.equals(Team.YELLOW)) {
                yellow.add(player);
            } else if (team.equals(Team.GREEN)) {
                green.add(player);
            } else if (team.equals(Team.GRAY)) {
                gray.add(player);
            } else if (team.equals(Team.WHITE)) {
                white.add(player);
            } else if (team.equals(Team.BLACK)) {
                black.add(player);
            } else {
                throw new TeamNotFoundEcxeption("The team with the key `" + team.getString() + "` could not be found.");
            }
        } catch (TeamNotFoundEcxeption e) {
            e.printStackTrace();
        }
    }

    public void reloadChat() {
        for (Team team : Team.values()) {
            if (team.equals(Team.RED)) {
                chats.put(team, red);
            } else if (team.equals(Team.BLUE)) {
                chats.put(team, blue);
            } else if (team.equals(Team.VIOLET)) {
                chats.put(team, violet);
            } else if (team.equals(Team.YELLOW)) {
                chats.put(team, yellow);
            } else if (team.equals(Team.GREEN)) {
                chats.put(team, green);
            } else if (team.equals(Team.GRAY)) {
                chats.put(team, gray);
            } else if (team.equals(Team.WHITE)) {
                chats.put(team, white);
            } else if (team.equals(Team.BLACK)) {
                chats.put(team, black);
            }
        }
    }

    public List<Player> getChat(Team team) {
        return chats.get(team);
    }

    public void setupTeam() {
        GameSystemAPI.getInstance().sendConsoleMessage("check if all players have a team");
        for (Player p : playing) {
            if (getTeam(p.getUniqueId()) == Team.ERROR) {
                int i = 1;
                for (Team team : Team.values()) {
                    if (i <= numberOfTeams) {
                        if (!teams.containsKey(p.getUniqueId())) {
                            if (team.getValue() == 0 || team.getValue() < playersPerTeam) {
                                addSize(team, 1);
                                teams.put(p.getUniqueId(), team.getString());
                                addChat(team, p);

                                GameSystemAPI.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.join").replace("%team%", team.getPrefix()));
                                GameSystemAPI.getInstance().sendConsoleMessage("set player " + p.getName() + " to team " + team.getString());
                            }
                        }

                        i++;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public void cleanSystem(final Player player) {
        GameSystemAPI.getInstance().sendConsoleMessage("start cleanup for player " + player.getName());
        if (teams.containsKey(player.getUniqueId())) {
            if (getTeam(player.getUniqueId()) != Team.ERROR) {
                final Team PlayerTeam = getTeam(player.getUniqueId());

                removeSize(PlayerTeam, 1);
                chats.get(PlayerTeam).remove(player);
                teams.remove(player.getUniqueId());
                playing.remove(player);
                reloadChat();
            } else {
                playing.remove(player);
            }
        } else {
            playing.remove(player);
        }
    }

    public void chekWinntection() {
        /* Winntection Rot, Blau, Gelb, Lila for Player == null */
        if (getTeamsize(Team.RED) > 0
                && getTeamsize(Team.BLUE) == 0
                && getTeamsize(Team.VIOLET) == 0
                && getTeamsize(Team.YELLOW) == 0
                && getTeamsize(Team.GREEN) == 0
                && getTeamsize(Team.GRAY) == 0
                && getTeamsize(Team.WHITE) == 0
                && getTeamsize(Team.BLACK) == 0) {

            initWin(Team.RED.getString());
        } else if (getTeamsize(Team.BLUE) > 0
                && getTeamsize(Team.RED) == 0
                && getTeamsize(Team.VIOLET) == 0
                && getTeamsize(Team.YELLOW) == 0
                && getTeamsize(Team.GREEN) == 0
                && getTeamsize(Team.GRAY) == 0
                && getTeamsize(Team.WHITE) == 0
                && getTeamsize(Team.BLACK) == 0) {

            initWin(Team.BLUE.getString());
        } else if (getTeamsize(Team.VIOLET) > 0
                && getTeamsize(Team.RED) == 0
                && getTeamsize(Team.BLUE) == 0
                && getTeamsize(Team.YELLOW) == 0
                && getTeamsize(Team.GREEN) == 0
                && getTeamsize(Team.GRAY) == 0
                && getTeamsize(Team.WHITE) == 0
                && getTeamsize(Team.BLACK) == 0) {

            initWin(Team.VIOLET.getString());
        } else if (getTeamsize(Team.YELLOW) > 0
                && getTeamsize(Team.RED) == 0
                && getTeamsize(Team.BLUE) == 0
                && getTeamsize(Team.VIOLET) == 0
                && getTeamsize(Team.GREEN) == 0
                && getTeamsize(Team.GRAY) == 0
                && getTeamsize(Team.WHITE) == 0
                && getTeamsize(Team.BLACK) == 0) {

            initWin(Team.YELLOW.getString());
        } else if (getTeamsize(Team.GREEN) > 0
                && getTeamsize(Team.RED) == 0
                && getTeamsize(Team.BLUE) == 0
                && getTeamsize(Team.VIOLET) == 0
                && getTeamsize(Team.YELLOW) == 0
                && getTeamsize(Team.GRAY) == 0
                && getTeamsize(Team.WHITE) == 0
                && getTeamsize(Team.BLACK) == 0) {

            initWin(Team.GREEN.getString());
        } else if (getTeamsize(Team.GRAY) > 0
                && getTeamsize(Team.RED) == 0
                && getTeamsize(Team.BLUE) == 0
                && getTeamsize(Team.VIOLET) == 0
                && getTeamsize(Team.YELLOW) == 0
                && getTeamsize(Team.GREEN) == 0
                && getTeamsize(Team.WHITE) == 0
                && getTeamsize(Team.BLACK) == 0) {

            initWin(Team.GRAY.getString());
        } else if (getTeamsize(Team.WHITE) > 0
                && getTeamsize(Team.RED) == 0
                && getTeamsize(Team.BLUE) == 0
                && getTeamsize(Team.VIOLET) == 0
                && getTeamsize(Team.YELLOW) == 0
                && getTeamsize(Team.GREEN) == 0
                && getTeamsize(Team.GRAY) == 0
                && getTeamsize(Team.BLACK) == 0) {

            initWin(Team.WHITE.getString());
        } else if (getTeamsize(Team.BLACK) > 0
                && getTeamsize(Team.RED) == 0
                && getTeamsize(Team.BLUE) == 0
                && getTeamsize(Team.VIOLET) == 0
                && getTeamsize(Team.YELLOW) == 0
                && getTeamsize(Team.GREEN) == 0
                && getTeamsize(Team.GRAY) == 0
                && getTeamsize(Team.WHITE) == 0) {

            initWin(Team.BLACK.getString());

        }
    }

    public String getKillMessage(final GlobalCorePlayer globalCorePlayer, final Player killer, final Player victim) {
        if (killer == null) {
            return CoreSystem.getInstance().getTranslationManager().get("game.death.normal", globalCorePlayer)
                    .replace("%victim_team%", getTeam(victim.getUniqueId()).getColor())
                    .replace("%victim%", victim.getName());
        } else {
            return CoreSystem.getInstance().getTranslationManager().get("game.death.bykiller", globalCorePlayer)
                    .replace("%victim_team%", getTeam(victim.getUniqueId()).getColor())
                    .replace("%victim%", victim.getName())
                    .replace("%killer_team%", getTeam(killer.getUniqueId()).getColor())
                    .replace("%killer%", killer.getName());
        }
    }

    public abstract void onPlayerDeath(final PlayerDeathEvent e);

    public abstract void onPlayerRespawn(final PlayerRespawnEvent e);

    public abstract void initWin(final String team);
}
