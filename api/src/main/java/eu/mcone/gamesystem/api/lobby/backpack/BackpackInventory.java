/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.lobby.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.category.StaticClassCategoryInventory;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.lobby.api.enums.Category;
import eu.mcone.lobby.api.enums.Item;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BackpackInventory extends StaticClassCategoryInventory {

    private static final Map<Category, Class<? extends BackpackInventory>> inventories = new HashMap<>();
    private final List<Item> ownItems;

    public BackpackInventory(Player p) {
        super("§8» §3§lRucksack", p);
        this.ownItems = GameTemplate.getInstance().getGamePlayer(p.getUniqueId()).getItems();

        for (Category cat : Category.values()) {
            addCategoryWithInventoryClass(cat.getItem(), inventories.get(cat));
        }
    }

    protected boolean playerHasItem(Item item) {
        if (item.getCategory().equals(Category.STORY_ITEMS)) {
            return ownItems.contains(item);
        } else {
            return ownItems.contains(item) || getPlayer().hasPermission("group.admin");
        }
    }

    public static void registerBackpackInventory(Category category, Class<? extends BackpackInventory> inventory) {
        inventories.put(category, inventory);
    }

    public static void openNewInventory(Category category, Player player) {
        try {
            inventories.get(category).getConstructor(Player.class).newInstance(player).openInventory();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
