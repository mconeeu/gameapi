/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.Stats;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.enums.Item;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.achivements.Achievement;
import eu.mcone.gamesystem.api.game.achivements.SolvedAchievement;
import eu.mcone.gamesystem.api.game.event.GameAchievementEvent;
import eu.mcone.gamesystem.api.game.manager.kit.Kit;
import eu.mcone.gamesystem.api.game.manager.kit.KitItem;
import eu.mcone.gamesystem.api.game.manager.kit.KitItemType;
import eu.mcone.gamesystem.api.game.manager.kit.sorting.CustomKit;
import eu.mcone.gamesystem.api.lobby.cards.ItemCard;
import eu.mcone.gamesystem.game.inventory.SpectatorInventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@Getter
public class GamePlayer extends eu.mcone.coresystem.api.bukkit.player.plugin.GamePlayer<GamePlayerProfile> implements eu.mcone.gamesystem.api.game.player.GamePlayer {

    private List<Item> items;
    private Map<ItemCard, Boolean> itemCards;
    private Map<String, CustomKit> customKits;
    private HashSet<SolvedAchievement> solvedAchievements;

    private transient Team team;
    private transient String name;
    private transient boolean playing = false;
    private transient boolean spectator = false;
    private transient Kit currentKit;
    private transient Stats stats;
    private transient int roundCoins;
    private transient int roundKills;
    private transient int roundDeaths;
    private transient int roundGoals;

    private transient Player player;

    public GamePlayer(Player player) {
        super(CoreSystem.getInstance().getCorePlayer(player.getUniqueId()));

        try {
            if (GameTemplate.getInstance() != null) {
                this.team = Team.ERROR;
                this.name = player.getName();
                this.stats = corePlayer.getStats(GameTemplate.getInstance().getGamemode());

                this.player = player;

                GameTemplate.getInstance().getPlaying().add(player);
                GameTemplate.getInstance().sendConsoleMessage("Create new GamePlayer `" + name + "`");
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
            GameTemplate.getInstance().sendConsoleMessage("§cException in GamePlayer.class, " + e.getMessage());
        }
    }

    @Override
    public GamePlayerProfile reload() {
        GamePlayerProfile profile = super.reload();

        this.items = profile.getItemList();
        this.itemCards = profile.getItemCardMap();
        this.customKits = profile.getCustomKits();
        this.solvedAchievements = profile.getSolvedAchievements();
        GameTemplate.getInstance().registerGamePlayer(this);

        return profile;
    }

    @Override
    protected GamePlayerProfile loadData() {
        return GameSystem.getInstance().loadGameProfile(corePlayer.bukkit(), GamePlayerProfile.class);
    }

    @Override
    protected void saveData() {
        GameSystem.getInstance().saveGameProfile(new GamePlayerProfile(corePlayer.bukkit(), items, itemCards, customKits, solvedAchievements));
    }

    //Lobby methods
    public void addItem(Item item) {
        if (!items.contains(item)) {
            items.add(item);
            saveData();
        }
    }

    public void removeItem(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            saveData();
        }
    }

    public boolean hasItem(Item i) {
        return items.contains(i);
    }

    public void buyItem(Player p, Item item) {
        if (!hasItem(item)) {
            CorePlayer cp = CoreSystem.getInstance().getCorePlayer(p);

            if ((cp.getCoins() - item.getEmeralds()) >= 0) {
                cp.removeCoins(item.getEmeralds());
                addItem(item);

                p.closeInventory();
                GameTemplate.getInstance().getMessager().send(p, "§2Du hast erfolgreich das Item " + item.getName() + "§2 gekauft!");
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                p.closeInventory();
                GameTemplate.getInstance().getMessager().send(p, "§4Du hast nicht genügend Coins!");
                p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
            }
        } else {
            p.closeInventory();
            GameTemplate.getInstance().getMessager().send(p, "§4Du besitzt dieses Item bereits!");
            p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
        }
    }

    private void addItemTemporary(Item i) {
        if (!items.contains(i)) {
            items.add(i);
        }
    }

    private void removeItemTemporary(Item i) {
        items.remove(i);
    }

