package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;

@Getter
public class EntityChangeHealthPacketWrapper extends PacketWrapper {

    private double health;

    public EntityChangeHealthPacketWrapper(double health) {
        super(PacketType.ENTITY, EntityAction.CHANGE_HEART);
        this.health = health;
    }
}
