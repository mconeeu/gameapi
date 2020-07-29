package eu.mcone.gameapi.api.replay.packets.player.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.HashMap;

@Getter
public class InventoryCloseEventCodec extends Codec<InventoryCloseEvent, PlayerRunner> {

    private HashMap<Integer, ItemStack> content;

    public InventoryCloseEventCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, InventoryCloseEvent closeEvent) {
        if (closeEvent.getInventory().getType().equals(InventoryType.CRAFTING)) {
            content = new HashMap<>();

            for (int i = 0; i < closeEvent.getPlayer().getInventory().getSize(); i++) {
                ItemStack item = closeEvent.getPlayer().getInventory().getItem(i);

                if (item != null) {
                    content.put(i, item);
                }
            }

            return new Object[]{closeEvent.getPlayer()};
        }

        return null;
    }

    @Override
    public void encode(PlayerRunner runner) {
        runner.getPlayer().setInventoryItems(content);
    }

    @Override
    protected void onWriteObject(DataOutputStream out) throws IOException {
        String json = CoreSystem.getInstance().getGson().toJson(content);
        out.writeUTF(json);
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException {
        content = (HashMap<Integer, ItemStack>) CoreSystem.getInstance().getGson().fromJson(in.readUTF(), HashMap.class);
    }

}