    //ItemCards
    public boolean hasItemCard(final String itemCardName) {
        for (Map.Entry<ItemCard, Boolean> entry : itemCards.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(itemCardName)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasItemCard(final ItemCard itemCard) {
        for (Map.Entry<ItemCard, Boolean> entry : itemCards.entrySet()) {
            if (entry.getKey().equals(itemCard)) {
                return true;
            }
        }

        return false;
    }

    public ItemCard getItemCard(final String itemCardName) {
        for (Map.Entry<ItemCard, Boolean> entry : itemCards.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(itemCardName)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public boolean hasItemCardRedeemed(final String itemCardName) {
        for (Map.Entry<ItemCard, Boolean> entry : itemCards.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(itemCardName)) {
                return entry.getValue();
            }
        }

        return false;
    }

    public void redeemedItemCard(final ItemCard itemCard) {
        itemCards.put(itemCard, true);
        saveData();
    }

    public void redeemedItemCard(final String itemCardName) {
        ItemCard itemCard = getItemCard(itemCardName);
        if (itemCard != null) {
            redeemedItemCard(itemCard);
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cCould not found itemCard with the name " + itemCardName);
        }
    }

    public void unRedeemedItemCard(final ItemCard itemCard) {
        itemCards.put(itemCard, false);
        saveData();
    }

    public void unRedeemedItemCard(final String itemCardName) {
        ItemCard itemCard = getItemCard(itemCardName);
        if (itemCard != null) {
            unRedeemedItemCard(itemCard);
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cCould not found itemCard with the name " + itemCardName);
        }
    }

    public List<ItemCard> getItemCards() {
        List<ItemCard> itemCards = new ArrayList<>();

        for (Map.Entry<ItemCard, Boolean> entry : this.itemCards.entrySet()) {
            itemCards.add(entry.getKey());
        }

        return itemCards;
    }

    public void removeItemCard(final ItemCard itemCard) {
        itemCards.remove(itemCard);
        saveData();
    }

    public void removeItemCard(final String itemCardName) {
        ItemCard itemCard = getItemCard(itemCardName);
        if (itemCard != null) {
            removeItemCard(itemCard);
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cCould not found itemCard with the name " + itemCardName);
        }
    }

    public void addItemCard(final ItemCard itemCard) {
        itemCards.put(itemCard, false);
        saveData();
    }

    public void addItemCard(final String itemCardName) {
        ItemCard itemCard = GameTemplate.getInstance().getItemCardManager().getItemCard(itemCardName);
        if (itemCard != null) {
            addItemCard(itemCard);
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cCould not found itemCard with the name " + itemCardName);
        }
    }

    public List<ItemCard> getRedeemedItemCards() {
        List<ItemCard> redeemedItemCards = new ArrayList<>();
        for (Map.Entry<ItemCard, Boolean> entry : itemCards.entrySet()) {
            if (entry.getValue()) {
                redeemedItemCards.add(entry.getKey());
            }
        }

        return redeemedItemCards;
    }

    public List<ItemCard> getRedeemedItemCards(Gamemode gamemode) {
        List<ItemCard> redeemedItemCards = new ArrayList<>();
        for (Map.Entry<ItemCard, Boolean> entry : itemCards.entrySet()) {
            if (entry.getKey().getGamemode().equals(gamemode)) {
                if (entry.getValue()) {
                    redeemedItemCards.add(entry.getKey());
                }
            }
        }

        return redeemedItemCards;
    }

    public void givePlayerRedeemedItemCards(Gamemode gamemode) {
        Player player = bukkit();
        List<ItemCard> redeemedItemCards = getRedeemedItemCards(gamemode);
        int cards = redeemedItemCards.size();

        if (redeemedItemCards.size() > 0) {
            int freePlaceInInventory = 0;

            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
                    freePlaceInInventory++;
                }
            }

            if (freePlaceInInventory > 0) {
                for (ItemCard itemCard : redeemedItemCards) {
                    if (freePlaceInInventory > 0) {
                        player.getInventory().addItem(itemCard.getItemCardBuilder().createStack());
                        itemCards.remove(itemCard);
                        cards--;
                        GameTemplate.getInstance().getMessager().send(player, "§2Du hast das Item " + itemCard.getItemCardBuilder().getDisplayName() + " erhalten!");
                        freePlaceInInventory--;
                    } else {
                        GameTemplate.getInstance().sendConsoleMessage("§7Du hast §cnicht genügend Platz §7für alle Itemkarten in deinem Inventar, \n" +
                                "§7sortiere zuerst dein Inventar aus um die restlichen Itemkarten (§f" + cards + "§7) zu erhalten.\n" +
                                "§7Benutze hierfür /itemcards");
                        break;
                    }
                }

                saveData();
            }
        } else {
            GameTemplate.getInstance().getMessager().send(player, "§7Du besitzt momentan keine Itemkarten!");
        }
    }

    //Custom kits
    public void modifyInventory(final Kit kit, final Map<String, Double> sortedItems) {
        customKits.put(String.valueOf(kit.getKitID()), new CustomKit(System.currentTimeMillis() / 1000, sortedItems));
    }

    public CustomKit getModifiedKit(Kit kit) {
        return customKits.getOrDefault(String.valueOf(kit.getKitID()), null);
    }

    public boolean isKitModified(Kit kit) {
        return customKits.containsKey(String.valueOf(kit.getKitID()));
    }

    public void setKit(Kit kit) {
        Player player = getCorePlayer().bukkit();
        CustomKit customKit = getModifiedKit(kit);
        setCurrentKit(kit);

        if (customKit != null) {
            for (Map.Entry<String, Double> kitEntry : customKit.getCustomItems().entrySet()) {
                int slot = Integer.valueOf(kitEntry.getKey());
                KitItem kitItem = kit.getKitItem(kitEntry.getValue());

                //Set armor
                for (KitItem defaultKit : kit.getKitItems()) {
                    if (defaultKit.getKitItemType().equals(KitItemType.HELMET)) {
                        player.getInventory().setHelmet(defaultKit.getItemStack());
                    } else if (defaultKit.getKitItemType().equals(KitItemType.CHESTPLATE)) {
                        player.getInventory().setChestplate(defaultKit.getItemStack());
                    } else if (defaultKit.getKitItemType().equals(KitItemType.LEGGINGS)) {
                        player.getInventory().setLeggings(defaultKit.getItemStack());
                    } else if (defaultKit.getKitItemType().equals(KitItemType.BOOTS)) {
                        player.getInventory().setBoots(defaultKit.getItemStack());
                    }
                }
                //Set weapons
                player.getInventory().setItem(slot, kitItem.getItemStack());
            }
        } else {
            int slot = 0;
            for (KitItem kitItem : kit.getKitItems()) {
                if (kitItem.getKitItemType().equals(KitItemType.HELMET)) {
                    player.getInventory().setHelmet(kitItem.getItemStack());
                } else if (kitItem.getKitItemType().equals(KitItemType.CHESTPLATE)) {
                    player.getInventory().setChestplate(kitItem.getItemStack());
                } else if (kitItem.getKitItemType().equals(KitItemType.LEGGINGS)) {
                    player.getInventory().setLeggings(kitItem.getItemStack());
                } else if (kitItem.getKitItemType().equals(KitItemType.BOOTS)) {
                    player.getInventory().setBoots(kitItem.getItemStack());
                } else {
                    player.getInventory().setItem(slot, kitItem.getItemStack());
                    slot++;
                }
            }
        }
    }

    public void setKit(final String kitName) {
        Kit kit = GameTemplate.getInstance().getKitManager().getKit(kitName);
        if (kit != null) {
            setKit(kit);
        }
    }

    public void setKit(final int kitID) {
        Kit kit = GameTemplate.getInstance().getKitManager().getKit(kitID);
        if (kit != null) {
            setKit(kit);
        }
    }

    //Achievements
    public void addAchievement(String achievementName) {
        try {
            Achievement achievement = GameTemplate.getInstance().getAchievementManager().getAchievement(achievementName);

            if (achievement != null) {
                if (!hasAchievement(achievementName)) {
                    SolvedAchievement solvedAchievement = new SolvedAchievement(System.currentTimeMillis() / 1000, achievementName);
                    ChatColor gamemodeColor = GameTemplate.getInstance().getGamemode().getColor();
                    addCoins(achievement.getReward());
                    GameTemplate.getInstance().getMessager().sendSimple(player,
                            "§8§kasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfsadfsadfasdfasdf\n" +
                                    "§aDu hast das folgendes Achievement freigeschaltet!\n" +
                                    "\n" +
                                    "§7Achievement: " + gamemodeColor + "§o" + achievementName + "\n" +
                                    "§7Beschreibung: " + gamemodeColor + "§o" + achievement.getDescription() + "\n" +
                                    "§7Coins:" + gamemodeColor + "§o" + achievement.getReward() + "\n" +
                                    "§8§kasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfsadfsadfasdfasdf"
                    );

                    saveData();

                    //Call Event
                    Bukkit.getPluginManager().callEvent(new GameAchievementEvent(getCorePlayer().getUuid(), solvedAchievement));
                }
            } else {
                throw new GameSystemException("Cannot find achievement with the name " + achievementName);
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }
    }

    public void addAchievements(String... achievementName) {
        for (String name : achievementName) {
            addAchievement(name);
        }
    }

    public void removeAchievement(final String achievementName) {
        if (hasAchievement(achievementName)) {
            SolvedAchievement solvedAchievement = getAchievement(achievementName);
            solvedAchievements.remove(solvedAchievement);
            saveData();
        }
    }

    public void removeAchievements(final String... achievements) {
        for (String achievementName : achievements) {
            removeAchievement(achievementName);
        }
    }

    public SolvedAchievement getAchievement(final String achievementName) {
        for (SolvedAchievement solvedAchievement : solvedAchievements) {
            if (solvedAchievement.getAchievementName().equalsIgnoreCase(achievementName)) {
                return solvedAchievement;
            }
        }

        return null;
    }

    public boolean hasAchievement(String achievementName) {
        for (SolvedAchievement solvedAchievement : solvedAchievements) {
            if (solvedAchievement.getAchievementName().equalsIgnoreCase(achievementName)) {
                return true;
            }
        }

        return false;
    }

    //Ingame methods
    public void setTeam(Team team) {
        this.team = team;
        addTeamSize(1);
        GameTemplate.getInstance().getChats().get(team).add(player);
        GameTemplate.getInstance().getTeams().put(player.getUniqueId(), team);
        GameTemplate.getInstance().sendConsoleMessage("Put the player `" + name + "` in team `" + team + "`");
    }

    public void updateTeamAlive(boolean var) {
        team.setBedAlive(var);
    }

    public void setTeamSize(int size) {
        if (team != Team.ERROR) {
            if (size > -1) {
                team.setValue(size);
                GameTemplate.getInstance().sendConsoleMessage("set new size `" + size + "` for team `" + team + "`");
            }
        }
    }

    public void addTeamSize(final int size) {
        if (team != Team.ERROR) {
            GameTemplate.getInstance().sendConsoleMessage("add size `" + size + "` to team `" + team + "`");
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
                GameTemplate.getInstance().sendConsoleMessage("§cCan not remove `" + size + "` from team, because the team size is smaller than 0");
            } else if (team.getValue() == 1) {
                team.setValue(0);
                GameTemplate.getInstance().sendConsoleMessage("Set team size to 0");
            } else if (team.getValue() >= size) {
                int var = team.getValue() - size;
                team.setValue(var);
                GameTemplate.getInstance().sendConsoleMessage("Remove `" + size + "` from team `" + team + "`");
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
        GameTemplate.getInstance().sendConsoleMessage("Add 1 roundKill to player `" + name + "`");
    }

    public void addRoundKill(final int var) {
        this.roundKills = this.roundKills + var;
        GameTemplate.getInstance().sendConsoleMessage("Add `" + var + "` roundKills to player `" + name + "`");
    }

    public void addRoundDeath() {
        this.roundDeaths = this.roundDeaths + 1;
        GameTemplate.getInstance().sendConsoleMessage("Add 1 roundDeath to player `" + name + "`");
    }

    public void addRoundDeath(final int var) {
        this.roundDeaths = this.roundDeaths + var;
        GameTemplate.getInstance().sendConsoleMessage("Add `" + var + "` roundDeaths to player `" + name + "`");
    }

    public void addGoal() {
        this.roundGoals = this.roundGoals + 1;
        GameTemplate.getInstance().sendConsoleMessage("Add 1 destroyed goals to player `" + name + "`");
    }

    public void addGoals(final int var) {
        this.roundGoals = this.roundGoals + var;
        GameTemplate.getInstance().sendConsoleMessage("Add `" + var + "` destroyed goals to player `" + name + "`");
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
            GameTemplate.getInstance().sendConsoleMessage("Remove player `" + name + "` from game");
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
            GameTemplate.getInstance().sendConsoleMessage("Remove player `" + name + "` from TeamSelection");
            if (team != Team.ERROR) {
                removeTeamSize(1);

                GameTemplate.getInstance().getTeamManager().getTeamStageHandler().removePlayerFromStage(this);
                GameTemplate.getInstance().getChats().get(team).remove(player);
                GameTemplate.getInstance().getTeams().remove(player.getUniqueId());
            }
        }
    }

    public void destroy() {
        GameTemplate.getInstance().sendConsoleMessage("Destroy GamePlayer `" + name + "`");
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
    }
}
