package eu.mcone.gamesystem.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.util.ItemBuilder;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.manager.TeamManager;
import eu.mcone.gamesystem.api.manager.team.Team;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamInventory extends CoreInventory {

    private TeamManager teamManager;

    public TeamInventory(Player player, TeamManager teamManager) {
        super("§8» §f§fTeam §8| §7Wähle dein Team", player, InventorySlot.ROW_3, Option.FILL_EMPTY_SLOTS);

        this.teamManager = teamManager;

        int i = 1;
        for (Team team : Team.values()) {
            if (i <= teamManager.getNumberOfTeams()) {
                setItem(getPlace(team), new ItemBuilder(Material.WOOL, team.getValue(), getSubID(team)).displayName(team.getPrefix()).lore(new String[]{
                        "§f§oSpieler",
                        getPlayers(team),
                        ""
                }).create(), this::registerFunction);
                i++;
            }
        }

        openInventory();
    }

    private void registerFunction(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        for (Team team : Team.values()) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(team.getPrefix())) {
                if (teamManager.getTeam(p.getUniqueId()).equals(team)) {
                    GameSystemAPI.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("bedwars.team.alredy"));
                } else {
                    if (teamManager.getTeam(p.getUniqueId()) != Team.ERROR) {
                        remove(p, team);
                    }

                    add(p, team);
                    teamManager.addSize(team, 1);
                    GameSystemAPI.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("bedwars.team.join").replace("%team%", team.getPrefix()));
                    p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1, 1);
                    p.updateInventory();
                }
            }
        }
    }

    private void update() {
        int i = 1;
        for (Team team : Team.values()) {
            if (i <= teamManager.getNumberOfTeams()) {
                setItem(getPlace(team), new ItemBuilder(Material.WOOL, team.getValue(), getSubID(team)).displayName(team.getPrefix()).lore(new String[]{
                        "§f§oSpieler",
                        getPlayers(team),
                        ""
                }).create(), this::registerFunction);
                i++;
            }
        }
    }

    private void add(Player p, Team team) {
        if (team.getValue() == 0
                || team.getValue() < teamManager.getPlayersPerTeam()
                && team.getValue() != teamManager.getPlayersPerTeam()) {
            teamManager.getTeams().put(p.getUniqueId(), team.getString());
            teamManager.addSize(team, 1);

            if (team.equals(Team.RED)) {
                teamManager.getRed().add(p);
            } else if (team.equals(Team.BLUE)) {
                teamManager.getBlue().add(p);
            } else if (team.equals(Team.VIOLET)) {
                teamManager.getViolet().add(p);
            } else if (team.equals(Team.YELLOW)) {
                teamManager.getYellow().add(p);
            } else if (team.equals(Team.GREEN)) {
                teamManager.getGreen().add(p);
            } else if (team.equals(Team.GRAY)) {
                teamManager.getGray().add(p);
            } else if (team.equals(Team.WHITE)) {
                teamManager.getWhite().add(p);
            } else if (team.equals(Team.BLACK)) {
                teamManager.getBlack().add(p);
            }
        } else {
            GameSystemAPI.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("bedwars.team.full"));
        }
    }

    private void remove(Player p, Team team) {
        teamManager.getTeams().remove(p.getUniqueId());
        teamManager.removeSize(team, 1);

        if (team.equals(Team.RED)) {
            teamManager.getRed().remove(p);
        } else if (team.equals(Team.BLUE)) {
            teamManager.getBlue().remove(p);
        } else if (team.equals(Team.VIOLET)) {
            teamManager.getViolet().remove(p);
        } else if (team.equals(Team.YELLOW)) {
            teamManager.getYellow().remove(p);
        } else if (team.equals(Team.GREEN)) {
            teamManager.getGreen().remove(p);
        } else if (team.equals(Team.GRAY)) {
            teamManager.getGray().remove(p);
        } else if (team.equals(Team.WHITE)) {
            teamManager.getWhite().remove(p);
        } else if (team.equals(Team.BLACK)) {
            teamManager.getBlack().remove(p);
        }
    }

    private int getSubID(final Team team) {
        if (team.equals(Team.RED)) {
            return 14;
        } else if (team.equals(Team.BLUE)) {
            return 3;
        } else if (team.equals(Team.VIOLET)) {
            return 10;
        } else if (team.equals(Team.YELLOW)) {
            return 4;
        } else if (team.equals(Team.GREEN)) {
            return 5;
        } else if (team.equals(Team.GRAY)) {
            return 8;
        } else if (team.equals(Team.WHITE)) {
            return 0;
        } else if (team.equals(Team.BLACK)) {
            return 15;
        }
        return 0;
    }

    private int getPlace(final Team team) {
        if (teamManager.getNumberOfTeams() == 2) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_6;
            }
        } else if (teamManager.getNumberOfTeams() == 4) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.equals(Team.VIOLET)) {
                return InventorySlot.ROW_2_SLOT_6;
            } else if (team.equals(Team.YELLOW)) {
                return InventorySlot.ROW_2_SLOT_8;
            }
        } else if (teamManager.getNumberOfTeams() == 8) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_1;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.equals(Team.VIOLET)) {
                return InventorySlot.ROW_2_SLOT_3;
            } else if (team.equals(Team.YELLOW)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.equals(Team.GREEN)) {
                return InventorySlot.ROW_2_SLOT_5;
            } else if (team.equals(Team.GRAY)) {
                return InventorySlot.ROW_2_SLOT_6;
            } else if (team.equals(Team.WHITE)) {
                return InventorySlot.ROW_2_SLOT_7;
            } else if (team.equals(Team.BLACK)) {
                return InventorySlot.ROW_2_SLOT_8;
            }
        }
        return 0;
    }

    private String getPlayers(Team team) {
        StringBuilder sb = new StringBuilder();
        if (teamManager.getChat(team) != null) {
            for (Player player : teamManager.getChat(team)) {
                String str = "§8» §7" + player.getName() + "\n";
                sb.append(str);
            }
        } else {
            return "§2Offen";
        }

        return sb.toString();
    }
}
