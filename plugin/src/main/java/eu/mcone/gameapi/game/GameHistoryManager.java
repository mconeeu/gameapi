package eu.mcone.gameapi.game;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.game.GameHistory;
import eu.mcone.gameapi.api.game.PlayerHistory;
import eu.mcone.gameapi.api.utils.IDUtils;
import group.onegaming.networkmanager.core.api.database.Database;
import group.onegaming.networkmanager.core.database.MongoConnection;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.UUID;

import static com.mongodb.client.model.Filters.*;

public class GameHistoryManager implements eu.mcone.gameapi.api.game.GameHistoryManager {
    private final MongoDatabase historyDatabase;
    private final MongoCollection<GameHistory> gameHistoryCollection;

    private final Gamemode currentGamemode;
    private GameHistory gameHistory;
    @Getter
    private final String gameID;

    private boolean saved = false;

    public GameHistoryManager() throws NoSuchFieldException, IllegalAccessException {
        Field f = CoreSystem.getInstance().getClass().getDeclaredField("mongoConnection");
        f.setAccessible(true);
        MongoConnection connection = (MongoConnection) f.get(CoreSystem.getInstance());
        f.setAccessible(false);

        this.historyDatabase = connection.getDatabase(Database.GAME);
        this.gameHistoryCollection = historyDatabase.getCollection(GamePlugin.getGamePlugin().getGamemode().toString(), GameHistory.class);

        this.currentGamemode = GameAPIPlugin.getSystem().getGamemode();
        this.gameID = IDUtils.generateID();

        if (GamePlugin.getGamePlugin().hasOption(Option.GAME_HISTORY_HISTORY_MODE)) {
            gameHistory = new GameHistory(gameID, currentGamemode);
        }
    }

    public boolean saveHistory() {
        try {
            if (GamePlugin.getGamePlugin().hasOption(Option.GAME_HISTORY_HISTORY_MODE) && !saved) {
                saved = true;
                gameHistory.setStopped(System.currentTimeMillis() / 1000);
                for (PlayerHistory playerHistory : gameHistory.getPlayers().values()) {
                    if (playerHistory.getLeft() == 0) {
                        playerHistory.setLeft(System.currentTimeMillis() / 1000);
                    }
                }

                gameHistoryCollection.insertOne(gameHistory);
                return true;
            } else {
                throw new IllegalArgumentException("The option GAME_HISTORY_HISTORY_MODE isn't active!");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }

    public long countGameHistories() {
        return countGameHistories(currentGamemode);
    }

    public long countGameHistories(Gamemode gamemode) {
        return historyDatabase.getCollection(gamemode.toString()).countDocuments();
    }

    public long countGameHistoriesForPlayer(UUID uuid) {
        return countGameHistoriesForPlayer(currentGamemode, uuid);
    }

    public long countGameHistoriesForPlayer(Gamemode gamemode, UUID uuid) {
        return historyDatabase.getCollection(gamemode.toString()).countDocuments(exists("players." + uuid));
    }

    public FindIterable<GameHistory> getGameHistoriesForPlayer(UUID uuid, int skip, int limit) {
        return getGameHistoriesForPlayer(currentGamemode, uuid, skip, limit);
    }

    public FindIterable<GameHistory> getGameHistoriesForPlayer(Gamemode gamemode, UUID uuid, int skip, int limit) {
        return historyDatabase.getCollection(gamemode.toString(), GameHistory.class).find(
                and(
                        exists("players." + uuid),
                        eq("gameID", gameID)
                )
        ).skip(skip).limit(limit);
    }

    public FindIterable<GameHistory> getGameHistoriesForPlayer(UUID uuid) {
        return getGameHistoriesForPlayer(currentGamemode, uuid);
    }

    public FindIterable<GameHistory> getGameHistoriesForPlayer(Gamemode gamemode, UUID uuid) {
        return historyDatabase.getCollection(gamemode.toString(), GameHistory.class).find(exists("players." + uuid));
    }

    public GameHistory getGameHistory(String gameID) {
        return getGameHistory(currentGamemode, gameID);
    }

    public GameHistory getGameHistory(Gamemode gamemode, String gameID) {
        return historyDatabase.getCollection(gamemode.toString(), GameHistory.class).find(eq("gameID", gameID)).first();
    }

    public GameHistory getCurrentGameHistory() {
        try {
            if (gameHistory != null) {
                return gameHistory;
            } else {
                throw new IllegalArgumentException("The option GAME_HISTORY_HISTORY_MODE isn't active!");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }
}
