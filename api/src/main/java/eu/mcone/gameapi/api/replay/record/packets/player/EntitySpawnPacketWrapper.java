package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Location;

@Getter
@BsonDiscriminator
public class EntitySpawnPacketWrapper extends PacketWrapper {

    private Location location;

    @BsonCreator
    public EntitySpawnPacketWrapper(@BsonProperty("location") final Location location) {
        super(PacketType.WORLD, EntityAction.SPAWN);
        this.location = location;
    }
}
