package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.config.typeadapter.ItemStackTypeAdapterUtils;
import eu.mcone.coresystem.api.bukkit.event.armor.ArmorEquipEvent;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.enums.EquipmentPosition;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ArmorEquipEventCodec extends Codec<ArmorEquipEvent, PlayerRunner> {

    private String material;
    private String enchantments;
    private int slot;

    public ArmorEquipEventCodec() {
        super("ArmorChange", ArmorEquipEvent.class, PlayerRunner.class);
    }

    @Override
    public Object[] decode(Player player, ArmorEquipEvent armorEquipEvent) {
        material = armorEquipEvent.getNewArmorPiece().getType().toString();
        enchantments = ItemStackTypeAdapterUtils.serializeEnchantments(armorEquipEvent.getNewArmorPiece().getEnchantments());
        slot = armorEquipEvent.getType().getSlot();
        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(material), 1).enchantments(ItemStackTypeAdapterUtils.getEnchantments(enchantments));
        runner.getPlayer().getNpc().setEquipment(EquipmentPosition.getPosition(slot), itemBuilder.create());
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(material);
        out.writeUTF(enchantments);
        out.writeInt(slot);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        material = in.readUTF();
        enchantments = in.readUTF();
        slot = in.readInt();
    }
}
