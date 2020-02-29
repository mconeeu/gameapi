package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@BsonDiscriminator
public class EntityChangeHealthPacketWrapper extends PacketWrapper {

    private double health;

    public EntityChangeHealthPacketWrapper(double health) {
        super(PacketType.ENTITY, EntityAction.CHANGE_HEART);
        this.health = health;
    }

    @BsonCreator
    public EntityChangeHealthPacketWrapper(@BsonProperty("packetType") PacketType packetType, @BsonProperty("entityAction") EntityAction entityAction, @BsonProperty("health") double health) {
        super(packetType, entityAction);
        this.health = health;
    }
}
