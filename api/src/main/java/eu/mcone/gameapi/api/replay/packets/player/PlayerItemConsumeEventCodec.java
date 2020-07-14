package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.MobEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Getter
public class PlayerItemConsumeEventCodec extends Codec<PlayerItemConsumeEvent, PlayerRunner> {

    private String typ;
    private int level;

    public PlayerItemConsumeEventCodec() {
        super("ItemConsume");
    }

    @Override
    public Object[] decode(Player player, PlayerItemConsumeEvent itemConsumeEvent) {
        if (itemConsumeEvent.getItem().getType().equals(Material.POTION)) {
            Potion potion = Potion.fromItemStack(itemConsumeEvent.getItem());
            typ = potion.getType().name();
            level = potion.getLevel();
            return new Object[]{player};
        }

        return null;
    }

    @Override
    public void encode(PlayerRunner runner) {
        for (PotionEffect effect : getPotion().getEffects()) {
            runner.getPlayer().getNpc().addPotionEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), runner.getWatchers().toArray(new Player[0]));
        }
    }

    public Potion getPotion() {
        return new Potion(PotionType.valueOf(typ), level);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(typ);
        out.writeInt(level);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        typ = in.readUTF();
        level = in.readInt();
    }
}
