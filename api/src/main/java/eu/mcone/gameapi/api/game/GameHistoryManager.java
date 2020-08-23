package eu.mcone.gameapi.api.game;

import com.mongodb.client.FindIterable;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;

import java.util.UUID;

public interface GameHistoryManager {

    String getGameID();

    boolean saveHistory();

    long countGameHistories();

    long countGameHistories(Gamemode gamemode);

    long countGameHistoriesForPlayer(UUID uuid);

    long countGameHistoriesForPlayer(Gamemode gamemode, UUID uuid);

    FindIterable<GameHistory> getGameHistoriesForPlayer(UUID uuid, int skip, int limit);

    FindIterable<GameHistory> getGameHistoriesForPlayer(Gamemode gamemode, UUID uuid, int skip, int limit);

    FindIterable<GameHistory> getGameHistoriesForPlayer(UUID uuid);

    FindIterable<GameHistory> getGameHistoriesForPlayer(Gamemode gamemode, UUID uuid);

    GameHistory getGameHistory(String gameID);

    GameHistory getGameHistory(Gamemode gamemode, String gameID);

    GameHistory getCurrentGameHistory();
}
