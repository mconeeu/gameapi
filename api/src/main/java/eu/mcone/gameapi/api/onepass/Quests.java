package eu.mcone.gameapi.api.onepass;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum Quests {

    SECRETS(
            "§6Secrets finden",
            "§2Finde §a15§2 Secrets um §a5 Xp §2zu erhalten!",
            new ItemBuilder(Material.SIGN, 1).displayName("§6Secrets finden...").lore("§7§oFinde 15 Secrets", "", "§2Belohnung:§l 5 XP").create(),
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_GIFT_SECRETS.getId(),
            1,
            5
    );


    private final String questName, questTask, backpackItemCategory;
    private final ItemStack itemStack;
    private final int chapter, awardXp, winItem;

    Quests(String questName, String questTask, ItemStack itemStack, String backpackItemCategory, int winItem, int chapter, int awardXp) {
        this.questName = questName;
        this.questTask = questTask;
        this.itemStack = itemStack;
        this.backpackItemCategory = backpackItemCategory;
        this.winItem = winItem;
        this.chapter = chapter;
        this.awardXp = awardXp;
    }

}
