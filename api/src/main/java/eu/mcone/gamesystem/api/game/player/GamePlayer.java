package eu.mcone.gamesystem.api.game.player;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamesystem.api.game.Team;
import org.bukkit.entity.Player;

import java.util.List;

public interface GamePlayer {

    Team getTeam();

    CorePlayer getCorePlayer();

    Player getBukkitPlayer();

    void setSpectator(boolean var);

    void setPlaying(boolean var);

    boolean isSpectator();

    boolean isPlaying();

    void setTeam(Team team);

    void updateTeamLive(boolean var);

    void setTeamSize(int size);

    void addTeamSize(int size);

    void removeTeamSize(int size);

    int getTeamSize();

    int getPlayingSize();

    List<Player> getTeamChat();

    void removeFromGame();

    void removeFromTeamSelection();

    void destroy();

    void addCoins(int coins);

    void addKill();

    void addKill(int var);

    void addDeath();

    void addDeath(int var);

    void addDestroyedBed();

    void addDestroyedBed(int var);

    int getRoundKills();

    int getRoundDeaths();

    int getRoundCoins();

    int getRoundBeds();

    double getRoundKD();
}
