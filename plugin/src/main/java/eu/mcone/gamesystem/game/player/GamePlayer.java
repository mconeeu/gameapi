package eu.mcone.gamesystem.game.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.gamesystem.GameSystemException;
import eu.mcone.gamesystem.api.game.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

public class GamePlayer implements eu.mcone.gamesystem.api.game.player.GamePlayer {

    @Getter
    private CorePlayer corePlayer;
    @Getter
    private Player bukkitPlayer;
    @Getter
    private Team team;
    @Getter
    @Setter
    boolean spectator;
    @Getter
    @Setter
    boolean playing;
    @Getter
    private int roundCoins;
    @Getter
    private int roundKills;
    @Getter
    private int roundDeaths;
    @Getter
    private int roundBeds;

    public GamePlayer(Player player) {
        try {
            if (GameTemplate.getInstance() != null) {
                this.corePlayer = CoreSystem.getInstance().getCorePlayer(player.getUniqueId());
                this.bukkitPlayer = player;
                this.team = Team.ERROR;
                setPlaying(true);
                GameTemplate.getInstance().getPlaying().add(player);
                GameTemplate.getInstance().getGamePlayers().put(player.getUniqueId(), this);
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }
    }

    public void setTeam(Team team) {
        this.team = team;
        addTeamSize(1);
        GameTemplate.getInstance().getChats().get(team).add(bukkitPlayer);
        GameTemplate.getInstance().getTeams().put(bukkitPlayer.getUniqueId(), team);
    }

    public void updateTeamLive(boolean var) {
        team.setTeamAlive(var);
    }

    public void setTeamSize(int size) {
        if (team != Team.ERROR) {
            GameSystemAPI.getInstance().sendConsoleMessage("set new size(" + size + ") for team " + team);
            if (size < -1) {
                team.setValue(size);
            }
        }
    }

    public void addTeamSize(int size) {
        if (team != Team.ERROR) {
            GameSystemAPI.getInstance().sendConsoleMessage("add size(" + size + ") to team " + team);
            if (team.getValue() == 0) {
                team.setValue(size);
            } else {
                int var = team.getValue() + size;
                team.setValue(var);
            }
        }
    }

    public void removeTeamSize(int size) {
        if (team != Team.ERROR) {
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
    }

    public Team getTeam() { return team; }

    public int getTeamSize() {
        return team.getValue();
    }

    public int getPlayingSize() {
        return GameTemplate.getInstance().getPlaying().size();
    }

    public void addCoins(int coins) { this.roundCoins = this.roundCoins + coins; }

    public void addKill() {
        this.roundKills = this.roundKills + 1;
    }

    public void addKill(int var) {
        this.roundKills = this.roundKills + var;
    }

    public void addDeath() {
        this.roundDeaths = this.roundDeaths + 1;
    }

    public void addDeath(int var) {
        this.roundDeaths = this.roundDeaths + var;
    }

    public void addDestroyedBed() { this.roundBeds = this.roundBeds + 1;}

    public void addDestroyedBed(int var) { this.roundBeds = this.roundBeds + var;}

    public double getRoundKD() {
        double KD = (double)roundDeaths / roundKills;

        if (KD <= 0.0) {
            return 0;
        } else {
            return KD;
        }
    }

    public List<Player> getTeamChat() {
        return GameTemplate.getInstance().getChats().get(team);
    }

    public void removeFromGame() {
        if (GameTemplate.getInstance().getPlaying().contains(bukkitPlayer)) {
            if (team != Team.ERROR) {
                removeTeamSize(1);
                GameTemplate.getInstance().getChats().get(team).remove(bukkitPlayer);
                GameTemplate.getInstance().getTeams().remove(bukkitPlayer.getUniqueId());
                GameTemplate.getInstance().getPlaying().remove(bukkitPlayer);
            } else {
                GameTemplate.getInstance().getPlaying().remove(bukkitPlayer);
            }
        }
    }

    public void removeFromTeamSelection() {
        if (GameTemplate.getInstance().getPlaying().contains(bukkitPlayer)) {
            if (team != Team.ERROR) {
                removeTeamSize(1);
                GameTemplate.getInstance().getChats().get(team).remove(bukkitPlayer);
                GameTemplate.getInstance().getTeams().remove(bukkitPlayer.getUniqueId());
            }
        }
    }

    public void destroy() {
        if (GameTemplate.getInstance().getPlaying().contains(bukkitPlayer)) {
            if (team == Team.ERROR) {
                removeTeamSize(1);
                GameTemplate.getInstance().getChats().get(team).remove(bukkitPlayer);
                GameTemplate.getInstance().getTeams().remove(bukkitPlayer.getUniqueId());
                GameTemplate.getInstance().getPlaying().remove(bukkitPlayer);
                GameTemplate.getInstance().getGamePlayers().remove(bukkitPlayer.getUniqueId());
            } else {
                GameTemplate.getInstance().getGamePlayers().remove(bukkitPlayer.getUniqueId());
                GameTemplate.getInstance().getPlaying().remove(bukkitPlayer);
            }
        } else {
            GameTemplate.getInstance().getGamePlayers().remove(bukkitPlayer.getUniqueId());
        }
    }
}
