/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.api.GameTemplate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPing implements Listener {

    @EventHandler
    public void on(ServerListPingEvent e) {
        if (GameTemplate.getInstance() != null) {
            switch (GameTemplate.getInstance().getGameStateHandler().getCurrentStateID()) {
                case ERROR:
                    e.setMotd("§f§lMCONE §3Minigamesnetzwerk §8» §f§lMC 1.12 §7[1.8 PVP]" +
                            "\n§7§o" + GameTemplate.getInstance().getPluginName() + "-" + GameTemplate.getInstance().getGameSettingsConfig().getTeams() + "x" + GameTemplate.getInstance().getGameSettingsConfig().getPlayersPreTeam() + " §8» §4§lERROR");
                    break;
                case LOBBY:
                    e.setMotd("§f§lMCONE §3Minigamesnetzwerk §8» §f§lMC 1.12 §7[1.8 PVP]" +
                            "\n§7§o" + GameTemplate.getInstance().getPluginName() + "-" + GameTemplate.getInstance().getGameSettingsConfig().getTeams() + "x" + GameTemplate.getInstance().getGameSettingsConfig().getPlayersPreTeam() + " §8» §a§lLOBBY");
                    break;
                case INGAME:
                    e.setMotd("§f§lMCONE §3Minigamesnetzwerk §8» §f§lMC 1.12 §7[1.8 PVP]" +
                            "\n§7§o" + GameTemplate.getInstance().getPluginName() + "-" + GameTemplate.getInstance().getGameSettingsConfig().getTeams() + "x" + GameTemplate.getInstance().getGameSettingsConfig().getPlayersPreTeam() + " §8» §c§lINGAME");
                    break;
                case END:
                    e.setMotd("§f§lMCONE §3Minigamesnetzwerk §8» §f§lMC 1.12 §7[1.8 PVP]" +
                            "\n§7§o" + GameTemplate.getInstance().getPluginName() + "-" + GameTemplate.getInstance().getGameSettingsConfig().getTeams() + "x" + GameTemplate.getInstance().getGameSettingsConfig().getPlayersPreTeam() + " §8» §4§lEND");
                    break;
            }
        }
    }
}
