/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.player;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.profile.GameProfile;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.BackpackSimpleItem;
import eu.mcone.gameapi.api.player.GamePlayerSettings;
import eu.mcone.gameapi.kit.KitSettings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
public class GameAPIPlayerProfile extends GameProfile {

    private Map<String, List<Integer>> items = new HashMap<>();
    private Map<String, Map<String, Long>> achievements = new HashMap<>();
    private Map<String, KitSettings> kitSettings = new HashMap<>();
    private GamePlayerSettings settings = new GamePlayerSettings();
    private BackpackSimpleItem currentBackpackItem;
    private int oneLevel, oneXp;
    private boolean onePass;

    private transient Map<String, Set<BackpackItem>> itemMap = new HashMap<>();
    private transient Map<Gamemode, Map<Achievement, Long>> achievementMap = new HashMap<>();

    GameAPIPlayerProfile(final Player p, final Map<String, Set<BackpackItem>> playerItems, Map<Gamemode, Map<Achievement, Long>> achievements, Map<String, KitSettings> kitSettings, BackpackSimpleItem currentBackpackItem, int oneLevel, int oneXp, boolean onePass) {
        super(p);
        this.oneLevel = oneLevel;
        this.oneXp = oneXp;
        this.onePass = onePass;
        this.currentBackpackItem = currentBackpackItem;
        this.kitSettings = kitSettings;

        for (Map.Entry<String, Set<BackpackItem>> entry : playerItems.entrySet()) {
            List<Integer> itemIds = new ArrayList<>();

            for (BackpackItem item : entry.getValue()) {
                itemIds.add(item.getId());
            }

            this.items.put(entry.getKey(), itemIds);
        }

        for (Map.Entry<Gamemode, Map<Achievement, Long>> entry : achievements.entrySet()) {
            Map<String, Long> achievementMap = new HashMap<>();

            for (Map.Entry<Achievement, Long> achievement : entry.getValue().entrySet()) {
                achievementMap.put(achievement.getKey().getName(), achievement.getValue());
            }

            this.achievements.put(entry.getKey().toString(), achievementMap);
        }
    }

    @Override
    public void doSetData(Player p) {
        if (GamePlugin.isGamePluginInitialized()) {
            if (GamePlugin.getGamePlugin().hasModule(Module.BACKPACK_MANAGER)) {
                for (Map.Entry<String, List<Integer>> itemEntry : items.entrySet()) {
                    Set<BackpackItem> items = new HashSet<>();

                    for (int itemId : itemEntry.getValue()) {
                        BackpackItem item = GamePlugin.getGamePlugin().getBackpackManager().getBackpackItem(itemEntry.getKey(), itemId);

                        if (item != null) {
                            items.add(item);
                        }
                    }

                    itemMap.put(itemEntry.getKey(), items);
                }
            }

            if (GamePlugin.getGamePlugin().hasModule(Module.ACHIEVEMENT_MANAGER)) {
                for (Map.Entry<String, Map<String, Long>> achievementEntry : achievements.entrySet()) {
                    Map<Achievement, Long> achievements = new HashMap<>();

                    for (Map.Entry<String, Long> solvedAchievement : achievementEntry.getValue().entrySet()) {
                        Achievement achievement = GamePlugin.getGamePlugin().getAchievementManager().getAchievement(solvedAchievement.getKey());

                        if (achievement != null) {
                            achievements.put(achievement, solvedAchievement.getValue());
                        }
                    }

                    achievementMap.put(Gamemode.valueOf(achievementEntry.getKey()), achievements);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "GameAPIPlayerProfile{" +
                "items=" + items +
                ", achievements=" + achievements +
                ", currentKits=" + kitSettings +
                ", settings=" + settings +
                ", currentBackpackItem=" + currentBackpackItem +
                ", oneLevel=" + oneLevel +
                ", oneXp=" + oneXp +
                ", onePass=" + onePass +
                ", itemMap=" + itemMap +
                ", achievementMap=" + achievementMap +
                '}';
    }
}
