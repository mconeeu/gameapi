/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.player;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.player.Stats;
import eu.mcone.gamesystem.api.enums.Item;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.achivements.SolvedAchievement;
import eu.mcone.gamesystem.api.game.manager.kit.Kit;
import eu.mcone.gamesystem.api.game.manager.kit.sorting.CustomKit;
import eu.mcone.gamesystem.api.lobby.cards.ItemCard;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface GamePlayer {

    List<Item> getItems();

    HashSet<SolvedAchievement> getSolvedAchievements();

    Team getTeam();

    String getName();

    CorePlayer getCorePlayer();

    void setSpectator(final boolean var);

    void setPlaying(final boolean var);

    boolean isSpectator();

    boolean isPlaying();

    //Lobby methods
    void addItem(Item item);

    void removeItem(Item item);

    boolean hasItem(Item i);

    void buyItem(Player p, Item item);

    //ItemCards
    boolean hasItemCard(final String itemCardName);

    boolean hasItemCard(final ItemCard itemCard);

    ItemCard getItemCard(final String itemCardName);

    boolean hasItemCardRedeemed(final String itemCardName);

    void redeemedItemCard(final String itemCardName);

    void redeemedItemCard(final ItemCard itemCard);

    void unRedeemedItemCard(final ItemCard itemCard);

    void unRedeemedItemCard(final String itemCardName);

    List<ItemCard> getItemCards();

    void removeItemCard(final ItemCard itemCard);

    void removeItemCard(final String itemCardName);

    void addItemCard(final ItemCard itemCard);

    void addItemCard(final String itemCardName);

    List<ItemCard> getRedeemedItemCards();

    List<ItemCard> getRedeemedItemCards(Gamemode gamemode);

    void givePlayerRedeemedItemCards(Gamemode gamemode);

    //Custom kits
    void modifyInventory(final Kit kit, final Map<String, Double> sortedItems);

    CustomKit getModifiedKit(Kit kit);

    boolean isKitModified(Kit kit);

    void setKit(Kit kit);

    void setKit(final String kitName);

    void setKit(final int kitID);

    //Achievements
    void addAchievement(String achievementName);

    void addAchievements(String... achievements);

    void removeAchievement(final String achievementName);

    void removeAchievements(final String... achievements);

    SolvedAchievement getAchievement(final String achievementName);

    boolean hasAchievement(String achievementName);

    //Ingame methods
    void setTeam(final Team team);

    void updateTeamAlive(final boolean var);

    void setTeamSize(final int size);

    void addTeamSize(final int size);

    void removeTeamSize(final int size);

    int getTeamSize();

    int getPlayingSize();

    List<Player> getTeamChat();

    void setCurrentKit(Kit kit);

    void removeCurrentKit();

    boolean hasCurrentKit();

    Kit getCurrentKit();

    void removeFromGame();

    void removeFromTeamSelection();

    void destroy();

    void addCoins(int coins);

    void addRoundKill();

    void addRoundKill(int var);

    void addRoundDeath();

    void addRoundDeath(int var);

    void addGoal();

    void addGoals(int var);

    int getRoundKills();

    int getRoundDeaths();

    int getRoundCoins();

    int getRoundGoals();

    Stats getStats();

    double getRoundKD();
}
