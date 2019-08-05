package eu.mcone.gamesystem.api.lobby.cards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemCardBuilder {
    private Material material;
    private int amount;
    private int subID;

    private String displayName;
    private List<String> lore;
    private List<ItemCardEnchantment> enchantments;
    private List<ItemFlag> itemFlags;
    private boolean unbreakable;

    public ItemCardBuilder() {
    }

    public ItemCardBuilder(final Material material) {
        this.material = material;
        this.amount = 1;
    }

    public ItemCardBuilder(final Material material, final int amount) {
        this.material = material;
        this.amount = amount;
    }

    public ItemCardBuilder(final Material material, final int amount, final String displayName) {
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
    }

    public ItemCardBuilder(final Material material, final boolean unbreakable) {
        this.material = material;
        this.amount = 1;
        this.unbreakable = unbreakable;
    }

    public ItemCardBuilder(final Material material, final String displayName, final boolean unbreakable) {
        this.material = material;
        this.displayName = displayName;
        this.amount = 1;
        this.unbreakable = unbreakable;
    }

    public ItemCardBuilder(final Material material, final int amount, final int subID) {
        this.material = material;
        this.amount = amount;
        this.subID = subID;
    }

    public ItemCardBuilder(final Material material, final int amount, final int subID, final String displayName) {
        this.material = material;
        this.amount = amount;
        this.subID = subID;
        this.displayName = displayName;
    }

    public ItemCardBuilder(final Material material, final int amount, final boolean unbreakable) {
        this.material = material;
        this.amount = amount;
        this.unbreakable = unbreakable;
    }

    public ItemCardBuilder(final Material material, final int amount, final String displayName, final boolean unbreakable) {
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
        this.unbreakable = unbreakable;
    }


    public ItemCardBuilder(final Material material, final int amount, final int subID, final boolean unbreakable) {
        this.material = material;
        this.amount = amount;
        this.subID = subID;
        this.unbreakable = unbreakable;
    }

    public ItemCardBuilder(final Material material, final int amount, final int subID, String displayName, final boolean unbreakable) {
        this.material = material;
        this.amount = amount;
        this.subID = subID;
        this.displayName = displayName;
        this.unbreakable = unbreakable;
    }


    public ItemCardBuilder(final Material material, final int amount, final String... lore) {
        this.material = material;
        this.amount = amount;
        this.lore = Arrays.asList(lore);
    }

    public ItemCardBuilder(final Material material, final int amount, final String displayName, final String... lore) {
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
        this.lore = Arrays.asList(lore);
    }

    public ItemCardBuilder(final Material material, final int amount, final boolean unbreakable, final String... lore) {
        this.material = material;
        this.amount = amount;
        this.unbreakable = unbreakable;
        this.lore = Arrays.asList(lore);
    }

    public ItemCardBuilder(final Material material, final int amount, final String displayName, final boolean unbreakable, final String... lore) {
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
        this.unbreakable = unbreakable;
        this.lore = Arrays.asList(lore);
    }

    public ItemCardBuilder(final Material material, final int amount, final int subID, final boolean unbreakable, final String... lore) {
        this.material = material;
        this.amount = amount;
        this.subID = subID;
        this.unbreakable = unbreakable;
        this.lore = Arrays.asList(lore);
    }

    public ItemCardBuilder(final Material material, final int amount, final String displayName, final int subID, final boolean unbreakable, final String... lore) {
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
        this.subID = subID;
        this.unbreakable = unbreakable;
        this.lore = Arrays.asList(lore);
    }


    /**
     * create ItemStack
     *
     * @return ItemStack
     */
    public ItemStack createStack() {
        ItemStack itemStack = new ItemStack(this.material, this.amount, (short) this.subID);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (displayName != null) {
            itemMeta.setDisplayName(displayName);
        }

        if (lore != null && !lore.isEmpty())
            itemMeta.setLore(this.lore);

        if (enchantments != null && !enchantments.isEmpty()) {
            for (ItemCardEnchantment enchantment : enchantments) {
                itemMeta.addEnchant(Enchantment.getByName(enchantment.getEnchantmentName()), enchantment.getEnchantmentLevel(), true);
            }
        }

        if (itemFlags != null && !itemFlags.isEmpty()) {
            itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
        }

        itemMeta.spigot().setUnbreakable(this.unbreakable);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void addItem(final Player player) {
        player.getInventory().addItem(createStack());
    }

    public void addItem(final Player player, final int slot) {
        player.getInventory().setItem(slot, createStack());
    }

    @Getter
    public class ItemCardEnchantment {
        @Getter
        private final String enchantmentName;
        @Getter
        private final int enchantmentLevel;

        public ItemCardEnchantment(final Enchantment enchantment, final int enchantmentLevel) {
            this.enchantmentName = enchantment.getName();
            this.enchantmentLevel = enchantmentLevel;
        }
    }
}
