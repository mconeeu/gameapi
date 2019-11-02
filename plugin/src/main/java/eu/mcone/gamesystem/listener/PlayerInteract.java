package eu.mcone.gamesystem.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.game.command.GameCommand;
import eu.mcone.gamesystem.game.inventory.SpectatorInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (GameTemplate.getInstance() != null) {
            Player player = e.getPlayer();
            CoreWorld coreWorld = CoreSystem.getInstance().getWorldManager().getWorld(player.getWorld());
            if (e.getItem() != null) {
                if (e.getItem().equals(SpectatorInventory.NAVIGATOR)) {
                    new SpectatorInventory(e.getPlayer());
                }
            } else if (GameCommand.getStatsWall().contains(player)) {
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (coreWorld.getLocation("statsPos1") != null) {
                        GameSystem.getInstance().getMessager().send(player, "§cDu hast diese Location bereits gesetzt, benutze §f/game set stats §cum das Setup zu verlassen!");
                    } else {
                        GameSystem.getInstance().getMessager().send(player, "§aStatswand Position 1 gesetzt!");
                    }

                    coreWorld.setLocation("statsPos1", e.getClickedBlock().getLocation()).save();
                } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (coreWorld.getLocation("statsPos2") != null) {
                        GameSystem.getInstance().getMessager().send(player, "§cDu hast diese Location bereits gesetzt, benutze §f/game set stats §cum das Setup zu verlassen!");
                    } else {
                        GameSystem.getInstance().getMessager().send(player, "§aStatswand Position 2 gesetzt!");
                    }

                    coreWorld.setLocation("statsPos2", e.getClickedBlock().getLocation()).save();
                    CoreSystem.getInstance().getWorldManager().getWorld(player.getWorld()).setLocation("statsPos2", e.getClickedBlock().getLocation());
                }
            }
        }
    }
}
