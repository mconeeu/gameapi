package eu.mcone.gamesystem.game.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.game.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePlayer implements eu.mcone.gamesystem.api.game.player.GamePlayer {

    private Logger log;

    @Getter
    private CorePlayer corePlayer;
    @Getter
    private Player bukkitPlayer;
    @Getter
    private Team team;
    @Getter
    private String name;
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
        log = GameSystemAPI.getInstance().getLogger();

        try {
            if (GameTemplate.getInstance() != null) {
                this.corePlayer = CoreSystem.getInstance().getCorePlayer(player.getUniqueId());
                this.bukkitPlayer = player;
                this.name = player.getName();
                this.team = Team.ERROR;
                setPlaying(true);
                GameTemplate.getInstance().getPlaying().add(player);
                GameTemplate.getInstance().getGamePlayers().put(player.getUniqueId(), this);
                log.info("Create new GamePlayer `" + name + "`");
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "Exception in GamePlayer.class", e);
        }
    }

    public void setTeam(Team team) {
        this.team = team;
        addTeamSize(1);
        GameTemplate.getInstance().getChats().get(team).add(bukkitPlayer);
        GameTemplate.getInstance().getTeams().put(bukkitPlayer.getUniqueId(), team);
        log.info("Put the player `" + name + "` in team `" + team + "`");
    }

    public void updateTeamLive(boolean var) {
        team.setTeamAlive(var);
    }

    public void setTeamSize(int size) {
        if (team != Team.ERROR) {
            if (size > -1) {
                team.setValue(size);
                log.info("set new size `" + size + "` for team `" + team + "`");
            }
        }
    }

    public void addTeamSize(final int size) {
        if (team != Team.ERROR) {
            log.info("add size `" + size + "` to team `" + team + "`");
            if (team.getValue() == 0) {
                team.setValue(size);
            } else {
                int var = team.getValue() + size;
                team.setValue(var);
            }
        }
    }

    public void removeTeamSize(final int size) {
        if (team != Team.ERROR) {
            if (team.getValue() < 0) {
                log.log(Level.WARNING, "Can not remove `" + size + "` from team, because the team size is smaller than 0");
            } else if (team.getValue() == 1) {
                team.setValue(0);
                log.info("Set team size to 0");
            } else if (team.getValue() >= size) {
                int var = team.getValue() - size;
                team.setValue(var);
                log.info("Remove `" + size + "` from team `"  + team + "`");
            }
        }
    }

    public int getTeamSize() {
        return team.getValue();
    }

    public int getPlayingSize() {
        return GameTemplate.getInstance().getPlaying().size();
    }

    public void addCoins(final int coins) { this.roundCoins = this.roundCoins + coins; }

    public void addRoundKill() {
        this.roundKills = this.roundKills + 1;
        log.info("Add 1 roundKill to player `" + name + "`");
    }

    public void addRoundKill(final int var) {
        this.roundKills = this.roundKills + var;
        log.info("Add `" + var + "` roundKills to player `" + name + "`");
    }

    public void addRoundDeath() {
        this.roundDeaths = this.roundDeaths + 1;
        log.info("Add 1 roundDeath to player `" + name + "`");
    }

    public void addRoundDeath(final int var) {
        this.roundDeaths = this.roundDeaths + var;
        log.info("Add `" + var + "` roundDeaths to player `" + name + "`");
    }

    public void addDestroyedBed() {
        this.roundBeds = this.roundBeds + 1;
        log.info("Add 1 destroyed bed to player `" + name + "`");
    }

    public void addDestroyedBed(final int var) {
        this.roundBeds = this.roundBeds + var;
        log.info("Add `" + var + "` destroyed beds to player `" + name + "`");
    }

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
            log.info("Remove player `" + name + "` from game");
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
            log.info("Remove player `" + name + "` from TeamSelection");
            if (team != Team.ERROR) {
                removeTeamSize(1);
                GameTemplate.getInstance().getTeamManager().getTeamStage().removePlayer(this);
                GameTemplate.getInstance().getChats().get(team).remove(bukkitPlayer);
                GameTemplate.getInstance().getTeams().remove(bukkitPlayer.getUniqueId());
            }
        }
    }

    public void destroy() {
        log.info("Destroy GamePlayer `" + name + "`");
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
