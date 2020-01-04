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
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.kit.GameKitManager;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public abstract class GameAPIPlayer extends eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayer<GameAPIPlayerProfile> implements GamePlayer {

    private static final GameAPI system = GameAPI.getInstance();

    @Getter
    private Map<String, Set<BackpackItem>> backpackItems;
    private List<ModifiedKit> customKits;
    private Map<Gamemode, Map<Achievement, Long>> achievements;

    public GameAPIPlayer(CorePlayer player) {
        super(player);
    }

    @Override
    public GameAPIPlayerProfile reload() {
        GameAPIPlayerProfile systemProfile = super.reload();
        this.backpackItems = systemProfile.getItemMap();
        this.customKits = systemProfile.getCustomKits();
        this.achievements = systemProfile.getAchievementMap();

        return super.reload();
    }

    @Override
    protected void saveData() {
        system.saveGameProfile(new GameAPIPlayerProfile(corePlayer.bukkit(), backpackItems, achievements/*, customKits*/));
    }


    /*
     * Backpack System
     */

    @Override
    public void addBackpackItem(String category, BackpackItem item) throws IllegalArgumentException {
        if (GamePlugin.getPlugin().getBackpackManager().categoryExists(category)) {
            if (GamePlugin.getPlugin().getBackpackManager().itemExists(item)) {
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
            throw new IllegalArgumentException("Backpack item " + item + " could not be added. Category "+category+" does not exist!");
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
                GamePlugin.getPlugin().getMessager().send(p, "§2Du hast erfolgreich das Item " + item.getName() + "§2 gekauft!");
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                p.closeInventory();
                GamePlugin.getPlugin().getMessager().send(p, "§4Du hast nicht genügend Emeralds!");
                p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
            }
        } else {
            p.closeInventory();
            GamePlugin.getPlugin().getMessager().send(p, "§4Du besitzt dieses Item bereits!");
            p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
        }
    }


    /*
     * Achievement System
     */

    @Override
    public void addAchievement(String name) throws IllegalArgumentException {
        Achievement achievement = GamePlugin.getPlugin().getAchievementManager().getAchievement(name);

        if (achievement != null) {
            if (!hasAchievement(name)) {
                if (GamePlugin.getPlugin().getAchievementManager().setAchievement(this, achievement)) {
                    if (achievements.containsKey(GamePlugin.getPlugin().getGamemode())) {
                        achievements.get(GamePlugin.getPlugin().getGamemode()).put(achievement, System.currentTimeMillis() / 1000);
                    } else {
                        achievements.put(GamePlugin.getPlugin().getGamemode(), new HashMap<Achievement, Long>(){{ put(achievement, System.currentTimeMillis() / 1000); }});
                    }

                    saveData();
                }
            }
        } else {
            throw new IllegalArgumentException("Cannot add Achievement "+name+" to player "+getCorePlayer().getName()+". Achievement does not exist!");
        }
    }

    @Override
    public Map<Achievement, Long> getAchievements() {
        return getAchievements(GamePlugin.getPlugin().getGamemode());
    }

    @Override
    public Map<Achievement, Long> getAchievements(Gamemode gamemode) {
        return achievements.get(gamemode);
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
            Achievement achievement = GamePlugin.getPlugin().getAchievementManager().getAchievement(name);
            achievements.get(GamePlugin.getPlugin().getGamemode()).remove(achievement);
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
        for (Achievement achievement : achievements.get(GamePlugin.getPlugin().getGamemode()).keySet()) {
            if (achievement.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }



    /*
     * Kit System
     */

    @Override
    public void modifyKit(final Kit kit, final Map<ItemStack, Integer> items) {
        ((GameKitManager) GamePlugin.getPlugin().getKitManager()).modifyKit(bukkit(), kit, items);
    }

    @Override
    public boolean hasKitModified(String name) {
        return ((GameKitManager) GamePlugin.getPlugin().getKitManager()).hasKitModified(bukkit(), name);
    }

    @Override
    public boolean setKit(Kit kit) {
        if (hasKit(kit)) {
            ((GameKitManager) GamePlugin.getPlugin().getKitManager()).setKit(kit, bukkit());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasKit(Kit kit) {
        return ((GameKitManager) GamePlugin.getPlugin().getKitManager()).hasKit(getCorePlayer().getUuid(), kit);
    }

    @Override
    public boolean buyKit(Kit kit) {
        if (!hasKit(kit)) {
            if (kit.getCoinsPrice() > 0) {
                if ((getCorePlayer().getCoins() - kit.getCoinsPrice()) < 0) {
                    GamePlugin.getPlugin().getMessager().send(bukkit(), "§4Du hast nicht genügend Coins!");
                    return false;
                }

                getCorePlayer().removeCoins(kit.getCoinsPrice());
            }

            ((GameKitManager) GamePlugin.getPlugin().getKitManager()).addKit(this, kit);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeKit(Kit kit) {
        if (hasKit(kit)) {
            ((GameKitManager) GamePlugin.getPlugin().getKitManager()).removeKit(this, kit);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ModifiedKit getModifiedKit(Kit kit) {
        return getModifiedKit(kit.getName());
    }

    @Override
    public ModifiedKit getModifiedKit(String name) {
        return ((GameKitManager) GamePlugin.getPlugin().getKitManager()).getModifiedKit(bukkit(), name);
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
