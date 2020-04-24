package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import lombok.Getter;

@Getter
public class EntityChangeStatePacketContainer extends PacketContainer {

    private int foodLevel;
    private int health;

    public EntityChangeStatePacketContainer(int foodLevel, int health) {
        super(PacketTyp.ENTITY, EntityAction.CHANGE_FOOD);
        this.foodLevel = foodLevel;
        this.health = health;
    }
}
