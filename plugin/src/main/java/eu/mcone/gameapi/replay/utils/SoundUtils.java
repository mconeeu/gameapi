package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.util.BlockSound;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Random;

public class SoundUtils {

    public static void playStepSound(Location location, Player[] watchers) {
        Random random = new Random(0);
        boolean rSound = random.nextInt(1) == 0;
        if (rSound) {
            BlockSound sound = new BlockSound(location.getBlock().getRelative(BlockFace.DOWN));
            sound.playSound(BlockSound.SoundKey.STEP_SOUND, watchers);
        }
    }
}
