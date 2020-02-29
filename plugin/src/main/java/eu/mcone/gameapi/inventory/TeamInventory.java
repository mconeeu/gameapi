package eu.mcone.gameapi.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Modules;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamEnum;
import eu.mcone.gameapi.team.TeamManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamInventory extends CoreInventory {

    private GamePlugin gamePlugin;
    private TeamManager teamManager;

    //TODO: Add team stage integration
    public TeamInventory(Player player, TeamManager teamManager) {
        super("§8» §c§oTeamauswahl", player, InventorySlot.ROW_4, InventoryOption.FILL_EMPTY_SLOTS);
        gamePlugin = GamePlugin.getPlugin();
        this.teamManager = teamManager;

        if (gamePlugin.getModules().contains(Modules.TEAM_MANAGER)) {
            int i = 1;
            for (Team team : teamManager.getTeams()) {
                setItem(getPlace(team), new ItemBuilder(Material.BED, team.getSize()).displayName(team.getTeamEnum().getPrefix()).lore(new String[]{
                        "§7§oSpieler",
                        getPlayers(team)
                }).create(), this::registerFunction);
                i++;
            }

            openInventory();
        } else {
            gamePlugin.getMessager().send(player, "§cDu kannst das Teamvoting Inventory nicht benutzen da das Team Manager modul nicht aktiviert wurde!");
        }
    }

    private void registerFunction(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        GamePlayer gp = gamePlugin.getGamePlayer(p.getUniqueId());

        for (Team team : teamManager.getTeams()) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(team.getTeamEnum().getPrefix())) {
                if (gp.getTeam().getTeam().equalsIgnoreCase(team.getTeamEnum().getTeam())) {
                    gamePlugin.getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.already"));
                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                    break;
                } else {
                    if (team.getSize() <= teamManager.getPlayersPerTeam()) {
                        if (gp.getTeam() == TeamEnum.ERROR || gp.getTeam() == null) {
                            gp.setTeam(team);

                            //GameTemplate.getInstance().getTeamManager().getTeamStageHandler().getTeamStage(team).addPlayerToStage(gp);
                        } else {
                            //Removes the Player from the Stage, Chat and Team
                            //gp.removeFromTeamSelection();
                            gp.setTeam(team);

                            //GameTemplate.getInstance().getTeamManager().getTeamStageHandler().getTeamStage(team).addPlayerToStage(gp);
                        }

                        gamePlugin.getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.join", CoreSystem.getInstance().getGlobalCorePlayer(p.getUniqueId())).replace("%team%", team.getTeamEnum().getPrefix()));
                        update();
                        p.playSound(p.getLocation(), Sound.HORSE_ARMOR, 1, 1);
                        p.updateInventory();
                        break;
                    } else {
                        gamePlugin.getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.maxSize").replace("%max%", Integer.toString(teamManager.getPlayersPerTeam())));
                        p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                        break;
                    }
                }
            }
        }
    }

    private void update() {
        int i = 1;
        for (Team team : teamManager.getTeams()) {
            setItem(getPlace(team), new ItemBuilder(Material.BED, team.getSize()).displayName(team.getTeamEnum().getPrefix()).lore(new String[]{
                    "§7§oSpieler",
                    getPlayers(team),
                    ""
            }).create(), this::registerFunction);
            i++;
        }
    }

    private int getPlace(final Team team) {
        if (teamManager.getTeams().size() == 2) {
            if (team.getTeamEnum().equals(TeamEnum.RED)) {
                return InventorySlot.ROW_2_SLOT_3;
            } else if (team.getTeamEnum().equals(TeamEnum.BLUE)) {
                return InventorySlot.ROW_2_SLOT_7;
            }
        } else if (teamManager.getTeams().size() == 4) {
            if (team.getTeamEnum().equals(TeamEnum.RED)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.getTeamEnum().equals(TeamEnum.BLUE)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.getTeamEnum().equals(TeamEnum.YELLOW)) {
                return InventorySlot.ROW_2_SLOT_6;
            } else if (team.getTeamEnum().equals(TeamEnum.GREEN)) {
                return InventorySlot.ROW_2_SLOT_8;
            }
        } else if (teamManager.getTeams().size() == 8) {
            if (team.getTeamEnum().equals(TeamEnum.RED)) {
                return InventorySlot.ROW_2_SLOT_1;
            } else if (team.getTeamEnum().equals(TeamEnum.BLUE)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.getTeamEnum().equals(TeamEnum.YELLOW)) {
                return InventorySlot.ROW_2_SLOT_3;
            } else if (team.getTeamEnum().equals(TeamEnum.GREEN)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.getTeamEnum().equals(TeamEnum.ORANGE)) {
                return InventorySlot.ROW_1_SLOT_5;
            } else if (team.getTeamEnum().equals(TeamEnum.AQUA)) {
                return InventorySlot.ROW_3_SLOT_6;
            } else if (team.getTeamEnum().equals(TeamEnum.WHITE)) {
                return InventorySlot.ROW_1_SLOT_7;
            } else if (team.getTeamEnum().equals(TeamEnum.PURPLE)) {
                return InventorySlot.ROW_3_SLOT_8;
            }
        }

        return 0;
    }

    private String getPlayers(Team team) {
        StringBuilder sb = new StringBuilder();
        if (!team.getPlayers().isEmpty()) {
            for (Player player : team.getPlayers()) {
                sb.append(System.getProperty("line.separator")).append("§8» §7").append(player.getName());
            }
        } else {
            return "§2Offen";
        }

        return sb.toString();
    }
}
