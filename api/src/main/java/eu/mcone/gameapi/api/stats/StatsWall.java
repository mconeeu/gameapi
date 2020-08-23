package eu.mcone.gameapi.api.stats;

import com.mongodb.client.MongoCollection;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.player.OfflineCorePlayer;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

public class StatsWall {

    private MongoCollection<Document> collection;

    public StatsWall() {
        //collection = CoreSystem.getInstance().getStatsDB().getCollection(GameTemplate.getInstance().getGamemode().toString());
    }

    public void update() {
        System.out.println("Run function update");

//        CoreWorld lobbyWorld = CoreSystem.getInstance().getWorldManager().getWorld(GameTemplate.getInstance().getGameConfigAsClass().getLobbyWorld());
//        Location pos1 = lobbyWorld.getLocation("statsPos1");
//        Location pos2 = lobbyWorld.getLocation("statsPos2");
//
//        boolean x = false;
//        boolean z = false;
//        double x1 = pos1.getX();
//        double z1 = pos1.getZ();
//
//        long pos1X = Math.round(pos1.getX());
//        long pos2X = Math.round(pos2.getX());
//
//        long pos1Z = Math.round(pos1.getZ());
//        long pos2Z = Math.round(pos2.getZ());
//
//        if (pos1X == pos2X) {
//            x = true;
//        } else if (pos1Z == pos2Z) {
//            z = true;
//        }
//
//        int place = 1;
//
//        for (Document user : collection.find().sort(orderBy(descending("kill")))) {
//            try {
//                OfflineCorePlayer offlineCorePlayer = CoreSystem.getInstance().getOfflineCorePlayer(UUID.fromString(user.getString("uuid")));
//                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(offlineCorePlayer.getUuid());
//
//                //Background block
//                Material material = getBlockWherePlace(place);
//
//                //South or North
//                if (z) {
//                    //Place position
//                    if (pos1.getX() > pos2.getX()) {
//                        pos1.setX(pos1.getX() - place);
//                    } else if (pos2.getX() > pos1.getX()) {
//                        pos1.setX(pos1.getX() + place);
//                    } else {
//                        break;
//                    }
//
//                    //Background block position
//                    pos1.setZ(pos1.getZ() - 1);
//                    if (!pos1.getBlock().getType().equals(Material.AIR)) {
//                        pos1.getBlock().setType(material);
//                        pos1.setZ(pos1.getZ() + 1);
//                    } else {
//                        pos1.setZ(pos1.getZ() + 2);
//                        pos1.getBlock().setType(material);
//                        pos1.setZ(pos1.getZ() - 1);
//                    }
//
//                    //Set Head
//                    pos1.setZ(pos1.getZ() - 1);
//                    if (pos1.getBlock().getType().equals(Material.AIR)) {
//                        pos1.setZ(pos1.getZ() + 1);
//                        setHead(pos1,  BlockFace.NORTH, offlinePlayer);
//                    } else {
//                        pos1.setZ(pos1.getZ() + 1);
//                        setHead(pos1,  BlockFace.SOUTH, offlinePlayer);
//                    }
//
//                    //Set sign
//                    pos1.setY(pos1.getY() - 1);
//                    if (pos1.getBlock().getType().equals(Material.WALL_SIGN)) {
//                        setSign(pos1, offlineCorePlayer, user, place);
//                        pos1.setY(pos1.getY() + 1);
//                    } else {
//                        pos1.setZ(pos1.getZ() - 2);
//                        setSign(pos1, offlineCorePlayer, user, place);
//                        pos1.setZ(pos1.getZ() + 2);
//                    }
//
//                    pos1.setX(x1);
//
//                    //West or east
//                } else if (x) {
//                    //Place position
//                    if (pos1.getZ() > pos2.getZ()) {
//                        pos1.setZ(pos1.getZ() - place);
//                    } else if (pos2.getZ() > pos1.getZ()) {
//                        pos1.setZ(pos1.getZ() + place);
//                    } else {
//                        break;
//                    }
//
//                    //Background block position
//                    pos1.setX(pos1.getX() - 1);
//                    if (!pos1.getBlock().getType().equals(Material.AIR)) {
//                        pos1.getBlock().setType(material);
//                        pos1.setX(pos1.getX() + 1);
//                    } else {
//                        pos1.setX(pos1.getX() + 2);
//                        pos1.getBlock().setType(material);
//                        pos1.setX(pos1.getX() - 1);
//                    }
//
//                    //Set Head
//                    pos1.setX(pos1.getX() - 1);
//                    if (pos1.getBlock().getType().equals(Material.AIR)) {
//                        pos1.setX(pos1.getX() + 1);
//                        setHead(pos1,  BlockFace.WEST, offlinePlayer);
//                    } else {
//                        pos1.setX(pos1.getX() + 1);
//                        setHead(pos1,  BlockFace.EAST, offlinePlayer);
//                    }
//
//                    //Set sign
//                    pos1.setY(pos1.getY() - 1);
//                    if (pos1.getBlock().getType().equals(Material.WALL_SIGN)) {
//                        setSign(pos1, offlineCorePlayer, user, place);
//                        pos1.setY(pos1.getY() + 1);
//                    } else {
//                        pos1.setX(pos1.getX() - 2);
//                        setSign(pos1, offlineCorePlayer, user, place);
//                        pos1.setX(pos1.getX() + 2);
//                    }
//
//                    pos1.setZ(z1);
//                }
//
//                place++;
//            } catch (PlayerNotResolvedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void setSign(final Location pos1, final OfflineCorePlayer offlinePlayer, final Document document, final int place) {
        Sign sign = (Sign) pos1.getBlock().getState();
        sign.setLine(0, "Platz #" + place);
        sign.setLine(1, offlinePlayer.getMainGroup().getPrefix() + offlinePlayer.getName());
        sign.setLine(2, document.getInteger("kill") + " Kills");
        sign.setLine(3, getKD(document) + " K/D");
        sign.update();
    }

    private void setHead(final Location location, final BlockFace blockFace, final OfflinePlayer offlinePlayer) {
        location.getBlock().setType(Material.SKULL);
        Skull skull = (Skull) location.getBlock().getState();
        skull.setSkullType(SkullType.PLAYER);
        skull.setOwner(offlinePlayer.getName());
        skull.setRotation(blockFace);
        skull.update();
    }

    private Material getBlockWherePlace(final int place) {
        if (place == 1) {
            return Material.GOLD_BLOCK;
        } else if (place == 2) {
            return Material.IRON_BLOCK;
        } else if (place == 3) {
            return Material.HARD_CLAY;
        } else {
            return new ItemBuilder(Material.STAINED_CLAY, 1, 9).create().getType();
        }
    }

    private double getKD(Document document) {
        double kill = document.getInteger("kill");
        double death = document.getInteger("death");

        if (kill == 0 && death == 0) {
            return 0.00D;
        } else if (kill == 0) {
            return 0.00D;
        } else if (death == 0) {
            return kill + .00D;
        } else {
            return kill / death;
        }
    }
}
