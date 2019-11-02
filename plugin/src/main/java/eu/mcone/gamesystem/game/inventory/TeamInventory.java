/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamInventory extends CoreInventory {

    public TeamInventory(Player player) {
        super("§8» §c§oTeamauswahl", player, InventorySlot.ROW_4, InventoryOption.FILL_EMPTY_SLOTS);

        if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_TEAM_MANAGER)
                || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
            int i = 1;
            for (Team team : Team.values()) {
                if (i <= GameTemplate.getInstance().getGameConfigAsClass().getTeams()) {
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
        } else {
            GameTemplate.getInstance().getMessager().send(player, "§cDu kannst das Teamvoting Inventory nicht benutzen da das Team Manager modul nicht aktiviert wurde!");
        }
    }

    private void registerFunction(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        GamePlayer gp = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());

        for (Team team : Team.values()) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(team.getPrefix())) {
                if (gp.getTeam().getString().equalsIgnoreCase(team.getString())) {
                    GameTemplate.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.already"));
                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                    break;
                } else {
                    if (team.getValue() <= GameTemplate.getInstance().getGameConfigAsClass().getPlayersPreTeam()) {
                        if (gp.getTeam() == Team.ERROR || gp.getTeam() == null) {
                            gp.setTeam(team);

                            GameTemplate.getInstance().getTeamManager().getTeamStageHandler().getTeamStage(team).addPlayerToStage(gp);
                        } else {
                            //Removes the Player from the Stage, Chat and Team
                            gp.removeFromTeamSelection();
                            gp.setTeam(team);

                            GameTemplate.getInstance().getTeamManager().getTeamStageHandler().getTeamStage(team).addPlayerToStage(gp);
                        }

                        GameTemplate.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.join", CoreSystem.getInstance().getGlobalCorePlayer(p.getUniqueId())).replace("%team%", team.getPrefix()));
                        update();
                        p.playSound(p.getLocation(), Sound.HORSE_ARMOR, 1, 1);
                        break;
                    } else {
                        GameTemplate.getInstance().getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.maxSize").replace("%max%", Integer.toString(GameTemplate.getInstance().getGameConfigAsClass().getPlayersPreTeam())));
                        p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                        break;
                    }
                }
            }
        }
    }

    private void update() {
        int i = 1;
        for (Team team : Team.values()) {
            if (i <= GameTemplate.getInstance().getGameConfigAsClass().getTeams()) {
                setItem(getPlace(team), new ItemBuilder(Material.BED, team.getValue()).displayName(team.getPrefix()).lore(new String[]{
                        "§7§oSpieler",
                        getPlayers(team),
                        ""
                }).create(), this::registerFunction);
                i++;

                player.updateInventory();
            } else {
                break;
            }
        }
    }

    private int getPlace(final Team team) {
        if (GameTemplate.getInstance().getGameConfigAsClass().getTeams() == 2) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_3;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_7;
            }
        } else if (GameTemplate.getInstance().getGameConfigAsClass().getTeams() == 4) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.equals(Team.YELLOW)) {
                return InventorySlot.ROW_2_SLOT_6;
            } else if (team.equals(Team.GREEN)) {
                return InventorySlot.ROW_2_SLOT_8;
            }
        } else if (GameTemplate.getInstance().getGameConfigAsClass().getTeams() == 8) {
            if (team.equals(Team.RED)) {
                return InventorySlot.ROW_2_SLOT_1;
            } else if (team.equals(Team.BLUE)) {
                return InventorySlot.ROW_2_SLOT_2;
            } else if (team.equals(Team.YELLOW)) {
                return InventorySlot.ROW_2_SLOT_3;
            } else if (team.equals(Team.GREEN)) {
                return InventorySlot.ROW_2_SLOT_4;
            } else if (team.equals(Team.ORANGE)) {
                return InventorySlot.ROW_1_SLOT_5;
            } else if (team.equals(Team.AQUA)) {
                return InventorySlot.ROW_3_SLOT_6;
            } else if (team.equals(Team.WHITE)) {
                return InventorySlot.ROW_1_SLOT_7;
            } else if (team.equals(Team.PURPLE)) {
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
