package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.util.BlockSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Random;

public class SoundUtils {

    public static void playStepSound(Location location, Player[] watchers) {
        Random random = new Random(0);
        boolean rSound = random.nextInt(1) == 1;
        if (rSound) {
            Block block = location.getBlock();
            System.out.println(block.getType());
            if (block.getType() != Material.AIR) {
                System.out.println("PLAY");
                BlockSound sound = new BlockSound(block.getRelative(BlockFace.DOWN));
                sound.playSound(BlockSound.SoundKey.STEP_SOUND, watchers);
            }
        }
    }
}
