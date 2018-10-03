package eu.mcone.gamesystem.api.game.player;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.manager.team.TeamStage;
import org.bukkit.entity.Player;

import java.util.List;

public interface GamePlayer {

    Team getTeam();

    String getName();

    CorePlayer getCorePlayer();

    Player getBukkitPlayer();

    void setSpectator(final boolean var);

    void setPlaying(final boolean var);

    boolean isSpectator();

    boolean isPlaying();

    void setTeam(final Team team);

    void updateTeamLive(final boolean var);

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

    void addDestroyedBed();

    void addDestroyedBed(int var);

    int getRoundKills();

    int getRoundDeaths();

    int getRoundCoins();

    int getRoundBeds();

    double getRoundKD();
}
