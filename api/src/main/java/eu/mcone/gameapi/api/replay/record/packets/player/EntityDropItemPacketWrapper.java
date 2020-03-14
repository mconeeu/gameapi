package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityItemPacketWrapperTemplate;
import org.bukkit.inventory.ItemStack;

public class EntityDropItemPacketWrapper extends EntityItemPacketWrapperTemplate {

    public EntityDropItemPacketWrapper(final ItemStack item) {
        super(EntityAction.DROP_ITEM, item);
    }
}
