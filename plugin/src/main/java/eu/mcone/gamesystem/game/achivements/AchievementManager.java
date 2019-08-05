/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.achivements;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.game.achivements.Achievement;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.logging.Level;

import static com.mongodb.client.model.Filters.eq;

@Log
public class AchievementManager implements eu.mcone.gamesystem.api.game.achivements.AchievementManager {

    @Getter
    private Gamemode gamemode;
    @Getter
    private TreeMap<String, TreeMap<String, Achievement>> achievements;

    private MongoCollection<Document> achievementsCollection;

    public AchievementManager() {
        try {
            if (GameTemplate.getInstance() != null) {
                if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ACHIEVEMENTS)) {
                    gamemode = GameTemplate.getInstance().getGamemode();
                    achievements = new TreeMap<>(Comparator.naturalOrder());
                    achievementsCollection = CoreSystem.getInstance().getMongoDB().getCollection("game_achievements");
                } else {
                    throw new GameSystemException("The option 'USE_ACHIEVEMENTS' is not activated");
                }
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }
    }

    public void loadAchievements() {
        for (Document document : achievementsCollection.find(eq("gamemode", gamemode.toString()))) {
            if (achievements != null) {
                TreeMap<String, Achievement> achievements = (TreeMap<String, Achievement>) document.get("achievements");
                this.achievements.put(gamemode.toString(), achievements);
            }
        }
    }

    public void registerAchievement(final Achievement achievement) {
        if (achievements.containsKey(gamemode.toString())) {
            achievements.get(gamemode.toString()).put(achievement.getAchievementName(), achievement);
        } else {
            achievements.put(gamemode.toString(), new TreeMap<String, Achievement>() {{
                put(achievement.getAchievementName(), achievement);
            }});
        }

        if (!achievementsCollection.replaceOne(
                eq("gamemode", gamemode.toString()),
                new Document("achievements", this.achievements.get(gamemode.toString())),
                ReplaceOptions.createReplaceOptions
                        (
                                new UpdateOptions().upsert(true)
                        )
        ).wasAcknowledged()) {
            log.log(Level.WARNING, "Error by inserting the new List Item in the database");
        }
    }

    public void registerAchievements(final Achievement... achievements) {
        for (Achievement achievement : achievements) {
            registerAchievement(achievement);
        }
    }

    public Achievement getAchievement(final String achievementName) {
        try {
            if (achievements.containsKey(gamemode.toString())) {
                if (achievements.get(gamemode.toString()).containsKey(achievementName)) {
                    return achievements.get(gamemode.toString()).get(achievementName);
                } else {
                    throw new GameSystemException("Could not found the Achievement with the name " + achievementName);
                }
            } else {
                throw new GameSystemException("Could not found the Achievement with the name " + achievementName);
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Achievement> getAchievements() {
        return new ArrayList<>(achievements.get(gamemode.toString()).values());
    }

    public void openAchievementInventory(final Player player) {
        new AchievementInventory(player);
    }
}
