package eu.mcone.gameapi.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamDefinition;
import eu.mcone.gameapi.team.TeamManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamInventory extends CoreInventory {

    private final GamePlugin gamePlugin;
    private final TeamManager teamManager;

    //TODO: Add team stage integration
    public TeamInventory(Player player, TeamManager teamManager) {
        super("§8» §c§oTeamauswahl", player, InventorySlot.ROW_4, InventoryOption.FILL_EMPTY_SLOTS);
        gamePlugin = GamePlugin.getGamePlugin();
        this.teamManager = teamManager;

        if (gamePlugin.hasModule(Module.TEAM_MANAGER)) {
            update();
            openInventory();
        } else {
            gamePlugin.getMessenger().send(player, "§cDu kannst das Teamvoting Inventory nicht benutzen da das Team Manager modul nicht aktiviert wurde!");
        }
    }

    private void update() {
        int slot = InventorySlot.ROW_1_SLOT_2;
        for (Team team : teamManager.getTeams()) {
            setItem(getPlace(slot), new ItemBuilder(Material.BED, team.getSize()).displayName(team.getPrefix()).lore(getPlayers(team)).create(), e -> {
                Player p = (Player) e.getWhoClicked();
                GamePlayer gp = gamePlugin.getGamePlayer(p.getUniqueId());

                if (gp.getTeam() != null && gp.getTeam().equals(team)) {
                    gamePlugin.getMessenger().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.alreadyJoined"));
                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                } else {
                    if (team.getSize() < teamManager.getPlayersPerTeam()) {
                        gp.setTeam(team);
                        gamePlugin.getMessenger().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.join", CoreSystem.getInstance().getGlobalCorePlayer(p.getUniqueId())).replace("%team%", team.getPrefix()));
                        update();
                        p.playSound(p.getLocation(), Sound.HORSE_ARMOR, 1, 1);
                        p.updateInventory();
                    } else {
                        gamePlugin.getMessenger().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.maxSize").replace("%max%", Integer.toString(teamManager.getPlayersPerTeam())));
                        p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                    }
                }
            });
        }

    }

    private int getPlace(double slot) {
        if (teamManager.isUseCustomTeams()) {
            double row = slot / 6;
            if (slot % 6 == row) {
                slot++;
            } else {
                slot += (9 * row) + 2;
            }

            return (int) slot;
        } else {
            Team team = teamManager.getTeam((int) slot);
            if (teamManager.getTeams().size() == 2) {
                if (team.getName().equals(TeamDefinition.RED.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_3;
                } else if (team.getName().equals(TeamDefinition.BLUE.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_7;
                }
            } else if (teamManager.getTeams().size() == 4) {
                if (team.getName().equals(TeamDefinition.RED.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_2;
                } else if (team.getName().equals(TeamDefinition.BLUE.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_4;
                } else if (team.getName().equals(TeamDefinition.YELLOW.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_6;
                } else if (team.getName().equals(TeamDefinition.GREEN.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_8;
                }
            } else if (teamManager.getTeams().size() == 8) {
                if (team.getName().equals(TeamDefinition.RED.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_1;
                } else if (team.getName().equals(TeamDefinition.BLUE.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_2;
                } else if (team.getName().equals(TeamDefinition.YELLOW.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_3;
                } else if (team.getName().equals(TeamDefinition.GREEN.getName())) {
                    slot = InventorySlot.ROW_2_SLOT_4;
                } else if (team.getName().equals(TeamDefinition.ORANGE.getName())) {
                    slot = InventorySlot.ROW_1_SLOT_5;
                } else if (team.getName().equals(TeamDefinition.AQUA.getName())) {
                    slot = InventorySlot.ROW_3_SLOT_6;
                } else if (team.getName().equals(TeamDefinition.WHITE.getName())) {
                    slot = InventorySlot.ROW_1_SLOT_7;
                } else if (team.getName().equals(TeamDefinition.PURPLE.getName())) {
                    slot = InventorySlot.ROW_3_SLOT_8;
                }
            }
        }

        return (int) slot;
    }

    private String[] getPlayers(Team team) {
        List<String> lore = new ArrayList<>();
        lore.add("§7§oSpieler");
        if (!team.getPlayers().isEmpty()) {
            for (Player player : team.getPlayers()) {
                lore.add("§8» §7" + player.getName());
            }
        } else {
            return new String[]{"§2Offen"};
        }

        return lore.toArray(new String[0]);
    }
}
