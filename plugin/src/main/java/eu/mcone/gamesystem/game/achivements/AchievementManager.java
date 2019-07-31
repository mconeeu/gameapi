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
import eu.mcone.gamesystem.api.game.achivements.IAchievementManager;
import eu.mcone.gamesystem.api.game.achivements.SolvedAchievement;
import eu.mcone.gamesystem.api.game.event.GameAchievementEvent;
import eu.mcone.gamesystem.api.game.player.IGamePlayer;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

import static com.mongodb.client.model.Filters.eq;

@Log
public class AchievementManager implements IAchievementManager {

    @Getter
    private Gamemode gamemode;
    @Getter
    private TreeMap<String, TreeMap<String, Achievement>> achievements;
    @Getter
    private Map<UUID, TreeMap<String, SolvedAchievement>> solvedAchievements;

    private MongoCollection<Document> achievementsCollection;
    private MongoCollection<Document> solvedAchievementsCollection;

    public AchievementManager() {
        try {
            if (GameTemplate.getInstance() != null) {
                if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ACHIEVEMENTS)) {
                    gamemode = GameTemplate.getInstance().getGamemode();
                    achievements = new TreeMap<>(Comparator.naturalOrder());
                    solvedAchievements = new HashMap<>();
                    achievementsCollection = CoreSystem.getInstance().getMongoDB().getCollection("game_achievements");
                    solvedAchievementsCollection = CoreSystem.getInstance().getMongoDB().getCollection("game_solved_achievements");
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

    public void loadSolvedAchievement(UUID uuid) {
        Document document = solvedAchievementsCollection.find(eq("uuid", uuid)).first();
        if (document != null) {
            this.solvedAchievements.put(uuid, (TreeMap<String, SolvedAchievement>) document.get("solvedAchievements"));
        }
    }

    public void loadSolvedAchievements() {
        for (Document document : solvedAchievementsCollection.find()) {
            this.solvedAchievements.put(UUID.fromString(document.getString("document")), (TreeMap<String, SolvedAchievement>) document.get("solvedAchievements"));
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

    public SolvedAchievement solveAchievement(final UUID uuid, final String achievementName) {
        Achievement achievement = getAchievement(achievementName);

        if (achievement != null) {
            SolvedAchievement solvedAchievement = new SolvedAchievement(System.currentTimeMillis() / 1000, achievement);
            IGamePlayer gamePlayer = GameTemplate.getInstance().getGamePlayer(uuid);

            if (solvedAchievements.containsKey(uuid)) {
                if (hasAchievement(uuid, achievementName)) {
                    return solvedAchievement;
                }
            } else {
                solvedAchievements.put(uuid, new TreeMap<String, SolvedAchievement>() {{
                    put(achievementName, solvedAchievement);
                }});
            }

            if (solvedAchievementsCollection.replaceOne(
                    eq("uuid", uuid),
                    new Document("solvedAchievements", solvedAchievements.get(uuid)),
                    ReplaceOptions.createReplaceOptions
                            (
                                    new UpdateOptions().upsert(true)
                            )
            ).wasAcknowledged()) {
                gamePlayer.addCoins(achievement.getReward());
                GameTemplate.getInstance().getMessager().sendSimple(gamePlayer.getBukkitPlayer(),
                        "§8§kasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfsadfsadfasdfasdf\n" +
                                "§aDu hast das folgendes Achievement freigeschaltet!\n" +
                                "\n" +
                                "§7Achievement: " + gamemode.getColor() + "§o" + achievementName + "\n" +
                                "§7Beschreibung: " + gamemode.getColor() + "§o" + achievement.getDescription() + "\n" +
                                "§7Coins:" + gamemode.getColor() + "§o" + achievement.getReward() + "\n" +
                                "§8§kasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfsadfsadfasdfasdf"
                );

                //Call Event
                Bukkit.getPluginManager().callEvent(new GameAchievementEvent(uuid, solvedAchievement));
            } else {
                log.log(Level.WARNING, "Error by replacing the SolvedAchievements object in the database, UUID: " + uuid);
                GameTemplate.getInstance().getMessager().send(gamePlayer.getBukkitPlayer(), "§cDas Achievement " + achievementName + " konnte für dich nicht freigeschaltet werden, melde dies bitte einem MCONE Teammitglied!");
            }
        }

        return null;
    }

    public void solveAchievements(final UUID uuid, final String... solvedAchievementName) {
        for (String achievementName : solvedAchievementName) {
            solveAchievement(uuid, achievementName);
        }
    }

    public void removeSolvedAchievement(final UUID uuid, final String achievementName) {
        if (solvedAchievements.containsKey(uuid)) {
            if (solvedAchievements.get(uuid).containsKey(achievementName)) {
                solvedAchievements.get(uuid).remove(achievementName);

                if (!(solvedAchievementsCollection.replaceOne(
                        eq("uuid", uuid),
                        new Document("solvedAchievements", solvedAchievements.get(uuid)),
                        ReplaceOptions.createReplaceOptions
                                (
                                        new UpdateOptions().upsert(true)
                                )
                ).wasAcknowledged())) {
                    log.log(Level.WARNING, "Error by replacing the SolvedAchievements object in the database, UUID: " + uuid);
                }
            }
        }
    }

    public void removeSolvedAchievements(final UUID uuid, final String... solvedAchievementNames) {
        for (String solvedAchievementName : solvedAchievementNames) {
            removeSolvedAchievement(uuid, solvedAchievementName);
        }
    }

    public boolean hasAchievement(final UUID uuid, final String achievementName) {
        if (solvedAchievements.containsKey(uuid)) {
            return solvedAchievements.get(uuid).containsKey(achievementName);
        } else {
            return false;
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

    public ArrayList<SolvedAchievement> getSolvedAchievements(final UUID uuid) {
        try {
            if (solvedAchievements.containsKey(uuid)) {
                return new ArrayList<>(solvedAchievements.get(uuid).values());
            } else {
                throw new GameSystemException("The player with the UUID " + uuid + " could not be found.");
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
