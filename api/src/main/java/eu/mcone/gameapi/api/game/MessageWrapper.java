/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.game;

import eu.mcone.coresystem.api.bukkit.broadcast.Broadcast;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@BsonDiscriminator
public class MessageWrapper {

    private Broadcast broadcast;
    private long timestamp;

    public MessageWrapper(Broadcast broadcast) {
        this.broadcast = broadcast;
        this.timestamp = System.currentTimeMillis() / 1000;
    }

}
