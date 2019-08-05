/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.lobby.backpack.BackpackInventory;
import eu.mcone.gamesystem.game.achivements.AchievementManager;
import eu.mcone.gamesystem.game.command.GameCommand;
import eu.mcone.gamesystem.game.manager.map.MapManager;
import eu.mcone.gamesystem.game.manager.team.TeamManager;
import eu.mcone.gamesystem.listener.*;
import eu.mcone.gamesystem.lobby.backpack.*;
import eu.mcone.gamesystem.lobby.cards.ItemCardManager;
import eu.mcone.gamesystem.lobby.manager.TrailManager;
import eu.mcone.gamesystem.player.DamageLogger;
import eu.mcone.lobby.api.enums.Category;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameSystem extends GameSystemAPI {

    @Getter
    private static GameSystem system;
    @Getter
    private DamageLogger damageLogger;

    @Override
    public void onEnable() {
        setInstance(this);
        system = this;

        CoreSystem.getInstance().getTranslationManager().loadCategories(this);

        damageLogger = new DamageLogger();

        if (GameTemplate.getInstance() != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendConsoleMessage("§aLoaded Player " + player.getName());
                //Creates a new GamePlayer object with the player from the collection
                PlayerJoin.loadPlayer(player);
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ITEM_CARDS)
                    || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                GameTemplate.getInstance().setItemCardManager(new ItemCardManager());
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_BACKPACK)
                    || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                GameTemplate.getInstance().setTrailManager(new TrailManager());

                BackpackInventory.registerBackpackInventory(Category.TRAIL, TrailInventory.class);
                BackpackInventory.registerBackpackInventory(Category.GADGET, GadgetInventory.class);
                BackpackInventory.registerBackpackInventory(Category.HAT, HatsInventory.class);
                BackpackInventory.registerBackpackInventory(Category.OUTFITS, OutfitInventory.class);
                BackpackInventory.registerBackpackInventory(Category.ARMOR, ArmorInventory.class);
                BackpackInventory.registerBackpackInventory(Category.MATERIAL, MaterialInventory.class);
                BackpackInventory.registerBackpackInventory(Category.EXCLUSIVE, ExclusiveInventory.class);
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_MAP_MANAGER)
                    || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                sendConsoleMessage("§aCreate MapManager...");
                GameTemplate.getInstance().setMapManager(new MapManager(this));
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_TEAM_MANAGER)
                    || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                sendConsoleMessage("§aCreate TeamManager...");
                //Create TeamManager instance
                GameTemplate.getInstance().setTeamManager(new TeamManager());
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ACHIEVEMENTS)
                    || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                sendConsoleMessage("§aCreate AchievementManager...");
                //Create AchievementManager instance
                GameTemplate.getInstance().setAchievementManager(new AchievementManager());
                //Stores all achievements from the database.
                GameTemplate.getInstance().getAchievementManager().loadAchievements();
            }
        }

        sendConsoleMessage("§aRegistering Events...");
        registerEvents(
                new EntityDamageByEntity(),
                new EntityDamage(),
                new InventoryMoveItem(),
                new PlayerDropItem(),
                new PlayerInteract(),
                new PlayerAchievementAwarded(),
                new WeatherChange(),
                new AsyncPlayerChat(),
                new PlayerJoin(),
                new PlayerQuit(),
                new ServerListPing()
        );

        sendConsoleMessage("§aRegistering Commands...");
        registerCommands(
                new GameCommand()
        );

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("§cPlugin disabled!");
    }
}
