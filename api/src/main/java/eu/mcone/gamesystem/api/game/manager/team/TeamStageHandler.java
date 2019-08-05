package eu.mcone.gamesystem.api.game.manager.team;

import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.player.GamePlayer;

public interface TeamStageHandler {

    TeamStage createTeamStage(final Team team);

    TeamStage getTeamStage(final Team team);

    void removePlayerFromStage(final GamePlayer gamePlayer);

    void addPlayerToStage(final GamePlayer gamePlayer);

    void deSpawnAllNPCs();

    void spawnAllNPCs();
}
