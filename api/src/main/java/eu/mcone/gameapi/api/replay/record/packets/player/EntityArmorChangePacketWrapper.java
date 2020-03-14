package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.event.armor.ArmorType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityItemPacketWrapperTemplate;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class EntityArmorChangePacketWrapper extends EntityItemPacketWrapperTemplate {

    private int slot;

    public EntityArmorChangePacketWrapper(ArmorType type, ItemStack itemStacks) {
        super(EntityAction.CHANGE_ARMOR, itemStacks);
        slot = type.getSlot();
    }
}
