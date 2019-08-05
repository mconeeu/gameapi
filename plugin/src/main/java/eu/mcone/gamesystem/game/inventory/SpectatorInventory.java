package eu.mcone.gamesystem.game.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpectatorInventory extends CoreInventory {

    public static ItemStack NAVIGATOR = new ItemBuilder(Material.COMPASS).displayName("§7Navigator").create();

    public SpectatorInventory(Player player) {
        super("§7Spectator", player, InventorySlot.ROW_6, InventoryOption.FILL_EMPTY_SLOTS);

        createInventory();
        openInventory();
    }

    private void createInventory() {
        int slot = 0;
        for (Player playing : GameTemplate.getInstance().getPlaying()) {
            GamePlayer gamePlayer = GameTemplate.getInstance().getGamePlayer(playing.getUniqueId());

            setItem(slot, new Skull(playing.getName()).setDisplayName("§7" + playing.getName()).lore(
                    "§8» §7§oRanking Platz: §f§l" + gamePlayer.getStats().getUserRanking(),
                    "§8» §7§oKD: §f§l" + gamePlayer.getStats().getKD(),
                    "§8» §7§oKills: §f§l" + gamePlayer.getStats().getKill(),
                    "§8» §7§oTode: §f§l" + gamePlayer.getStats().getDeath(),
                    "§8» §7§oGewonnen: §f§l" + gamePlayer.getStats().getWin(),
                    "§8» §7§oVerloren: §f§l" + gamePlayer.getStats().getLose()
            ).getItemStack(), e -> {
                if (gamePlayer.isPlaying()) {
                    player.teleport(gamePlayer.getCorePlayer().bukkit().getLocation());
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                    GameSystemAPI.getInstance().getMessager().send(player, "§7Du wurdest zu dem Spieler " + GameSystemAPI.getInstance().getPluginColor() + gamePlayer.getName() + " §7teleportiert");
                } else {
                    createInventory();
                    player.updateInventory();
                }
            });

            slot++;
        }
    }
}
