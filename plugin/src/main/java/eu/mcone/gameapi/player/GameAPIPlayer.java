/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.stats.CoreStats;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.BackpackSimpleItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.event.onepass.LevelChangeEvent;
import eu.mcone.gameapi.api.event.onepass.XpChangeEvent;
import eu.mcone.gameapi.api.event.stats.PlayerRoundStatsChangeEvent;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.onepass.OnePassManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerSettings;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.stats.StatsHistory;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.kit.GameKitManager;
import eu.mcone.gameapi.team.GameTeamManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class GameAPIPlayer extends eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayer<GameAPIPlayerProfile> implements GamePlayer {

    private static final int ONE_PASS_EMERALDS_PRICE = 150;
    private static final GameAPI system = GameAPI.getInstance();

    @Getter
    private Map<String, Set<BackpackItem>> backpackItems;
    @Getter
    private Map<String, String> currentKits;
    private Map<Gamemode, Map<Achievement, Long>> achievements;
    @Getter
    private GamePlayerSettings settings;

    @Getter
    private GamePlayerState state = GamePlayerState.PLAYING;
    @Getter
    private BackpackSimpleItem currentBackpackItem;
    @Getter
    @Setter
    private boolean effectsVisible = true;
    @Getter
    private final StatsHistory statsHistory;
    @Getter
    private int oneLevel, oneXp;
    @Getter
    private boolean onePass;

    private final Player player;
    @Getter
    @Setter
    private Team team;

    @Getter
    private final CoreStats stats;

    public GameAPIPlayer(CorePlayer player) {
        super(player);
        this.player = player.bukkit();

        statsHistory = new StatsHistory();
        this.stats = GamePlugin.isGamePluginInitialized() && GamePlugin.getGamePlugin().getGamemode() != null
                ? player.getStats(GamePlugin.getGamePlugin().getGamemode(), CoreStats.class)
                : null;
    }

    @Override
    public GameAPIPlayerProfile reload() {
        GameAPIPlayerProfile systemProfile = super.reload();
        this.backpackItems = systemProfile.getItemMap();
        this.currentKits = systemProfile.getCurrentKits();
        this.achievements = systemProfile.getAchievementMap();
        this.settings = systemProfile.getSettings();
        this.currentBackpackItem = systemProfile.getCurrentBackpackItem();
        this.effectsVisible = systemProfile.getSettings().isEnableGadgets();
        this.oneLevel = systemProfile.getOneLevel();
        this.oneXp = systemProfile.getOneXp();
        this.onePass = systemProfile.isOnePass();

        return super.reload();
    }

    @Override
    protected GameAPIPlayerProfile loadData() {
        return system.loadGameProfile(corePlayer.bukkit(), GameAPIPlayerProfile.class);
    }

    @Override
    public void saveData() {
        CoreSystem.getInstance().getCoreStatsManager().save(stats);

        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_HISTORY_MANAGER)) {
            if (GamePlugin.getGamePlugin().hasOption(Option.GAME_HISTORY_HISTORY_MODE)) {
                GamePlugin.getGamePlugin().getGameHistoryManager().getCurrentGameHistory().getPlayer(player).setStatsHistory(statsHistory);
            }
        }

        system.saveGameProfile(new GameAPIPlayerProfile(corePlayer.bukkit(), backpackItems, achievements, currentKits, currentBackpackItem, oneLevel, oneXp, onePass));
    }

    /*
     * Backpack System
     */

    public void setCurrentBackpackItem(BackpackItem item, DefaultCategory category) {
        currentBackpackItem = new BackpackSimpleItem(category, item);
        saveData();
    }

    @Override
    public void resetCurrentBackpackItem() {
        currentBackpackItem = null;
        saveData();
    }

    @Override
    public void addBackpackItem(String category, BackpackItem item) throws IllegalArgumentException {
        if (GamePlugin.getGamePlugin().getBackpackManager().categoryExists(category)) {
            if (GamePlugin.getGamePlugin().getBackpackManager().itemExists(item)) {
                if (!hasBackpackItem(category, item)) {
                    if (backpackItems.containsKey(category)) {
                        backpackItems.get(category).add(item);
                    } else {
                        backpackItems.put(category, new HashSet<>(Collections.singletonList(item)));
                    }

                    saveData();
                }
            } else {
                throw new IllegalArgumentException("Backpack item " + item + " could not be added. Item is not registered in any Category!");
            }
        } else {
            throw new IllegalArgumentException("Backpack item " + item + " couer().itemExild not be added. Category " + category + " does not exist!");
        }
    }

    @Override
    public boolean hasBackpackItem(String category, BackpackItem item) {
        return hasBackpackItem(category, item.getId());
    }

    @Override
    public boolean hasBackpackItem(String category, int id) {
        for (BackpackItem item : backpackItems.getOrDefault(category, Collections.emptySet())) {
            if (item.getId() == id) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void removeBackpackItem(String category, BackpackItem item) {
        if (hasBackpackItem(category, item)) {
            backpackItems.getOrDefault(category, Collections.emptySet()).remove(item);
            saveData();
        }
    }

    @Override
    public void buyBackpackItem(Player p, String category, BackpackItem item) {
        if (!hasBackpackItem(category, item)) {
            CorePlayer cp = CoreSystem.getInstance().getCorePlayer(p);

            if ((cp.getEmeralds() - item.getBuyPrice()) >= 0) {
                cp.removeEmeralds(item.getBuyPrice());
                addBackpackItem(category, item);

                p.closeInventory();
                GamePlugin.getGamePlugin().getMessenger().send(p, "§2Du hast erfolgreich das Item " + item.getName() + "§2 gekauft!");
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                p.closeInventory();
                GamePlugin.getGamePlugin().getMessenger().send(p, "§4Du hast nicht genügend Emeralds!");
                p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
            }
        } else {
            p.closeInventory();
            GamePlugin.getGamePlugin().getMessenger().send(p, "§4Du besitzt dieses Item bereits!");
            p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
        }
    }


    @Override
    public boolean hasDefaultItem(DefaultItem item) {
        return hasBackpackItem(item.getCategory().name(), item.getId());
    }

    @Override
    public void addDefaultItem(DefaultItem item) {
        addBackpackItem(
                item.getCategory().name(),
                GamePlugin.getGamePlugin().getBackpackManager().getBackpackItem(item.getCategory().name(), item.getId())
        );
    }

    @Override
    public void removeDefaultItem(DefaultItem item) {
        removeBackpackItem(
                item.getCategory().name(),
                GamePlugin.getGamePlugin().getBackpackManager().getBackpackItem(item.getCategory().name(), item.getId())
        );
    }

    @Override
    public void resetEffectsVisible() {
        this.effectsVisible = settings.isEnableGadgets();
    }


    /*
     * Achievement system
     */

    @Override
    public void addAchievement(String name) throws IllegalArgumentException {
        Achievement achievement = GamePlugin.getGamePlugin().getAchievementManager().getAchievement(name);

        if (achievement != null) {
            if (!hasAchievement(name)) {
                if (GamePlugin.getGamePlugin().getAchievementManager().setAchievement(this, achievement)) {
                    if (achievements.containsKey(GamePlugin.getGamePlugin().getGamemode())) {
                        achievements.get(GamePlugin.getGamePlugin().getGamemode()).put(achievement, System.currentTimeMillis() / 1000);
                    } else {
                        achievements.put(GamePlugin.getGamePlugin().getGamemode(), new HashMap<Achievement, Long>() {{
                            put(achievement, System.currentTimeMillis() / 1000);
                        }});
                    }

                    saveData();
                }
            }
        } else {
            throw new IllegalArgumentException("Cannot add Achievement " + name + " to player " + getCorePlayer().getName() + ". Achievement does not exist!");
        }
    }

    @Override
    public Map<Achievement, Long> getAchievements() {
        return getAchievements(GamePlugin.getGamePlugin().getGamemode());
    }

    @Override
    public Map<Achievement, Long> getAchievements(Gamemode gamemode) {
        return achievements.getOrDefault(gamemode, new HashMap<>());
    }

    @Override
    public void addAchievements(String... names) {
        for (String name : names) {
            addAchievement(name);
        }
    }

    @Override
    public void removeAchievement(String name) {
        if (hasAchievement(name)) {
            Achievement achievement = GamePlugin.getGamePlugin().getAchievementManager().getAchievement(name);
            achievements.get(GamePlugin.getGamePlugin().getGamemode()).remove(achievement);
            saveData();
        }
    }

    @Override
    public void removeAchievements(final String... names) {
        for (String achievementName : names) {
            removeAchievement(achievementName);
        }
    }

    @Override
    public boolean hasAchievement(String name) {
        for (Achievement achievement : getAchievements(GamePlugin.getGamePlugin().getGamemode()).keySet()) {
            if (achievement.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }


    /*
     * Player system
     */

    @Override
    public void setState(GamePlayerState state) {
        ((GamePlayerManager) GamePlugin.getGamePlugin().getPlayerManager()).setState(this, state);
        this.state = state;
    }

    @Override
    public boolean isInCameraMode() {
        return GamePlugin.getGamePlugin().getPlayerManager().isInCameraMode(this);
    }

    @Override
    public void setInCameraMode(Player target) {
        ((GamePlayerManager) GamePlugin.getGamePlugin().getPlayerManager()).setCameraMode(this, target);
    }

    @Override
    public void removeFromCameraMode() {
        ((GamePlayerManager) GamePlugin.getGamePlugin().getPlayerManager()).removeFromCameraMode(this);
    }


    /*
     * Team system
     */

    @Override
    public void changeTeamTo(Team team) {
        ((GameTeamManager) GamePlugin.getGamePlugin().getTeamManager()).setPlayerTeam(this, team);
    }

    @Override
    public void removeFromGame(boolean quitted) {
        //TODO: Add team stage integration

        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER)) {
            ((GameTeamManager) GamePlugin.getGamePlugin().getTeamManager()).removeFromGame(this);
        }

        if (!quitted && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            setState(GamePlayerState.SPECTATING);
        }
    }


    /*
     * Stats System
     */

    public void addKills(int kills) {
        statsHistory.addKills(kills);
        stats.addKills(kills);
        callStatsEvent();
    }

    @Override
    public void addDeaths(int deaths) {
        statsHistory.addDeaths(deaths);
        stats.addDeaths(deaths);
        callStatsEvent();
    }

    @Override
    public void addGoals(int goals) {
        statsHistory.addGoals(goals);
        stats.addGoals(goals);
        callStatsEvent();
    }

    @Override
    public void addLose(int loses) {
        stats.addLosses(loses);
        callStatsEvent();
    }

    private void callStatsEvent() {
        GamePlugin.getGamePlugin().getServer().getPluginManager().callEvent(new PlayerRoundStatsChangeEvent(player, statsHistory.getKills(), statsHistory.getDeaths(), statsHistory.getGoals()));
    }

    @Override
    public double getRoundKD() {
        return statsHistory.getKD();
    }

    @Override
    public int getRoundKills() {
        return statsHistory.getKills();
    }

    @Override
    public int getRoundDeaths() {
        return statsHistory.getDeaths();
    }

    @Override
    public int getRoundGoals() {
        return statsHistory.getGoals();
    }


    /*
     * Kit System
     */

    @Override
    public Kit getCurrentKit() {
        return GamePlugin.getGamePlugin().getKitManager().getKit(
                currentKits.getOrDefault(
                        ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).getPluginKey(),
                        null
                )
        );
    }

    public void saveCurrentKit(Kit kit) {
        currentKits.put(((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).getPluginKey(), kit.getName());
        saveData();
    }

    @Override
    public boolean setKit(Kit kit, boolean force) {
        if (kit.equals(getCurrentKit()) || force) {
            ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).setKit(kit, bukkit());
            return true;
        } else return false;
    }

    @Override
    public void setCurrentKitAgain(Kit defaultKit) {
        Kit currentKit = getCurrentKit();

        if (!setKit(currentKit != null ? currentKit : defaultKit, true)) {
            throw new IllegalStateException("Could not set current kit again for player "+getCorePlayer().getName()+". setKit returns false with force == true");
        }
    }

    @Override
    public boolean hasKit(Kit kit) {
        return ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).hasKit(getCorePlayer().getUuid(), kit);
    }

    @Override
    public boolean buyKit(Kit kit) {
        if (!hasKit(kit) && kit.getCoinsPrice() > 0) {
            if ((getCorePlayer().getCoins() - kit.getCoinsPrice()) < 0) {
                GamePlugin.getGamePlugin().getMessenger().send(bukkit(), "§4Du hast nicht genügend Coins!");
                return false;
            }

            getCorePlayer().removeCoins(kit.getCoinsPrice());
        }

        if (((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).isChooseKitsForServerLifetime()) {
            ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).addKit(this, kit);
        }

        setKit(kit, true);
        return true;
    }

    @Override
    public boolean addKit(Kit kit) {
        return ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).addKit(this, kit);
    }

    @Override
    public boolean removeKit(Kit kit) {
        return ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).removeKit(this, kit);
    }

    @Override
    public void openKitInventory(Runnable onBackClick) {
        GamePlugin.getGamePlugin().getKitManager().openKitsInventory(bukkit(), onBackClick);
    }

    @Override
    public ModifiedKit getModifiedKit(Kit kit) {
        return getModifiedKit(kit.getName());
    }

    @Override
    public ModifiedKit getModifiedKit(String name) {
        return GamePlugin.getGamePlugin().getKitManager().getModifiedKit(bukkit(), name);
    }

    @Override
    public boolean hasKitModified(Kit kit) {
        return getModifiedKit(kit) != null;
    }


    /*
     * OnePass
     */

    @Override
    public void buyOnePass(boolean premium) throws IllegalStateException {
        if (!onePass) {
            if (corePlayer.getEmeralds() >= ONE_PASS_EMERALDS_PRICE) {
                corePlayer.removeEmeralds(ONE_PASS_EMERALDS_PRICE);

                if (premium) {
                    setOnePassLevel(OnePassManager.ONE_PASS_PREMIUM_LEVEL);
                }
                onePass = true;
                saveData();
            } else {
                player.closeInventory();
                GameAPI.getInstance().getMessenger().send(player, "§4Du hast nicht genügend Emeralds!");
                throw new IllegalStateException("Could not buy OnePass for " + corePlayer.getName() + ". Player has not enough emeralds!");
            }
        } else {
            player.closeInventory();
            GameAPI.getInstance().getMessenger().send(player, "§4Du hast den OnePass bereits gekauft!");
            throw new IllegalStateException("Could not buy OnePass for " + corePlayer.getName() + ". Player already has OnePass!");
        }
    }

    @Override
    public void setOnePassLevel(int level) {
        if (level < oneLevel) {
            removeOnePassLevel(level);
        } else if (level > oneLevel) {
            addOnePassLevel(level);
        }
    }

    @Override
    public void addOnePassLevel(int level) {
        boolean multiAdd = level > 1;
        while (level > 0) {
            oneLevel++;
            level--;
            Bukkit.getPluginManager().callEvent(new LevelChangeEvent(this, oneLevel - 1));
            GamePlugin.getGamePlugin().getOnePassManager().levelChanged(this, oneLevel - 1, oneLevel, !multiAdd);
        }

        saveData();
    }

    @Override
    public void removeOnePassLevel(int level) {
        while (level > 0) {
            oneLevel--;
            level--;
            Bukkit.getPluginManager().callEvent(new LevelChangeEvent(this, oneLevel + 1));
            GamePlugin.getGamePlugin().getOnePassManager().levelChanged(this, oneLevel + 1, oneLevel, false);
        }

        saveData();
    }

    @Override
    public void setOnePassXp(int xp) {
        if (xp < oneXp) {
            removeOnePassXp(xp);
        } else if (xp > oneXp) {
            addOnePassXp(xp);
        }
    }

    @Override
    public void addOnePassXp(int xp) {
        int oldXp = oneXp;

        while ((oneXp + xp) >= OnePassManager.NEEDED_XP_FOR_NEXT_LEVEL) {
            xp -= OnePassManager.NEEDED_XP_FOR_NEXT_LEVEL;
            addOnePassLevel(1);
        }
        oneXp += xp;

        Bukkit.getPluginManager().callEvent(new XpChangeEvent(this, oldXp));
        saveData();
    }

    @Override
    public void removeOnePassXp(int xp) {
        int oldXp = oneXp;

        if ((((oneLevel * OnePassManager.NEEDED_XP_FOR_NEXT_LEVEL) + oneXp) - xp) > 0) {
            while ((oneXp - xp) < 0) {
                xp -= OnePassManager.NEEDED_XP_FOR_NEXT_LEVEL;
                removeOnePassLevel(1);
            }
            oneXp -= xp;

            Bukkit.getPluginManager().callEvent(new XpChangeEvent(this, oldXp));
            saveData();
        } else {
            throw new IllegalStateException("Cannot remove " + xp + " xp from player " + corePlayer.getName() + ". This player has just " + oneXp + " xp!");
        }
    }

}
