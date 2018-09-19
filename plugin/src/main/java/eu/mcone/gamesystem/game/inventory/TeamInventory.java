package eu.mcone.gamesystem.game.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.util.ItemBuilder;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.game.manager.team.TeamStage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamInventory extends CoreInventory {

    private TeamStage teamStage;

    public TeamInventory(Player player, TeamStage teamStage) {
        super("§8» §c§oTeamauswahl", player, InventorySlot.ROW_3, Option.FILL_EMPTY_SLOTS);
        this.teamStage = teamStage;

        int i = 1;
        for (Team team : Team.values()) {
            if (i <= GameTemplate.getInstance().getNumberOfTeams()) {
                setItem(getPlace(team), new ItemBuilder(Material.BED, team.getValue()).displayName(team.getPrefix()).lore(new String[]{
                        "§7§oSpieler",
                        getPlayers(team)
                }).create(), this::registerFunction);
                i++;
            } else {
                break;
            }
        }

        openInventory();
    }

    private void registerFunction(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        GamePlayer gp = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());

        for (Team team : Team.values()) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(team.getPrefix())) {
                if (gp.getTeam().equals(team)) {
                    GameTemplate.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.already"));
                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                    break;
                } else {
                    if (team.getValue() <= GameTemplate.getInstance().getPlayerPreTeam()) {
                        if (gp.getTeam() == Team.ERROR || gp.getTeam() == null) {
                            gp.setTeam(team);
                            teamStage.setPlayer(gp);
                        } else {
                            gp.removeFromTeamSelection();
                            teamStage.removePlayer(gp);
                            gp.setTeam(team);
                            teamStage.setPlayer(gp);
                        }

                        GameTemplate.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.join", CoreSystem.getInstance().getGlobalCorePlayer(p.getUniqueId())).replace("%team%", team.getPrefix()));
                        update();
                        p.playSound(p.getLocation(), Sound.HORSE_ARMOR, 1, 1);
                        p.updateInventory();
                        break;
                    } else {
                        GameTemplate.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.maxSize").replace("%max%", Integer.toString(GameTemplate.getInstance().getPlayerPreTeam())));
                        p.playSound(p.getLocation(), Sound.ANVIL_BREAK,1,1);
                        break;
                    }
                }
            }
        }
    }

    private void update() {
        int i = 1;
        for (Team team : Team.values()) {
            if (i <= GameTemplate.getInstance().getNumberOfTeams()) {
                setItem(getPlace(team), new ItemBuilder(Material.BED, team.getValue()).displayName(team.getPrefix()).lore(new String[]{
                        "§7§oSpieler",
                        getPlayers(team),
                        ""
                }).create(), this::registerFunction);
                i++;
            } else {
                break;
            }
        }
    }

    private int getPlace(final Team team) {
        if (GameTemplate.getInstance().getNumberOfTeams() == 2) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_3;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_7;
            }
        } else if (GameTemplate.getInstance().getNumberOfTeams() == 4) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.equals(Team.AQUA)) {
                return InventorySlot.ROW_2_SLOT_6;
            } else if (team.equals(Team.GREEN)) {
                return InventorySlot.ROW_2_SLOT_8;
            }
        } else if (GameTemplate.getInstance().getNumberOfTeams() == 8) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_1;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.equals(Team.AQUA)) {
                return InventorySlot.ROW_2_SLOT_3;
            } else if (team.equals(Team.GREEN)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.equals(Team.YELLOW)) {
                return InventorySlot.ROW_2_SLOT_5;
            } else if (team.equals(Team.GOLD)) {
                return InventorySlot.ROW_2_SLOT_6;
            } else if (team.equals(Team.GRAY)) {
                return InventorySlot.ROW_2_SLOT_7;
            } else if (team.equals(Team.WHITE)) {
                return InventorySlot.ROW_3_SLOT_8;
            }
        } else if (GameTemplate.getInstance().getNumberOfTeams() == 16) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_1;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.equals(Team.AQUA)) {
                return InventorySlot.ROW_2_SLOT_3;
            } else if (team.equals(Team.GREEN)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.equals(Team.YELLOW)) {
                return InventorySlot.ROW_2_SLOT_5;
            } else if (team.equals(Team.GOLD)) {
                return InventorySlot.ROW_2_SLOT_6;
            } else if (team.equals(Team.GRAY)) {
                return InventorySlot.ROW_2_SLOT_7;
            } else if (team.equals(Team.WHITE)) {
                return InventorySlot.ROW_3_SLOT_8;
            } else if (team.equals(Team.BLACK)) {
                return InventorySlot.ROW_3_SLOT_1;
            } else if (team.equals(Team.LIGHT_PURPLE)) {
                return InventorySlot.ROW_3_SLOT_2;
            } else if (team.equals(Team.DARK_BLUE)) {
                return InventorySlot.ROW_3_SLOT_3;
            } else if (team.equals(Team.DARK_GREEN)) {
                return InventorySlot.ROW_3_SLOT_4;
            } else if (team.equals(Team.DARK_AQUA)) {
                return InventorySlot.ROW_3_SLOT_5;
            } else if (team.equals(Team.DARK_RED)) {
                return InventorySlot.ROW_3_SLOT_6;
            } else if (team.equals(Team.DARK_PURPLE)) {
                return InventorySlot.ROW_3_SLOT_7;
            } else if (team.equals(Team.DARK_GRAY)) {
                return InventorySlot.ROW_3_SLOT_8;
            }
        }
        return 0;
    }

    private String getPlayers(Team team) {
        StringBuilder sb = new StringBuilder();
        if (GameTemplate.getInstance().getChats().get(team) != null) {
            for (Player player : GameTemplate.getInstance().getChats().get(team)) {
                sb.append(System.getProperty("line.separator")).append("§8» §7").append(player.getName());
            }
        } else {
            return "§2Offen";
        }

        return sb.toString();
    }
}
