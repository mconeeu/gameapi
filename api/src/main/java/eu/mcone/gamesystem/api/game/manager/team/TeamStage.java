/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.manager.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.NPC;
import eu.mcone.coresystem.api.bukkit.npc.NpcData;
import eu.mcone.coresystem.api.bukkit.npc.data.PlayerNpcData;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.coresystem.api.core.player.SkinInfo;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamStage {

    @Getter
    private final Team team;
    @Getter
    private final CoreWorld stageWorld;
    @Getter
    private Map<GamePlayer, PlayerNpc> npcs;
    @Getter
    private Map<GamePlayer, Location> occupiedLocations;

    private List<Location> availableLocations;

    public TeamStage(final Team team) {
        this.team = team;
        this.stageWorld = CoreSystem.getInstance().getWorldManager().getWorld(GameTemplate.getInstance().getGameConfigAsClass().getLobbyWorld());
        npcs = new HashMap<>();
        occupiedLocations = new HashMap<>();

        availableLocations = new ArrayList<>();

        for (int i = 0; i < GameTemplate.getInstance().getGameConfigAsClass().getTeams(); i++) {
            Location location = stageWorld.getLocation(team.toString().toLowerCase() + ":stage:" + i);
            if (location != null) {
                availableLocations.add(location);
            } else {
                GameTemplate.getInstance().sendConsoleMessage("§8[§7Team-Stage§8] §cDie Location mit dem Key §f" + team.toString().toLowerCase() + ":stage:" + i + " §ckonnte nicht gefunden werden!");
            }
        }
    }

    public void addPlayerToStage(final GamePlayer gamePlayer) {
        if (!npcs.containsKey(gamePlayer)) {
            Location freePlace = getFreePlace();
            if (freePlace != null) {
                availableLocations.remove(freePlace);
                occupiedLocations.put(gamePlayer, freePlace);

                NpcData npcData = new NpcData
                        (
                                EntityType.PLAYER,
                                "stage:" + team.getString().toLowerCase() + ":" + gamePlayer.getName(),
                                team.getChatColor() + gamePlayer.getName(),
                                new CoreLocation(freePlace),
                                new PlayerNpcData
                                        (
                                                gamePlayer.getName(),
                                                "",
                                                SkinInfo.SkinType.PLAYER,
                                                false,
                                                false,
                                                false,
                                                new ArrayList<ItemStack>() {{
                                                    add(0, null);
                                                    add(1, ItemBuilder.createLeatherArmorItem(Material.LEATHER_HELMET, team.getColor()).create());
                                                    add(2, ItemBuilder.createLeatherArmorItem(Material.LEATHER_CHESTPLATE, team.getColor()).create());
                                                    add(3, ItemBuilder.createLeatherArmorItem(Material.LEATHER_LEGGINGS, team.getColor()).create());
                                                    add(4, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, team.getColor()).create());
                                                }}
                                        )
                        );

                npcs.put(gamePlayer, (PlayerNpc) CoreSystem.getInstance().getNpcManager().addNPC(npcData));
                GameTemplate.getInstance().sendConsoleMessage("§8[§7Team-Stage§8] §cSpawn NPC for Player " + gamePlayer.getName() + " Team: " + team.getPrefix());
            } else {
                GameTemplate.getInstance().sendConsoleMessage("§8[§7Team-Stage§8] §cDie zurückgegebene Location ist NULL!");
            }
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§8[§7Team-Stage§8] §cFür Team " + team.getPrefix() + " §csind keine Spawnplätze mehr verfügbar");
        }
    }


    public void removePlayerFromStage(final GamePlayer gamePlayer) {
        if (npcs.containsKey(gamePlayer)) {
            NPC playerNpc = CoreSystem.getInstance().getNpcManager().getNPC(stageWorld, "stage:" + team.getString().toLowerCase() + ":" + gamePlayer.getName());

            if (playerNpc != null) {
                availableLocations.add(playerNpc.getData().getLocation().bukkit());
                occupiedLocations.remove(gamePlayer);
                npcs.remove(gamePlayer);

                CoreSystem.getInstance().getNpcManager().removeNPC(playerNpc);
            } else {
                GameTemplate.getInstance().sendConsoleMessage("§8[§7Team-Stage§8] §cDer NPC mit dem Namen §fstage:" + team.getString().toLowerCase() + ":" + gamePlayer.getName() + " §ckonnte nicht gefunden werden.");
            }
        }
    }

    public void updateStage() {
        if (!npcs.isEmpty()) {
            CoreSystem.getInstance().getNpcManager().reload();
        }
    }

    public void removePlayersFromStage() {
        for (NPC npc : npcs.values()) {
            CoreSystem.getInstance().getNpcManager().removeNPC(npc);
        }
    }

    private Location getFreePlace() {
        int i = 0;
        for (Location available : availableLocations) {
            if (!occupiedLocations.containsValue(available)) {
                return available;
            } else {
                i++;
            }
        }

        if (i >= availableLocations.size()) {
            return null;
        }

        return null;
    }
}
