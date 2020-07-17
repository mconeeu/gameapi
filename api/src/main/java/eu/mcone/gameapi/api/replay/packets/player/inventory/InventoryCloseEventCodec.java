package eu.mcone.gameapi.api.replay.packets.player.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

@Getter
public class InventoryCloseEventCodec extends Codec<InventoryCloseEvent, PlayerRunner> {

    private HashMap<Integer, ItemStack> content;

    public InventoryCloseEventCodec() {
        super("Close", InventoryCloseEvent.class, PlayerRunner.class);
    }

    @Override
    public Object[] decode(Player player, InventoryCloseEvent closeEvent) {
        if (closeEvent.getInventory().getType().equals(InventoryType.PLAYER)) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);

                if (item != null) {
                    content.put(i, item);
                }
            }

            return new Object[]{player};
        }

        return null;
    }

    @Override
    public void encode(PlayerRunner runner) {
        runner.getPlayer().setInventoryItems(content);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        String json = CoreSystem.getInstance().getGson().toJson(content);
        out.writeUTF(json);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        content = (HashMap<Integer, ItemStack>) CoreSystem.getInstance().getGson().fromJson(in.readUTF(), HashMap.class);
    }
}
