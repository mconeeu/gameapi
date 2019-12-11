package eu.mcone.gameapi.backpack;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.core.player.Group;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum RankBoots {

    PREMIUM_BOOTS(Group.PREMIUM, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.ORANGE).displayName("§7§lPremium Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §7Gewöhnlich", "", "§7Premium Boots").create()),
    PREMIUM_PLUS_BOOTS(Group.PREMIUMPLUS, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.ORANGE).displayName("§3§lPremium+ Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §3Ungewöhnlich", "", "§7Premium+ Boots").create()),
    YOUTUBER_BOOTS(Group.YOUTUBER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.PURPLE).displayName("§5§lYoutuber Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §5Episch", "", "§7Youtuber Boots").create()),
    JR_SUPPORTER_BOOTS(Group.JRSUPPORTER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.LIME).displayName("§c§lJunior Supporter Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Junior Supporter Boots").create()),
    SUPPORTER_BOOTS(Group.SUPPORTER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.LIME).displayName("§c§lSupporter Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Supporter Boots").create()),
    MODERATOR_BOOTS(Group.MODERATOR, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.OLIVE).displayName("§c§lModerator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Moderator Boots").create()),
    SR_MODERATOR_BOOTS(Group.SRMODERATOR, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.OLIVE).displayName("§c§lSenior Moderator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Senior Moderator Boots").create()),
    BUILDER_BOOTS(Group.BUILDER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.YELLOW).displayName("§c§lBuilder Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Builder Boots").create()),
    DEVELOPER_BOOTS(Group.DEVELOPER, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.AQUA).displayName("§c§lDeveloper Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Nike Developer Ultra Boost").create()),
    ADMIN_BOOTS(Group.ADMIN, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.RED).displayName("§c§lAdminstrator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7ADMIN SCHUHE ICH BIN DER BOSS").create());

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