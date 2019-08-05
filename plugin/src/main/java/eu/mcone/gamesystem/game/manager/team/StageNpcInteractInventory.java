/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class StageNpcInteractInventory extends CoreInventory {

    public StageNpcInteractInventory(final Player player, final GamePlayer stageGamePlayer) {
        super("§8» " + player.getName(), player, InventorySlot.ROW_5, InventoryOption.FILL_EMPTY_SLOTS);

        setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.BED)
                .displayName("§8» " + stageGamePlayer.getTeam().getChatColor().toString() + stageGamePlayer.getName() + " §7Stats")
                .lore(
                        "§8» §7§oRanking Platz: §f§l" + stageGamePlayer.getStats().getUserRanking(),
                        "§8» §7§oKD: §f§l" + stageGamePlayer.getStats().getKD(),
                        "§8» §7§oKills: §f§l" + stageGamePlayer.getStats().getKill(),
                        "§8» §7§oTode: §f§l" + stageGamePlayer.getStats().getDeath(),
                        "§8» §7§oGewonnen: §f§l" + stageGamePlayer.getStats().getWin(),
                        "§8» §7§oVerloren: §f§l" + stageGamePlayer.getStats().getLose()
                ).create());

        if (!stageGamePlayer.getName().equalsIgnoreCase(player.getName())) {
            CoreSystem.getInstance().getChannelHandler().createGetRequest(player, friendString -> {
                boolean isFriend = false;
                for (String friend : friendString.split(",")) {
                    if (friend.contains(stageGamePlayer.getName())) isFriend = true;
                }
                if (isFriend) {
                    ItemStack skullItem = new Skull(stageGamePlayer.getName(), 1).getItemStack();
                    skullItem.getItemMeta().setDisplayName("§7Als §1§lFreund §7hinzufügen");
                    skullItem.getItemMeta().setLore(new ArrayList<String>() {{
                        add("§7§oFüge §f§o" + stageGamePlayer.getName() + " §7§odeiner §1§lFreundelitse §7§ohinzu!");
                        add("");
                        add("§8» §f§nLinksklick §8| §7§oZur §1§lFreundliste §7§ohinzufügen");
                    }});

                    setItem(InventorySlot.ROW_4_SLOT_3, skullItem, e -> {
                        player.closeInventory();
                        CoreSystem.getInstance().getChannelHandler().createSetRequest(player, "CMD", "friend add " + stageGamePlayer.getName());
                    });
                } else {
                    setItem(InventorySlot.ROW_4_SLOT_3, new ItemBuilder(Material.BARRIER)
                            .displayName("§7Als §1§lFreund §7hinzufügen")
                            .lore("§7§oDu bist bereits mit §f§o" + stageGamePlayer.getName() + " §7§obefreundet!")
                            .create(), e -> {
                    });
                }
            }, "FRIENDS");

            setItem(InventorySlot.ROW_4_SLOT_7, new ItemBuilder(Material.CAKE, 1, 0)
                    .displayName("§7In §5§lParty §7einladen")
                    .lore(
                            "§7§oLade §f§o" + stageGamePlayer.getName() + " §7§oin eine Party ein",
                            "",
                            "§8» §f§nLinksklick §8| §7§oIn §5§lParty §7§oeinladen"
                    ).create(), e -> {

                player.closeInventory();
                CoreSystem.getInstance().getChannelHandler().createSetRequest(player, "CMD", "party invite " + stageGamePlayer.getName());
            });
        }

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        openInventory();
    }
}
