/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.player.GamePlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SpectatorInventory extends CoreInventory {

    public final static ItemStack NAVIGATOR = new ItemBuilder(Material.COMPASS).displayName("§7Navigator").create();

    public SpectatorInventory(Player player, GamePlayerManager playerManager) {
        super("§7Spectator", player, InventorySlot.ROW_6, InventoryOption.FILL_EMPTY_SLOTS);

        int slot = 0;
        for (Player playing : playerManager.getPlayers(GamePlayerState.PLAYING)) {
            GamePlayer gamePlayer = Objects.requireNonNull(GamePlugin.getGamePlugin()).getGamePlayer(playing.getUniqueId());

            setItem(slot, new Skull(playing.getName()).setDisplayName("§7" + playing.getName()).lore(
//                    "§8» §7§oRanking Platz: §f§l" + gamePlayer.getStats().getUserRanking(),
                    "§8» §7§oKD: §f§l" + gamePlayer.getStats().getKD(),
                    "§8» §7§oKills: §f§l" + gamePlayer.getStats().getKills(),
                    "§8» §7§oTode: §f§l" + gamePlayer.getStats().getDeaths(),
                    "§8» §7§oGewonnen: §f§l" + gamePlayer.getStats().getGoals(),
                    "§8» §7§oVerloren: §f§l" + gamePlayer.getStats().getLosses()
            ).getItemStack(), e -> {
                player.teleport(gamePlayer.getCorePlayer().bukkit().getLocation());
                Sound.teleport(player);
                GamePlugin.getGamePlugin().getMessenger().send(player, "§7Du wurdest zu dem Spieler " + GamePlugin.getGamePlugin().getPluginColor() + gamePlayer.getCorePlayer().getName() + " §7teleportiert");
            });

            slot++;
        }

        openInventory();
    }
}
