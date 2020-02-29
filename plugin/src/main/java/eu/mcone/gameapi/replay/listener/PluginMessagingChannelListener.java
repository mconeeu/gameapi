package eu.mcone.gameapi.replay.listener;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PluginMessagingChannelListener implements PluginMessageListener {

    //Incoming Messages
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            String uuid = in.readUTF();
            System.out.println("UUID: " + uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
