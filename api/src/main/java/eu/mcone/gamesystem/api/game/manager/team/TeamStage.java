package eu.mcone.gamesystem.api.game.manager.team;

import eu.mcone.gamesystem.api.game.player.GamePlayer;

public interface TeamStage {

    void setPlayer(final GamePlayer gamePlayer);

    void removePlayer(final GamePlayer gamePlayer);

}
