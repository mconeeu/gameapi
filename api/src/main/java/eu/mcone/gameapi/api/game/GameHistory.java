/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.game;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class GameHistory {

    private String gameID;
    @Getter
    private Gamemode gamemode;
    @Setter
    private long started;
    @Setter
    private long stopped;
    @Setter
    private String winner;
    @Setter
    private String map;

    private Map<String, List<MessageWrapper>> messages = new HashMap<>();
    private Map<String, PlayerHistory> players = new HashMap<>();

    public GameHistory(String gameID, Gamemode gamemode) {
        this.gameID = gameID;
        this.gamemode = gamemode;
        this.players = new HashMap<>();
        this.messages = new HashMap<>();
    }

    public void addMessage(int tick, MessageWrapper wrapper) {
        String sTick = String.valueOf(tick);
        if (!this.messages.containsKey(sTick)) {
            this.messages.put(sTick, new ArrayList<>());
        }

        this.messages.get(sTick).add(wrapper);
    }

    public void addPlayer(Player player) {
        if (!this.players.containsKey(player.getUniqueId().toString())) {
            this.players.put(player.getUniqueId().toString(), new PlayerHistory(player));
        }
    }

    @BsonIgnore
    public PlayerHistory getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    @BsonIgnore
    public PlayerHistory getPlayer(UUID uuid) {
        return this.players.get(uuid.toString());
    }
}
