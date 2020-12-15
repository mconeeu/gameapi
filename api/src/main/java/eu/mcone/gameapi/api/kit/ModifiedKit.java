/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.kit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter @Setter
public class ModifiedKit {

    private transient Map<Integer, Integer> items;

    private long lastUpdated;
    private String name;
    private Map<String, Integer> customItems;

    public ModifiedKit(long lastUpdated, String name, Map<Integer, Integer> customItems) {
        this.lastUpdated = lastUpdated;
        this.name = name;

        Map<String, Integer> items = new HashMap<>();
        customItems.forEach((k, v) -> items.put(String.valueOf(k), v));
        this.customItems = items;
    }

    public Map<Integer, Integer> calculateCustomItems() {
        if (items == null) {
            items = new HashMap<>();
            customItems.forEach((k, v) -> items.put(Integer.parseInt(k), v));
        }

        return items;
    }

}
