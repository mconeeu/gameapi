package eu.mcone.gameapi.backpack;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.core.player.Group;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

@Getter
public enum RankBoots {

    PREMIUM_BOOTS(Group.PREMIUM, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.ORANGE).displayName("§7§lPremium Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §7Gewöhnlich", "", "§7Premium Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    PREMIUM_PLUS_BOOTS(Group.PREMIUMPLUS, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.ORANGE).displayName("§3§lPremium+ Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §3Ungewöhnlich", "", "§7Premium+ Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    ONE_BOOTS(Group.ONE, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.AQUA).displayName("§5§lOne Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Junior Supporter Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    CREATOR_BOOTS(Group.CREATOR, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.PURPLE).displayName("§5§lCreator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §5Episch", "", "§7Creator Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    JR_SUPPORTER_BOOTS(Group.JRSUPPORTER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.LIME).displayName("§c§lJunior Supporter Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Junior Supporter Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    SUPPORTER_BOOTS(Group.SUPPORTER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.LIME).displayName("§c§lSupporter Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Supporter Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    MODERATOR_BOOTS(Group.MODERATOR, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.OLIVE).displayName("§c§lModerator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Moderator Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    SR_MODERATOR_BOOTS(Group.SRMODERATOR, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.OLIVE).displayName("§c§lSenior Moderator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Senior Moderator Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    JR_BUILDER_BOOTS(Group.JRBUILDER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.YELLOW).displayName("§c§lJunior Builder Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Junior Builder Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    BUILDER_BOOTS(Group.BUILDER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.YELLOW).displayName("§c§lBuilder Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Builder Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    SR_BUILDER_BOOTS(Group.SRBUILDER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.YELLOW).displayName("§c§lSenior Builder Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Senior Builder Boots").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    JR_DEVELOPER_BOOTS(Group.JRDEVELOPER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.BLUE).displayName("§c§lJunior Developer Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Nike Jr Developer Ultra Boost x DL").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    DEVELOPER_BOOTS(Group.DEVELOPER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.BLUE).displayName("§c§lDeveloper Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Nike Developer Ultra Boost").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    SR_DEVELOPER_BOOTS(Group.SRDEVELOPER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.BLUE).displayName("§c§lSenior Developer Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Nike Sr Developer Ultra Boost x RM").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    CONTENT_BOOTS(Group.CONTENT, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.NAVY).displayName("§c§lContent Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Gucci x Supreme MH100").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    ADMIN_BOOTS(Group.ADMIN, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.RED).displayName("§c§lAdminstrator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7BAN BAN BAN. ICH BIN DER BOSS!!").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create());

    private Group group;
    private ItemStack item;

    RankBoots(Group group, ItemStack item) {
        this.group = group;
        this.item = item;
    }

    public static RankBoots getBootsByGroup(Group group) {
        for (RankBoots boots : values()) {
            if (boots.getGroup().equals(group)) {
                return boots;
            }
        }

        return null;
    }
}
