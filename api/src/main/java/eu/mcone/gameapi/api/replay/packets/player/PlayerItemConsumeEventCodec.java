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

import java.io.*;

@Getter
public class PlayerItemConsumeEventCodec extends Codec<PlayerItemConsumeEvent, PlayerRunner> {

    private String typ;
    private int level;

    public PlayerItemConsumeEventCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, PlayerItemConsumeEvent itemConsumeEvent) {
        if (itemConsumeEvent.getItem().getType().equals(Material.POTION)) {
            Potion potion = Potion.fromItemStack(itemConsumeEvent.getItem());
            typ = potion.getType().name();
            level = potion.getLevel();
            return new Object[]{itemConsumeEvent.getPlayer()};
        }

        return null;
    }

    @Override
    public void encode(PlayerRunner runner) {
        for (PotionEffect effect : getPotion().getEffects()) {
            runner.getPlayer().getNpc().addPotionEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), runner.getWatchers().toArray(new Player[0]));
        }
    }

    @Override
    protected void onWriteObject(DataOutputStream out) throws IOException {
        out.writeUTF(typ);
        out.writeInt(level);
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException, ClassNotFoundException {
        typ = in.readUTF();
        level = in.readInt();
    }

    public Potion getPotion() {
        return new Potion(PotionType.valueOf(typ), level);
    }
}
