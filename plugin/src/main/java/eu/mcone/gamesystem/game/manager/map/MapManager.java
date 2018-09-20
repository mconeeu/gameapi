/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.game.manager.map.MapItem;
import eu.mcone.gamesystem.game.inventory.MapInventory;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MapManager implements eu.mcone.gamesystem.api.game.manager.map.MapManager {

    @Getter
    private CorePlugin instance;
    @Getter
    private File jsonFile;
    @Getter
    private MapItem gainedMap;

    @Getter
    private List<MapItem> maps;
    @Getter
    private Map<String, Integer> mapPopularity;
    @Getter
    private Map<Player, String> mapVoting;

    public MapManager(CorePlugin instance) {
        this.instance = instance;

        maps = new ArrayList<>();
        mapPopularity = new HashMap<>();
        mapVoting = new HashMap<>();
    }

    public void createVotingSelection() throws IOException {
        jsonFile = new File("./plugins/" + instance.getPluginName() + "/worlds.json");
        if (!jsonFile.exists()) {
            GameSystemAPI.getInstance().sendConsoleMessage("§aCreating json file...");
            jsonFile.createNewFile();
        }
    }

    public void addMaps() throws IOException {
        JsonArray array = new JsonParser().parse(FileUtils.readFileToString(this.jsonFile)).getAsJsonArray();
        for (JsonElement e : array) {
            this.maps.add(CoreSystem.getInstance().getGson().fromJson(e, MapItem.class));
        }
    }

    public MapItem getMap(final String map) {
        for (MapItem mapItems : this.maps) {
            if (mapItems.getName().equalsIgnoreCase(map)) {
                return mapItems;
            }
        }
        return null;
    }

    public MapItem closeVoting() {
        if (mapVoting.size() != 0) {
            for (int i = 0; i <= mapVoting.size(); i++) {
                for (Map.Entry<String, Integer> entry : this.mapPopularity.entrySet()) {
                    if (entry.getValue() < i || entry.getValue() == i) {
                        gainedMap = getMap(entry.getKey());
                        instance.sendConsoleMessage("§aReturn world " + entry.getKey());
                        return gainedMap;
                    }
                }
            }
        } else {
            gainedMap = maps.get(new Random().nextInt(maps.size()));
            instance.sendConsoleMessage("§aReturn random map");
            return gainedMap;
        }
        return null;
    }

    public void createMapInventory(final Player player, CoreInventory returnInventory) {
        new MapInventory(this, player, returnInventory);
    }

}
