/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.event.stats.PlayerRoundStatsChangeEvent;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerSettings;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamDefinition;
import eu.mcone.gameapi.kit.GameKitManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class GameAPIPlayer extends eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayer<GameAPIPlayerProfile> implements GamePlayer {

    private static final GameAPI system = GameAPI.getInstance();

    @Getter
    private Map<String, Set<BackpackItem>> backpackItems;
    private List<ModifiedKit> customKits;
    private Map<Gamemode, Map<Achievement, Long>> achievements;
    @Getter
    private GamePlayerSettings settings;

    @Getter
    @Setter
    private boolean effectsVisible = true;

    @Getter
    private int roundKills;
    @Getter
    private int roundDeaths;
    @Getter
    private int roundGoals;

    private Player player;
    @Getter
    private Team team;

    public GameAPIPlayer(CorePlayer player) {
        super(player);
        this.player = player.bukkit();
    }

    @Override
    public GameAPIPlayerProfile reload() {
        GameAPIPlayerProfile systemProfile = super.reload();
        this.backpackItems = systemProfile.getItemMap();
        this.customKits = systemProfile.getCustomKits();
        this.achievements = systemProfile.getAchievementMap();
        this.settings = systemProfile.getSettings();

        return super.reload();
    }

    @Override
    protected GameAPIPlayerProfile loadData() {
        return system.loadGameProfile(corePlayer.bukkit(), GameAPIPlayerProfile.class);
    }

    @Override
    protected void saveData() {
        system.saveGameProfile(new GameAPIPlayerProfile(corePlayer.bukkit(), backpackItems, achievements));
    }

    /*
     * Backpack System
     */

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
            throw new IllegalArgumentException("Backpack item " + item + " could not be added. Category " + category + " does not exist!");
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
                GamePlugin.getGamePlugin().getMessager().send(p, "§2Du hast erfolgreich das Item " + item.getName() + "§2 gekauft!");
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                p.closeInventory();
                GamePlugin.getGamePlugin().getMessager().send(p, "§4Du hast nicht genügend Emeralds!");
                p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
            }
        } else {
            p.closeInventory();
            GamePlugin.getGamePlugin().getMessager().send(p, "§4Du besitzt dieses Item bereits!");
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


    /*
     * Achievement System
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
     *
     * Team management
     */
    public void setTeam(Team team) {
        if (this.team != null)
            this.team.removePlayer(player);

        this.team = team;
        this.team.addPlayer(player);
    }

    public void setTeam(TeamDefinition team) {
        if (this.team != null)
            this.team.removePlayer(player);

        this.team = GamePlugin.getGamePlugin().getTeamManager().getTeam(team);
        this.team.addPlayer(player);
    }

    public void removeTeam() {
        if (this.team != null) {
            this.team.removePlayer(player);
            this.team = null;
        }
    }

    public void removeFromGame() {
        //TODO: Add team stage integration
        removeTeam();
        GamePlugin.getGamePlugin().getPlayerManager().setPlaying(player, false);
    }

    public void addKills(final int var) {
        this.roundKills = this.roundKills + var;
        corePlayer.getStats(GamePlugin.getGamePlugin().getGamemode()).addKills(var);
        callStatsEvent();
    }

    public void addDeaths(final int var) {
        this.roundDeaths = this.roundDeaths + var;
        corePlayer.getStats(GamePlugin.getGamePlugin().getGamemode()).addDeaths(var);
        callStatsEvent();
    }

    public void addGoals(final int var) {
        this.roundGoals = this.roundGoals + var;
        corePlayer.getStats(GamePlugin.getGamePlugin().getGamemode()).addGoal(var);
        callStatsEvent();
    }

    public void addLose(final int var) {
        corePlayer.getStats(GamePlugin.getGamePlugin().getGamemode()).addLoses(var);
        callStatsEvent();
    }

    private void callStatsEvent() {
        GamePlugin.getGamePlugin().getServer().getPluginManager().callEvent(new PlayerRoundStatsChangeEvent(player, roundKills, roundDeaths, roundGoals));
    }

    public double getRoundKD() {
        double KD = (double) roundDeaths / roundKills;

        if (KD <= 0.0) {
            return 0;
        } else {
            return KD;
        }
    }

    /*
     * Kit System
     */

    @Override
    public void modifyKit(final Kit kit, final Map<ItemStack, Integer> items) {
        GamePlugin.getGamePlugin().getKitManager().modifyKit(bukkit(), kit, items);
    }

    @Override
    public boolean hasKitModified(String name) {
        return GamePlugin.getGamePlugin().getKitManager().hasKitModified(bukkit(), name);
    }

    @Override
    public boolean setKit(Kit kit) {
        if (hasKit(kit) || ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).isApplyKitsOnce()) {
            ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).setKit(kit, bukkit());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasKit(Kit kit) {
        return ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).hasKit(getCorePlayer().getUuid(), kit);
    }

    @Override
    public Kit getCurrentKit() {
        return ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).getCurrentKit(bukkit());
    }

    @Override
    public boolean buyKit(Kit kit) {
        if (!hasKit(kit)) {
            if (kit.getCoinsPrice() > 0) {
                if ((getCorePlayer().getCoins() - kit.getCoinsPrice()) < 0) {
                    GamePlugin.getGamePlugin().getMessager().send(bukkit(), "§4Du hast nicht genügend Coins!");
                    return false;
                }

                getCorePlayer().removeCoins(kit.getCoinsPrice());
            }

            ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).addKit(this, kit);
            setKit(kit);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addKit(Kit kit) {
        if (((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).isApplyKitsOnce()) {
            return true;
        }

        if (!hasKit(kit)) {
            ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).addKit(this, kit);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeKit(Kit kit) {
        if (((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).isApplyKitsOnce()) {
            return true;
        }

        if (hasKit(kit)) {
            ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).removeKit(this, kit);
            return true;
        } else {
            return false;
        }
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
        return ((GameKitManager) GamePlugin.getGamePlugin().getKitManager()).getModifiedKit(bukkit(), name);
    }

    /*
     * Game methods
     *//*
    
    public void setTeam(Team team) {
        this.team = team;
        addTeamSize(1);
        GameTemplate.getInstance().getChats().get(team).add(player);
        GameTemplate.getInstance().getTeams().put(player.getUniqueId(), team);
        GameSystem.getInstance().sendConsoleMessage("Put the player `" + name + "` in team `" + team + "`");
    }

    public void updateTeamAlive(boolean var) {
        team.setBedAlive(var);
    }

    public void setTeamSize(int size) {
        if (team != Team.ERROR) {
            if (size > -1) {
                team.setValue(size);
                GameSystem.getInstance().sendConsoleMessage("set new size `" + size + "` for team `" + team + "`");
            }
        }
    }

    public void addTeamSize(final int size) {
        if (team != Team.ERROR) {
            GameSystem.getInstance().sendConsoleMessage("add size `" + size + "` to team `" + team + "`");
            if (team.getValue() == 0) {
                team.setValue(size);
            } else {
                int var = team.getValue() + size;
                team.setValue(var);
            }
        }
    }

    public void removeTeamSize(final int size) {
        if (team != Team.ERROR) {
            if (team.getValue() < 0) {
                GameSystem.getInstance().sendConsoleMessage("§cCan not remove `" + size + "` from team, because the team size is smaller than 0");
            } else if (team.getValue() == 1) {
                team.setValue(0);
                GameSystem.getInstance().sendConsoleMessage("Set team size to 0");
            } else if (team.getValue() >= size) {
                int var = team.getValue() - size;
                team.setValue(var);
                GameSystem.getInstance().sendConsoleMessage("Remove `" + size + "` from team `" + team + "`");
            }
        }
    }

    public int getTeamSize() {
        return team.getValue();
    }

    public int getPlayingSize() {
        return GameTemplate.getInstance().getPlaying().size();
    }

    public void addCoins(final int coins) {
        corePlayer.addCoins(coins);
        this.roundCoins += coins;
    }

    public void addRoundKill() {
        this.roundKills = this.roundKills + 1;
        GameSystem.getInstance().sendConsoleMessage("Add 1 roundKill to player `" + name + "`");
    }

    public void addRoundKill(final int var) {
        this.roundKills = this.roundKills + var;
        GameSystem.getInstance().sendConsoleMessage("Add `" + var + "` roundKills to player `" + name + "`");
    }

    public void addRoundDeath() {
        this.roundDeaths = this.roundDeaths + 1;
        GameSystem.getInstance().sendConsoleMessage("Add 1 roundDeath to player `" + name + "`");
    }

    public void addRoundDeath(final int var) {
        this.roundDeaths = this.roundDeaths + var;
        GameSystem.getInstance().sendConsoleMessage("Add `" + var + "` roundDeaths to player `" + name + "`");
    }

    public void addGoal() {
        this.roundGoals = this.roundGoals + 1;
        GameSystem.getInstance().sendConsoleMessage("Add 1 destroyed goals to player `" + name + "`");
    }

    public void addGoals(final int var) {
        this.roundGoals = this.roundGoals + var;
        GameSystem.getInstance().sendConsoleMessage("Add `" + var + "` destroyed goals to player `" + name + "`");
    }

    public double getRoundKD() {
        double KD = (double) roundDeaths / roundKills;
        if (KD <= 0.0) {
            return 0;
        } else {
            return KD;
        }
    }

    public List<Player> getTeamChat() {
        return GameTemplate.getInstance().getChats().get(team);
    }

    public void setPlaying(boolean var) {
        if (spectator) {
            setSpectator(false);
        }

        this.playing = var;

        if (var) {
            if (!GameTemplate.getInstance().getPlaying().contains(player)) {
                GameTemplate.getInstance().getPlaying().add(player);
            }
        } else {
            GameTemplate.getInstance().getPlaying().remove(player);
        }
    }

    public void setSpectator(boolean var) {
        if (playing) {
            setPlaying(false);
        }

        this.spectator = var;

        if (var) {
            if (!GameTemplate.getInstance().getSpectators().contains(player)) {
                GameTemplate.getInstance().getSpectators().add(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
                player.getInventory().setItem(InventorySlot.ROW_1_SLOT_5, SpectatorInventory.NAVIGATOR);

                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.hidePlayer(player);
                }
            }
        } else {
            GameTemplate.getInstance().getSpectators().remove(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.getInventory().remove(SpectatorInventory.NAVIGATOR);

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.showPlayer(player);
            }
        }
    }

    public void setCurrentKit(Kit kit) {
        this.currentKit = kit;
    }

    public void removeCurrentKit() {
        this.currentKit = null;
    }

    public boolean hasCurrentKit() {
        return currentKit != null;
    }

    public void removeFromGame() {
        if (GameTemplate.getInstance().getPlaying().contains(player)) {
            GameSystem.getInstance().sendConsoleMessage("Remove player `" + name + "` from game");
            if (team != Team.ERROR) {
                remove();
            } else {
                saveData();
                GameTemplate.getInstance().unregisterGamePlayer(this);
                GameTemplate.getInstance().getPlaying().remove(player);
            }
        }
    }

    public void removeFromTeamSelection() {
        if (GameTemplate.getInstance().getPlaying().contains(player)) {
            GameSystem.getInstance().sendConsoleMessage("Remove player `" + name + "` from TeamSelection");
            if (team != Team.ERROR) {
                removeTeamSize(1);

                GameTemplate.getInstance().getTeamManager().getTeamStageHandler().removePlayerFromStage(this);
                GameTemplate.getInstance().getChats().get(team).remove(player);
                GameTemplate.getInstance().getTeams().remove(player.getUniqueId());
            }
        }
    }

    public void destroy() {
        GameSystem.getInstance().sendConsoleMessage("Destroy GamePlayer `" + name + "`");
        if (GameTemplate.getInstance().getPlaying().contains(player)) {
            if (team != Team.ERROR) {
                remove();

                saveData();
                GameTemplate.getInstance().unregisterGamePlayer(this);
            } else {
                saveData();
                GameTemplate.getInstance().unregisterGamePlayer(this);
                GameTemplate.getInstance().getPlaying().remove(player);
            }
        } else {
            saveData();
            GameTemplate.getInstance().unregisterGamePlayer(this);
        }
    }

    private void remove() {
        removeTeamSize(1);
        GameTemplate.getInstance().getChats().get(team).remove(player);
        GameTemplate.getInstance().getTeams().remove(player.getUniqueId());
        GameTemplate.getInstance().getPlaying().remove(player);
        saveData();
        GameTemplate.getInstance().unregisterGamePlayer(this);
    }*/

}
