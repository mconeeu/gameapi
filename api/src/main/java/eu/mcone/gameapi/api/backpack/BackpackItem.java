/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BackpackItem {

    private int id;
    private String name;
    private Level level;
    private ItemStack item;
    private boolean winnable, buyable, sellable;
    private int buyPrice, sellPrice;

    @Override
    public boolean equals(Object obj) {
        BackpackItem other = (BackpackItem) obj;

        return other.id == id
                && other.name.equals(name)
                && other.level.equals(level)
                && other.item.equals(item)
                && other.winnable == winnable
                && other.buyable == buyable
                && other.buyPrice == buyPrice
                && other.sellPrice == sellPrice;
    }

    @Override
    public String toString() {
        return "BackpackItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", item=" + item +
                ", winnable=" + winnable +
                ", buyable=" + buyable +
                ", sellable=" + sellable +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                '}';
    }
}
