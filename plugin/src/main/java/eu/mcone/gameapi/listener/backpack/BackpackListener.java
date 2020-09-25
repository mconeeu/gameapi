package eu.mcone.gameapi.listener.backpack;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.TeamManager;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
public class BackpackListener implements Listener {

    private final GameBackpackManager manager;

    @EventHandler
    public void on(InventoryCloseEvent e) {
        /*if (e.getPlayer() instanceof Player) {
            if (e.getInventory().getTitle().equals("§8» §3§lWähle aus") || e.getInventory().getTitle().equals("§8» §e§lTrade")) {
                manager.cancelTraid((Player) e.getPlayer());
            }
        }*/
    }

    @EventHandler
    public void onJoin(GamePlayerLoadedEvent e) {

        if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
            if (GamePlugin.getGamePlugin().hasOption(Option.NORMAL_HOTBAR_WAITING_ITEMS)) {

                GamePlayer gp = e.getPlayer();
                CorePlayer cp = e.getCorePlayer();
                Player player = cp.bukkit();

                /* main stuff */
                player.setGameMode(GameMode.SURVIVAL);

                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setLevel(0);
                player.setExp(0);
                player.removePotionEffect(PotionEffectType.SLOW);

                /* Quit Item */
                player.getInventory().setItem(8, LobbyGameState.QUIT_ITEM);

                /* Kit Chooser Item */
                if (GamePlugin.getGamePlugin().hasOption(Option.KIT_MANAGER_APPLY_KITS_ONCE) ||
                        GamePlugin.getGamePlugin().hasOption(Option.KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME) ||
                        GamePlugin.getGamePlugin().hasOption(Option.KIT_MANAGER_CLEAR_INVENTORY_ON_KIT_SET)) {
                    player.getInventory().setItem(GamePlugin.getGamePlugin().getKitManager().getKitsChooserItemSlot(), GamePlugin.getGamePlugin().getKitManager().getKitChooserItem());
                }

                /* Team Manager Item */
                player.getInventory().setItem(GamePlugin.getGamePlugin().getTeamManager().getTeamChooseItemSlot(), GamePlugin.getGamePlugin().getTeamManager().getKitsChooseItem());

                /* BackPack Manager Item */
                player.getInventory().setItem(GamePlugin.getGamePlugin().getBackpackManager().getBackpackSlot(), GamePlugin.getGamePlugin().getBackpackManager().getBackPackItem());

                /* Set last BackPackItem */
                gp.getLastUsedBackPackItem();

                /* Set rank boots */
                if (manager.isUseRankBoots()) {
                    manager.setRankBoots(e.getPlayer().bukkit());
                }
            }
        }
    }
}
