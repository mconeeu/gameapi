package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bukkit.entity.Item;

public class EntityPickItemUpPacketWrapper extends PacketWrapper {

    @Getter
    private String item;

    public EntityPickItemUpPacketWrapper(final Item item) {
        super(PacketType.ENTITY, EntityAction.PICK_ITEM_UP);
        this.item = item.getName();
    }
}
