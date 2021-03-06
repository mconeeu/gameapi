/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.achievement;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.event.achievement.AchievementGetEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.inventory.AchievementInventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class GameAchievementManager implements AchievementManager {

    private static final MongoCollection<GamemodeAchievement> ACHIEVEMENT_COLLECTION = CoreSystem.getInstance().getMongoDB().getCollection(
            "gameapi_achievements",
            GamemodeAchievement.class
    );

    private final boolean loadAllAchievements;

    @Getter
    private final GamePlugin plugin;
    private final Map<Gamemode, List<Achievement>> achievements;

    public GameAchievementManager(GamePlugin plugin, GameAPIPlugin system) {
        this.loadAllAchievements = GamePlugin.getGamePlugin().hasOption(Option.ACHIEVEMENT_MANAGER_LOAD_ALL_ACHIEVEMENTS);
        this.plugin = plugin;
        this.achievements = new HashMap<>();

        system.sendConsoleMessage("§aLoading AchievementManager...");
        reload();
    }

    @Override
    public void registerAchievements(Achievement... achievements) {
        List<Achievement> registered = Arrays.asList(achievements);

        if (!this.achievements.containsKey(plugin.getGamemode()) || !this.achievements.get(plugin.getGamemode()).equals(registered)) {
            ACHIEVEMENT_COLLECTION.replaceOne(
                    eq("gamemode", plugin.getGamemode().toString()),
                    new GamemodeAchievement(plugin.getGamemode(), registered),
                    ReplaceOptions.createReplaceOptions(new UpdateOptions().upsert(true))
            );
            this.achievements.put(plugin.getGamemode(), registered);
        }
    }

    @Override
    public void reload() {
        if (loadAllAchievements) {
            achievements.clear();

            for (GamemodeAchievement achievement : ACHIEVEMENT_COLLECTION.find()) {
                this.achievements.put(achievement.getGamemode(), achievement.getAchievements());
            }
        }
    }

    @Override
    public Achievement getAchievement(String name) {
        return getAchievement(plugin.getGamemode(), name);
    }

    @Override
    public Achievement getAchievement(Gamemode gamemode, String name) {
        for (Achievement achievement : achievements.getOrDefault(gamemode, Collections.emptyList())) {
            if (achievement.getName().equals(name)) {
                return achievement;
            }
        }

        if (achievements.containsKey(gamemode)) {
            throw new IllegalArgumentException("Could not get achievement with name " + name + " from gamemode " + gamemode + ". Gamemode is loaded but achievement does not exist!");
        } else {
            return null;
        }
    }

    public List<Achievement> getAchievements(Gamemode gamemode) {
        if (achievements.containsKey(gamemode)) {
            return achievements.get(gamemode);
        } else {
            GamemodeAchievement achievements = ACHIEVEMENT_COLLECTION.find(eq("gamemode", gamemode)).first();
            if (achievements != null) {
                return achievements.getAchievements();
            } else {
                return new ArrayList<>();
            }
        }
    }

    @Override
    public boolean setAchievement(GamePlayer player, Achievement achievement) {
        AchievementGetEvent event = new AchievementGetEvent(player, achievement);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            ChatColor gamemodeColor = plugin.getPluginColor();
            player.getCorePlayer().addCoins(achievement.getRewardCoins());
            plugin.getMessenger().send(player.bukkit(), "§aDu hast das Achievement " + gamemodeColor.toString() + achievement.getName() + " erfolgreich §afreigeschaltet! ");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void openAchievementInventory(Player p) {
        new AchievementInventory(p, this);
    }

    @Override
    public void openAchievementInventory(Player p, Gamemode gamemode) {
        new AchievementInventory(p, this, gamemode);
    }

}
