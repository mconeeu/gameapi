package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;

@Getter
public class EntityChangeStatePacketWrapper extends PacketWrapper {

    private int foodLevel;
    private int health;

    public EntityChangeStatePacketWrapper(int foodLevel, int health) {
        super(PacketType.ENTITY, EntityAction.CHANGE_FOOD);
        this.foodLevel = foodLevel;
        this.health = health;
    }
}
