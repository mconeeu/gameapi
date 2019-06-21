package eu.mcone.gamesystem.api.game.manager.team;

import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.player.IGamePlayer;

public interface ITeamStageHandler {

    TeamStage createTeamStage(final Team team);

    TeamStage getTeamStage(final Team team);

    void removePlayerFromStage(final IGamePlayer gamePlayer);

    void addPlayerToStage(final IGamePlayer gamePlayer);

    void deSpawnAllNPCs();

    void spawnAllNPCs();
}
