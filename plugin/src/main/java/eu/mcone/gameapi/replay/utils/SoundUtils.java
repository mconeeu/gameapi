package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.util.BlockSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static void playStepSound(Location location, Player[] watchers) {
        System.out.println(location.getBlock().getType());
        Block block = location.getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() != Material.AIR) {
            System.out.println(block.getType());
            BlockSound sound = new BlockSound(block);
            sound.playSound(BlockSound.SoundKey.STEP_SOUND, watchers);
        }
    }
}
