/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.team.GameTeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamInventory extends CoreInventory {

    private final GamePlugin gamePlugin;
    private final GameTeamManager teamManager;

    //TODO: Add team stage integration
    public TeamInventory(Player player, GameTeamManager teamManager) {
        super("§8» §c§oTeamauswahl", player, (((teamManager.getTeams().size() - 1) / 9) + 1) * 9, InventoryOption.FILL_EMPTY_SLOTS);
        gamePlugin = GamePlugin.getGamePlugin();
        this.teamManager = teamManager;

        if (gamePlugin.hasModule(Module.TEAM_MANAGER)) {
            update(player);
            openInventory();
        } else {
            gamePlugin.getMessenger().send(player, "§cDu kannst das Teamvoting Inventory nicht benutzen da das Team Manager modul nicht aktiviert wurde!");
        }
    }

    private void update(Player p) {
        List<Team> teams = teamManager.getTeams();

        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);

            setItem(i, new ItemBuilder(Material.BED, team.getPlayers().size()).displayName(team.getLabel()).lore(getPlayers(team)).create(), e -> {
                if (!gamePlugin.getTeamManager().isTeamsFinallySet()) {
                    System.out.println("choosing team "+team+" "+p.getName());
                    GamePlayer gp = gamePlugin.getGamePlayer(p);

                    if (gp.getTeam() != null && gp.getTeam().equals(team)) {
                        gamePlugin.getMessenger().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.alreadyJoined"));
                        Sound.cancel(p);
                    } else {
                        if (team.getPlayers().size() < team.getSize()) {
                            gp.changeTeamTo(team);
                            gamePlugin.getMessenger().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.join", CoreSystem.getInstance().getGlobalCorePlayer(p.getUniqueId())).replace("%team%", team.getLabel()));
                            Sound.equip(p);

                            //Update all opened Team Inventories
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                CoreInventory inv = CoreSystem.getInstance().getPluginManager().getCurrentCoreInventory(player);

                                if (inv instanceof TeamInventory) {
                                    ((TeamInventory) inv).update(player);
                                    player.updateInventory();
                                }
                            }
                        } else {
                            gamePlugin.getMessenger().send(p, CoreSystem.getInstance().getTranslationManager().get("game.team.maxSize").replace("%max%", Integer.toString(teamManager.getPlayersPerTeam())));
                            Sound.cancel(p);
                        }
                    }
                } else {
                    Msg.send(p, "§4Du kannst dein Team nicht mehr ändern!");
                }
            });
        }
    }

    private String[] getPlayers(Team team) {
        List<String> lore = new ArrayList<>();
        lore.add("§7§oSpieler");
        if (!team.getPlayers().isEmpty()) {
            for (GamePlayer player : team.getPlayers()) {
                lore.add("§8» §7" + player.bukkit().getName());
            }
        } else {
            return new String[]{"§2Offen"};
        }

        return lore.toArray(new String[0]);
    }
}
