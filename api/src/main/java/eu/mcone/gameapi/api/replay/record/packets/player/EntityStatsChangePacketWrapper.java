package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
public class EntityStatsChangePacketWrapper extends PacketWrapper {

    private final int kills;
    private final int deaths;
    private final int goals;

    @BsonCreator
    public EntityStatsChangePacketWrapper(@BsonProperty("kills") final int kills, @BsonProperty("deaths") final int deaths, @BsonProperty("goals") final int goals) {
        super(PacketType.ENTITY, EntityAction.STATS);

        this.kills = kills;
        this.deaths = deaths;
        this.goals = goals;
    }
}
