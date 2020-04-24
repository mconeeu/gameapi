package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import lombok.Getter;
import org.bukkit.entity.Item;

@Getter
public class EntityPickItemUpPacketContainer extends PacketContainer {

    private final int entityID;
    private final String item;

    public EntityPickItemUpPacketContainer(int entityID, final Item item) {
        super(PacketTyp.ENTITY, EntityAction.PICK_ITEM_UP);
        this.entityID = entityID;
        this.item = item.getName();
    }
}
