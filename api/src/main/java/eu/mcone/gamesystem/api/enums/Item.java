/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.enums;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum Item {

    //Gadgets
    LOVEGUN(1, "LoveGun", Category.GADGET, Level.USUAL, 50,20, new ItemBuilder(Material.REDSTONE, 1, 0).displayName("§7§lLove Gun").lore("§7Kategorie: §bGadget", "§7Seltenheit: §7Gewöhnlich", "", "", "§7Herze über Herze").create()),
    EASTERGUN(2, "OsterGun", Category.GADGET, Level.UNUSUAL, 80,50, new ItemBuilder(Material.FIREWORK_CHARGE, 1, 0).displayName("§3§lOster Gun").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7Schieße das Osterfieber dürch die Lobby!").create()),
    SNOWGUN(3, "SnowGun", Category.GADGET, Level.EPIC, 120,70, new ItemBuilder(Material.WOOD_HOE, 1, 0).displayName("§3§lSnowGun").lore("§7Kategorie: §bGadget", "§7Seltenheit: §5Episch", "", "", "§7Schieße das Weinachtfieber dürch die Lobby!").create()),
    //ENDERGUN(4, "EnderGun", Category.GADGET, Level.USUAL, 40, new ItemBuilder(Material.EYE_OF_ENDER, 1, 0).displayName("§7§lEnder Gun").lore("§7Kategorie: §bGadget", "§7Seltenheit: §7Gewöhnlich", "", "", "§7Grusselig").create()),
    COINBOMB(5, "CoinBombe", Category.GADGET, Level.UNUSUAL, 80,50, new ItemBuilder(Material.IRON_INGOT).displayName("§3§lCoin Bombe").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7COIIIIIINSSS FÜR ALLE").create()),
    BOMB(6, "Bombe", Category.GADGET, Level.UNUSUAL, 80,50, new ItemBuilder(Material.TNT, 1).displayName("§3§lBombe").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7Achtung Sprengung!").create()),
    GRAPPLING_HOOK(7, "Enterhacken", Category.GADGET, Level.EPIC, 120,70, new ItemBuilder(Material.FISHING_ROD, 1, 0).displayName("§3§lEnterhacken").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7Fühle dich wie Spiderman!").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),

    //Story items
    MAGICDRINK(11, "ZauberTrank", Category.STORY_ITEMS, Level.EPIC, 0, 0,new ItemBuilder(Material.POTION, 1, 0).displayName("§5§lZauber Trank").lore("§7Kategorie: §bItem", "§7Seltenheit: §5Episch", "", "§7Helfe den Bürgermeister Rufi!").create()),
    MAGICWAND(12, "ZauberStab", Category.STORY_ITEMS, Level.EPIC, 0,0, new ItemBuilder(Material.STICK, 1, 0).displayName("§5§lZauber Stab").lore("§7Kategorie: §bItem", "§7Seltenheit: §5Episch", "", "§7Zauber wie ein Profi!").create()),
    ONE_HIT_SWORD(13, "One Hit Schwerdt", Category.STORY_ITEMS, Level.LEGENDARY, 0,0, new ItemBuilder(Material.DIAMOND_SWORD, 1, 0).displayName("§6§lOne Hit Sword").lore("§7Kategorie: §bItem", "§7Seltenheit: §cMythisch", "", "§7Schieße Blitze duch die Lobby").enchantment(Enchantment.DAMAGE_ALL, 5).unbreakable(true).itemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE).create()),
    PASS(14, "Ausweiss", Category.STORY_ITEMS, Level.EPIC, 0, 0, new ItemBuilder(Material.BOOK, 1, 0).displayName("§5§lPersonalausweis").lore("§7Kategorie: §bItem", "§7Seltenheit: §5Episch", "", "§7Dein Pass").enchantment(Enchantment.DAMAGE_ALL, 5).unbreakable(true).itemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE).create()),
    BOAT_PASS(15, "Fahrkarte", Category.STORY_ITEMS, Level.EPIC, 0,0, new ItemBuilder(Material.PAPER, 1, 0).displayName("§5§lFahrkarte").lore("§7Kategorie: §bItem", "§7Seltenheit: §5Episch", "§720 Coins", "§7Deine Fahrkarte zu Paradise Island").enchantment(Enchantment.DAMAGE_ALL, 5).unbreakable(true).itemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE).create()),
    RADIO_SET1(16, "Funkgerät1", Category.STORY_ITEMS, Level.EPIC, 0, 0,new ItemBuilder(Material.TRIPWIRE_HOOK, 1, 0).displayName("§5§lFunkgerät").lore("§7Kategorie: §bItem", "§7Seltenheit: §5Episch", "", "§7Funk verbunden mit Edward").create()),
    GPS(17, "GPS", Category.STORY_ITEMS, Level.EPIC, 0,0, new ItemBuilder(Material.PRISMARINE_SHARD, 1, 0).displayName("§5§lGPS-Sender").lore("§7Kategorie: §bItem", "§7Seltenheit: §5Episch", "", "§7GPS verbunden mit Edward").create()),
    RADIO_SET_2(18, "Funkgerät2", Category.STORY_ITEMS, Level.UNUSUAL, 0,0, new ItemBuilder(Material.REDSTONE_COMPARATOR, 1, 0).displayName("§3§lAltes Funkgerät").lore("§7Kategorie: §bItem", "§7Seltenheit: §3Ungewöhnlich", "", "§7Funk verbunden mit Sparow").create()),
    BANKCARD(19, "Bankkarte", Category.STORY_ITEMS, Level.LEGENDARY, 0, 0,new ItemBuilder(Material.PAINTING, 1, 0).displayName("§6§lBankkarte").lore("§7Kategorie: §bItem", "§7Seltenheit: §6Legendär", "", "§7Karte rein karte raus Geld da").create()),
    BANKCARD_PREMIUM(20, "Bankkarte", Category.STORY_ITEMS, Level.LEGENDARY, 0,0, new ItemBuilder(Material.PAINTING, 1, 0).displayName("§6§lPremium Bankkarte").lore("§7Kategorie: §bItem", "§7Seltenheit: §6Legendär", "", "§7Karte rein karte raus mehr Geld da").create()),
    OFFICE_CARD_BRONZE(21, "Büro Karte", Category.STORY_ITEMS, Level.LEGENDARY, 0,0, new ItemBuilder(Material.PAPER, 1, 0).displayName("§6§lBüro Karte").lore("§7Kategorie: §bItem", "§7Seltenheit: §6Legendär", "", "§7Ihre Büro SchlüsselKarte").create()),
    OFFICE_CARD_SILVER(22, "Büro Karte", Category.STORY_ITEMS, Level.LEGENDARY, 0,0, new ItemBuilder(Material.PAPER, 1, 0).displayName("§6§lBüro Karte").lore("§7Kategorie: §bItem", "§7Seltenheit: §6Legendär", "", "§7Ihre Büro SchlüsselKarte").create()),
    OFFICE_CARD_GOLD(23, "Büro Karte", Category.STORY_ITEMS, Level.LEGENDARY, 0,0, new ItemBuilder(Material.PAPER, 1, 0).displayName("§6§lBüro Karte").lore("§7Kategorie: §bItem", "§7Seltenheit: §6Legendär", "", "§7Ihre Büro SchlüsselKarte").create()),

    //bank robbery

    GOLD_BARDING(24, "Gold Barren", Category.STORY_ITEMS, Level.LEGENDARY, 0,0, new ItemBuilder(Material.GOLD_INGOT, 58).displayName("§6§lGold Barren").lore("§7Kategorie: §bItem", "§7Seltenheit: §6Legendär", "", "§724 Karat Barren").create()),
    BUTTON(25, "Knopf im Ohr", Category.STORY_ITEMS, Level.LEGENDARY, 0,0, new ItemBuilder(Material.STONE_BUTTON, 1, 0).displayName("§6§lKnopf im Ohr").lore("§7Kategorie: §bItem", "§7Seltenheit: §6Legendär", "", "§7Verbunden mit John").create()),
    BANK_MAP(26, "Bank Plan", Category.STORY_ITEMS, Level.LEGENDARY, 0,0, new ItemBuilder(Material.EMPTY_MAP, 1, 0).displayName("§6§lPlan der Bank").lore("§7Kategorie: §bItem", "§7Seltenheit: §6Legendär", "", "§7Ach dahinten ist der Tresor").create()),
    WHITE_WOOL(27, "Bank Plan", Category.STORY_ITEMS, Level.UNUSUAL, 0,0, new ItemBuilder(Material.WOOL, 37, 0).displayName("§3§lWeiße Wolle").lore("§7Kategorie: §bItem", "§7Seltenheit: §3Ungewöhnlich", "", "§7Zum nähen geeignet").create()),
    BANK_OUTFIT(28, "Bank Outfit", Category.STORY_ITEMS, Level.UNUSUAL, 0,0, new ItemBuilder(Material.CHEST, 1, 0).displayName("§3§lBank Outfit im Packet").lore("§7Kategorie: §bItem / Outfit", "§7Seltenheit: §3Ungewöhnlich", "", "§7Sehe aus wie ein Bänker").create()),
    GOLD_NUGGET(29, "Gold Münze", Category.STORY_ITEMS, Level.MYSTICAL, 0,0, new ItemBuilder(Material.GOLD_NUGGET, 1, 0).displayName("§c§lGold Münze").lore("§7Kategorie: §bItem", "§7Seltenheit: §cMythisch", "", "§7Ein kleines Geschenk von John").create()),


    //Heads
    HEAD_PALUTEN(30, "Palutens Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/33121196cf12ee65354016861da064948d4ce0912f662bca2e2a6b2a932038", 1).toItemBuilder().displayName("§5§lPalutens Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "§7Rang:§5 Youtuber", "", "§7Edgar bist du da ??").create()),
    HEAD_DNER(31, "Dners Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/6092d4ea3448f3b2fbf355fcdfa2d36e51b2587a09e41e58eaa51e3daad4de5", 1).toItemBuilder().displayName("§5§lDners Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "§7Rang:§5 Youtuber", "", "§7Spielkind Zeit").create()),
    HEAD_RUFI(32, "Rufis Kopf", Category.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/367ca878127ae2268b954dc3b32c6f22ed3a45937a843f149e268cb273fa", 1).toItemBuilder().displayName("§6§lRufis Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "§7Rang:§4 Admin", "", "§7Hat wer probleme?").create()),
    HEAD_TWINSTER(33, "Twinsters Kopf", Category.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/1d8b90f78845b3ef1093dec5883e2821349424bb10fb567527d285241c54131e", 1).toItemBuilder().displayName("§6§lTwinsters Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "§7Rang:§4 Admin", "", "§7grüß god").create()),
    HEAD_DRMARV(34, "DrMarvs Kopf", Category.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/a0d0f12bd7d654e49466dcd69a2733076b6df9fe0519ef2c59cb75008744877a", 1).toItemBuilder().displayName("§6§lDrMarvs Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "§7Rang:§b Developer ", "", "§7I bims der DrMarv").create()),
    HEAD_POKEMON(35, "Pokemon Kopf", Category.HAT, Level.UNUSUAL, 80,95, Skull.fromUrl("http://textures.minecraft.net/texture/d43d4b7ac24a1d650ddf73bd140f49fc12d2736fc14a8dc25c0f3f29d85f8f", 1).toItemBuilder().displayName("§3§lPokémon Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Da ist ein wildes Pikachu").create()),
    HEAD_EYE(36, "Augen Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/2cef87772afd85b468f4c7fb9571e31435ef765ad413fe460262150423e2021", 1).toItemBuilder().displayName("§5§lAugen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Nur 1 Auge").create()),
    HEAD_GERMAN(37, "Deutschland Kopf", Category.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f", 1).toItemBuilder().displayName("§7§lDeutschland Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Deutsches Vaterland").create()),
    HEAD_PISTON(38, "Kolben Kopf", Category.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/aa868ce917c09af8e4c350a5807041f6509bf2b89aca45e591fbbd7d4b117d", 1).toItemBuilder().displayName("§7§lKolben Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Kolben raus Kolben rein").create()),
    HEAD_DEATH_SMILEY(39, "Toter Kopf", Category.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/b371e4e1cf6a1a36fdae27137fd9b8748e6169299925f9af2be301e54298c73", 1).toItemBuilder().displayName("§7§lToter Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Booom Tot").create()),
    HEAD_SPION(40, "Spion Kopf", Category.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/4fc1d88be2528168f67da16a19b14f04e1e4963a99dfcb4e49d984a351313c", 1).toItemBuilder().displayName("§6§lSpion Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "", "§7Tötet jeden der im weg ist!").create()),
    HEAD_MRMOREGAME(41, "MrMoreGames Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/ce3a8afbc57d27a63ed4aa2e4d1c2d05274b5f6a04e85ee1fae56e36b187ad", 1).toItemBuilder().displayName("§5§lMrMoreGames Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "§7Rang:§5 Youtuber", "", "§7Bin ich ein Mr. ??").create()),
    HEAD_HAPPY_SMILLEY(42, "Fröhlicher Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/3e1debc73231f8ed4b69d5c3ac1b1f18f3656a8988e23f2e1bdbc4e85f6d46a", 1).toItemBuilder().displayName("§7§lGlücklicher Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Ahh heute ist ein wundervoller Tag").create()),
    HEAD_MARIO(43, "Mario Kopf", Category.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/dba8d8e53d8a5a75770b62cce73db6bab701cc3de4a9b654d213d54af9615", 1).toItemBuilder().displayName("§3§lMario Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Mario").create()),
    HEAD_SKYPE(44, "Skype Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/2ec182da7d3c0a8acc3be9b77c29be47e08c20b050b13fd4c4c7d71f66273", 1).toItemBuilder().displayName("§7§lSkype Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Beeepp Beeeepp").create()),
    HEAD_BEDROCK(45, "Bedrock Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/36d1fabdf3e342671bd9f95f687fe263f439ddc2f1c9ea8ff15b13f1e7e48b9", 1).toItemBuilder().displayName("§7§lBedrock Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Bewegt sich nix").create()),
    HEAD_GIFT(46, "Geschenk Kopf", Category.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/f0afa4fffd10863e76c698da2c9c9e799bcf9ab9aa37d8312881734225d3ca", 1).toItemBuilder().displayName("§7§lGeschenk Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Herzlichen Glückwunsch").create()),
    HEAD_YOUTUBE(47, "Youtube Kopf", Category.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/b4353fd0f86314353876586075b9bdf0c484aab0331b872df11bd564fcb029ed", 1).toItemBuilder().displayName("§3§lYoutube Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Jaaa neues Video").create()),
    HEAD_LUIGI(48, "Luigi Kopf", Category.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/ff1533871e49ddab8f1ca82edb1153a5e2ed3764fd1ce029bf829f4b3caac3", 1).toItemBuilder().displayName("§7§lLuigi Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Mario wo bist den du ??").create()),
    HEAD_BEE(49, "Bienen Kopf", Category.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/947322f831e3c168cfbd3e28fe925144b261e79eb39c771349fac55a8126473", 1).toItemBuilder().displayName("§7§lBienen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7 Bwwwww pix...  Ahhhhh!!").create()),
    HEAD_RABBIT(50, "Hasen Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/dc7a317ec5c1ed7788f89e7f1a6af3d2eeb92d1e9879c05343c57f9d863de130", 1).toItemBuilder().displayName("§7§lHasen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7 Hier ein Ei da ein Ei").create()),
    HEAD_PIG(51, "Edgars Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4", 1).toItemBuilder().displayName("§5§lEdgars Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Paluten wo bist du ??").create()),
    HEAD_TUBILP(52, "Tubis Kopf", Category.HAT, Level.USUAL, 25,20, Skull.fromUrl("http://textures.minecraft.net/texture/ee5c31f12b34652e5ca5f8dc94b7a15476d10528f7a714304de7c398da", 1).toItemBuilder().displayName("§7§lTubis Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Bin ich so billig oder wieso will mich jeder haben?").create()),
    HEAD_UNGESPIELT(53, "Ungespielts Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/74b0a3b34ae1d736bc0620fa1f1d8766e63a7f6f326d3271e8f3b1958aec75", 1).toItemBuilder().displayName("§5§lUngespielts Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "§7Rang:§5 Youtuber ", "", "§7Milch ist Gift!").create()),
    HEAD_MONSTER(54, "Monster Kopf", Category.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/c73ad1ebeb9b7525708a933bdae086599a8dcd66d8b414531ce63bf9953bd3e", 1).toItemBuilder().displayName("§6§lMonster Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "", "§7Arhhhhh").create()),
    HEAD_DARTH_VADER(55, "Dath Vader Kopf", Category.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/c1c3e1f224b446ccac6a6cc3cd9891019a122f99691c3907992a3af99a21b0", 1).toItemBuilder().displayName("§3§lDarth Vader Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Mit dir geht die schwarze Macht!").create()),
    HEAD_KISS_SMILEY(56, "Kuss Kopf", Category.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/545bd18a2aaf469fad72e52cde6cfb02bfbaa5bfed2a8151277f779ebcdcec1", 1).toItemBuilder().displayName("§7§lKuss Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Muuuu aaa!").create()),
    HEAD_CHICKEN(57, "Huhn Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", 1).toItemBuilder().displayName("§7§lHuhn Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Gagagaga!").create()),
    HEAD_MUSHROOM_COW(58, "Pilz Kopf", Category.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db", 1).toItemBuilder().displayName("§7§lPilzKuh Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Muhh").create()),
    HEAD_PANDA(59, "Panda Kopf", Category.HAT, Level.USUAL, 50, 20, Skull.fromUrl("http://textures.minecraft.net/texture/d188c980aacfa94cf33088512b1b9517ba826b154d4cafc262aff6977be8a", 1).toItemBuilder().displayName("§7§lPanda Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Panda Musik thick thack").create()),
    HEAD_COMPUTER(61, "Computer Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/45e9bd425fbd39a94c30bf5aeb301db186713322761fc82a76fb6168793490", 1).toItemBuilder().displayName("§7§lComputer Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7FORTNITE SPIELEN...").create()),
    HEAD_BURGER(62, "Bürger Kopf", Category.HAT, Level.UNUSUAL, 85,50, Skull.fromUrl("http://textures.minecraft.net/texture/a6ef1c25f516f2e7d6f7667420e33adcf3cdf938cb37f9a41a8b35869f569b", 1).toItemBuilder().displayName("§3§lBürger Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§750 Jahre BigMac").create()),
    HEAD_DISCO_BALL(63, "Disco Kugel Kopf", Category.HAT, Level.UNUSUAL, 80, 50, Skull.fromUrl("http://textures.minecraft.net/texture/b462ddfa553ce78683be477b8d8654f3dfc3aa2969808478c987ab88c376a0", 1).toItemBuilder().displayName("§3§lDisco Kugel Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Tanz Baby Tanz").create()),
    HEAD_LISTENING_SMILEY(64, "Lauschender Kopf", Category.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/3baabe724eae59c5d13f442c7dc5d2b1c6b70c2f83364a488ce5973ae80b4c3", 1).toItemBuilder().displayName("§3§lLauschender Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Aha du hast 1 Millionen Euro auf der Bank").create()),
    HEAD_MELON(65, "Melonen Kopf", Category.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/c3fed514c3e238ca7ac1c94b897ff6711b1dbe50174afc235c8f80d029", 1).toItemBuilder().displayName("§7§lMelonen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Hohl wie Bedrock").create()),
    HEAD_PENGUIN(66, "Pinguin Kopf", Category.HAT, Level.USUAL, 80, 50,Skull.fromUrl("http://textures.minecraft.net/texture/d3c57facbb3a4db7fd55b5c0dc7d19c19cb0813c748ccc9710c714727551f5b9", 1).toItemBuilder().displayName("§7§lPinguin Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Bohr ist das warm hier!").create()),
    HEAD_JOKER(67, "Joker Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/af4f6825ef6d5e46d794697d1bf86d144bf6fb3da4e55f7cf55271f637eaa7", 1).toItemBuilder().displayName("§5§lJoker Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Wo ist Batman!").create()),
    HEAD_SHEEP(68, "Schaf Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70", 1).toItemBuilder().displayName("§7§lSchaf Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Mahh").create()),
    HEAD_VILLAGER(69, "Villager Kopf", Category.HAT, Level.UNUSUAL, 80, 50,Skull.fromUrl("http://textures.minecraft.net/texture/822d8e751c8f2fd4c8942c44bdb2f5ca4d8ae8e575ed3eb34c18a86e93b", 1).toItemBuilder().displayName("§3§lVillager Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Will wer was kaufen").create()),
    HEAD_GARADOS(70, "Garados Kopf", Category.HAT, Level.UNUSUAL, 80, 50,Skull.fromUrl("http://textures.minecraft.net/texture/1ab93af668cb83e379e9edbcdc4532f1294f90cb13de6a582efab87696c36dd", 1).toItemBuilder().displayName("§3§lGarados Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Pokémon sind cool").create()),
    HEAD_PORTAL(71, "Portal Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/a4a319deafefd6adb37f21449ea56d3ea5a83857fb9616fa7d4f9ea625177", 1).toItemBuilder().displayName("§7§lPortal Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Rein in das Portal").create()),
    HEAD_GHOST(72, "Ghost Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/23c71a85eeb3cd6449159675aa89278a2a1d587b4d0b768174fc2e15c9be4d", 1).toItemBuilder().displayName("§5§lGeist Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Uhh hier sind Geister").create()),
    HEAD_CRYING(73, "Wein Kopf", Category.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/1f1b875de49c587e3b4023ce24d472ff27583a1f054f37e73a1154b5b5498", 1).toItemBuilder().displayName("§7§lWein Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Ahhhh ich bin so traurig").create()),
    HEAD_DINOSAUR(74, "Dino Kopf", Category.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/d582ce1d9f6f34c087b4fbec5bdb758732dc0658b86e275a9b46bacd58ae899", 1).toItemBuilder().displayName("§5§lDino Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7UAHHHHH").create()),
    HEAD_PLAYCUBEHD(75, "Playcubes Kopf", Category.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/ff747a6c6757cc7720059d7f5cdbbc32d6d45e1984262d11a92213f1812a4d0e", 1).toItemBuilder().displayName("§3§lPlaycubes Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Will wer GTA").create()),

    //Gadget Heads
    HEAD_SECRET_STRIPCLUB(76, "Stripclub Kopf", Category.HAT, Level.EPIC, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/36aae86da0cd317a47fa6668fd4785b5a7a7e4ed9e7bc68652bae27984b84c", 1).toItemBuilder().displayName("§3§lLeichen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Kirphas 100 Jahre alter Kopf").create()),

    //Outfits
    OUTFIT_RABBIT(111, "Hasen Outfit", Category.OUTFITS, Level.UNUSUAL, 125,75, Skull.fromUrl("http://textures.minecraft.net/texture/dc7a317ec5c1ed7788f89e7f1a6af3d2eeb92d1e9879c05343c57f9d863de130", 1).toItemBuilder().displayName("§3§lHasen Outfit").lore("§7Kategorie: §bKleidung / Outfits", "§7Seltenheit: §3Ungewöhnlich", "", "§7Bringe die Eier raus").create()),
    OUTFIT_DINOSAUR(112, "Dino Outfit", Category.OUTFITS, Level.EPIC, 145,85, Skull.fromUrl("http://textures.minecraft.net/texture/d582ce1d9f6f34c087b4fbec5bdb758732dc0658b86e275a9b46bacd58ae899", 1).toItemBuilder().displayName("§5§lDino Outfit").lore("§7Kategorie: §bKleidung / Outfits", "§7Seltenheit: §5Episch", "", "§7AUUHHHH").create()),
    OUTFIT_SANTA(113, "Weinachstmann Outfit", Category.OUTFITS, Level.EPIC, 145,85, Skull.fromUrl("http://textures.minecraft.net/texture/d582ce1d9f6f34c087b4fbec5bdb758732dc0658b86e275a9b46bacd58ae899", 1).toItemBuilder().displayName("§5§lWeinachtsmann Outfit").lore("§7Kategorie: §bKleidung / Outfits", "§7Seltenheit: §5Episch", "", "§7Wer möchte Geschenke").create()),

    //Trails
    TRAIL_COOKIES(121, "Cookie-Trail", Category.TRAIL, Level.UNUSUAL, 60,35, new ItemBuilder(Material.COOKIE, 1, 0).displayName("§3§lCookie-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §3Ungewöhnlich", "", "§7Spiel das Krümmel Monster").create()),
    TRAIL_GLOW(122, "Glow-Trail", Category.TRAIL, Level.UNUSUAL, 60,35, new ItemBuilder(Material.GLOWSTONE_DUST, 1, 0).displayName("§3§lGlow-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §3Ungewöhnlich", "", "§7Spiele Millionär und zeige den anderen Spieler dein Vermögen").create()),
    TRAIL_ENDER(123, "Ender-Trail", Category.TRAIL, Level.UNUSUAL, 60, 35,new ItemBuilder(Material.ENDER_PEARL, 1, 0).displayName("§3§lEnder-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §3Ungewöhnlich", "", "§7Teleport Teleport ...").create()),
    TRAIL_MUSIC(124, "Musik-Trail", Category.TRAIL, Level.EPIC, 90, 55,new ItemBuilder(Material.JUKEBOX, 1, 0).displayName("§5§lMusik-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §5Episch", "", "§7Wap Bap").create()),
    TRAIL_HEART(125, "Herzen-Trail", Category.TRAIL, Level.USUAL, 90,55, new ItemBuilder(Material.REDSTONE, 1, 0).displayName("§7§lHerzen-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §7Gewöhnlich", "", "§7Liebe über Liebe").create()),
    TRAIL_LAVA(126, "Lava-Trail", Category.TRAIL, Level.EPIC, 90,55, new ItemBuilder(Material.LAVA_BUCKET, 1, 0).displayName("§3§lLava-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §3Ungewöhnlich", "", "§7HEIß HEIß").create()),
    TRAIL_SNOW(127, "Schnee-Trail", Category.TRAIL, Level.USUAL, 40,15, new ItemBuilder(Material.SNOW_BALL, 1, 0).displayName("§7§lSchnee-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §7Gewöhnlich", "", "§7Ist schon Weinachten?").create()),
    TRAIL_WATER(128, "Wasser-Trail", Category.TRAIL, Level.USUAL, 40,15, new ItemBuilder(Material.WATER_BUCKET, 1, 0).displayName("§7§lWasser-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §7Gewöhnlich", "", "§7Huu ist das kalt").create()),

    //Animals
    ANIMAL_PIG(150, "Schwein", Category.ANIMAL, Level.UNUSUAL, 110,60, Skull.fromUrl("http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4", 1).toItemBuilder().displayName("§3§lSchwein").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme dein Schwein mit auf reisen").create()),
    ANIMAL_WITHER(151, "Wither", Category.ANIMAL, Level.LEGENDARY, 160,90, new ItemBuilder(Material.SKULL_ITEM, 1, 1).displayName("§6§lWither").lore("§7Kategorie: §bTiere", "§7Seltenheit: §6Legendär", "", "§7Buhh").create()),
    ANIMAL_SHEEP(152, "Schaf", Category.ANIMAL, Level.UNUSUAL, 110,60, Skull.fromUrl("http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70", 1).toItemBuilder().displayName("§3§lSchaf").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme dein Schaf mit auf reisen").create()),
    ANIMAL_MUSHROOM_COW(153, "Pilz Kuh", Category.ANIMAL, Level.EPIC, 140,70, Skull.fromUrl("http://textures.minecraft.net/texture/d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db", 1).toItemBuilder().displayName("§5§lPilz Kuh").lore("§7Kategorie: §bTiere", "§7Seltenheit: §5Episch", "", "§7Nehme deine Pilz Kuh mit auf reisen").create()),
    ANIMAL_CHICKEN(154, "Huhn", Category.ANIMAL, Level.UNUSUAL, 110,60, Skull.fromUrl("http://textures.minecraft.net/texture/1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", 1).toItemBuilder().displayName("§3§lHuhn").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme deine Huhn Kuh mit auf reisen").create()),
    ANIMAL_MAGMA(155, "Magma", Category.ANIMAL, Level.EPIC, 140,70, Skull.fromUrl("http://textures.minecraft.net/texture/ff111158a481fa6cad6e2ce298fa3b5fdb448c058b09cd57c28ab1ea9bd887", 1).toItemBuilder().displayName("§5§lMagma").lore("§7Kategorie: §bTiere", "§7Seltenheit: §5Episch", "", "§7Nehme dein Magma mit auf reisen").create()),
    ANIMAL_SPIDER(156, "Spinne", Category.ANIMAL, Level.UNUSUAL, 110,60, Skull.fromUrl("http://textures.minecraft.net/texture/cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1", 1).toItemBuilder().displayName("§3§lSpinne").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme deine Spinne mit auf reisen").create()),
    ANIMAL_RABBIT(157, "Hase", Category.ANIMAL, Level.UNUSUAL, 100,45, Skull.fromUrl("http://textures.minecraft.net/texture/dc7a317ec5c1ed7788f89e7f1a6af3d2eeb92d1e9879c05343c57f9d863de130", 1).toItemBuilder().displayName("§3§lHase").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme dein Hase mit auf reisen").create()),

    //Coin items (Chest opening)
    EMERALDS_20(171, "20+ Emeralds", null, Level.USUAL, 0,0, new ItemBuilder(Material.GOLD_INGOT, 1, 0).displayName("§7§lEmerald-Loot ").lore("§7Kategorie: §bEmeralds", "§7Seltenheit: §7Gewöhnlich").create()),
    EMERALDS_100(171, "100+ Emeralds", null, Level.UNUSUAL, 0, 0,new ItemBuilder(Material.GOLD_BLOCK, 1, 0).displayName("§3§lEmerald-Loot ").lore("§7Kategorie: §bEmeralds", "§7Seltenheit: §3Ungewöhnlich").create()),
    EMERALDS_250(171, "250+ Emeralds", null, Level.EPIC, 0, 0,new ItemBuilder(Material.DIAMOND_BLOCK, 1, 0).displayName("§5§lEmerald-Loot ").lore("§7Kategorie: §bEmeralds", "§7Seltenheit: §5Episch").create()),
    EMERALDS_400(171, "400+ Emeralds", null, Level.LEGENDARY, 0, 0, new ItemBuilder(Material.EMERALD_BLOCK, 1, 0).displayName("§6§lEmerald-Loot ").lore("§7Kategorie: §bEmeralds", "§7Seltenheit: §6Legendär").create()),

    //Coin material items
    MATERIAL_DIAMOND_4(180, "Material_dia_4", Category.MATERIAL, Level.UNUSUAL, 30,15, new ItemBuilder(Material.DIAMOND, 4, 0).displayName("§5§l4 Diamanten").lore("§7Kategorie: §9Material", "§7Seltenheit: §5Episch", "", "§730 Coins", "", "§7Diamante zum bauen").create()),
    MATERIAL_IRON_2(181, "Material_iron_2", Category.MATERIAL, Level.UNUSUAL, 15,5, new ItemBuilder(Material.IRON_INGOT, 2, 0).displayName("§3§l2 Eisen").lore("§7Kategorie: §9Material", "§7Seltenheit: §3Ungewöhnlich", "", "§715 Coins", "", "§7Eisen zum bauen").create()),
    MATERIAL_IRON_4(182, "Material_iron_4", Category.MATERIAL, Level.UNUSUAL, 25,10,new ItemBuilder(Material.IRON_INGOT, 4, 0).displayName("§3§l4 Eisen").lore("§7Kategorie: §9Material", "§7Seltenheit: §3Ungewöhnlich", "", "§725 Coins", "", "§7Eisen zum bauen").create()),
    MATERIAL_IRON_6(183, "Material_iron_6", Category.MATERIAL, Level.UNUSUAL, 35,20, new ItemBuilder(Material.IRON_INGOT, 6, 0).displayName("§3§l6 Eisen").lore("§7Kategorie: §9Material", "§7Seltenheit: §3Ungewöhnlich", "", "§735 Coins", "", "§7Eisen zum bauen").create()),
    MATERIAL_IRON_8(184, "Material_iron_8", Category.MATERIAL, Level.UNUSUAL, 45,30, new ItemBuilder(Material.IRON_INGOT, 8, 0).displayName("§3§l8 Eisen").lore("§7Kategorie: §9Material", "§7Seltenheit: §3Ungewöhnlich", "", "§745 Coins", "", "§7Eisen zum bauen").create()),
    MATERIAL_IRON_10(185, "Material_iron_10", Category.MATERIAL, Level.EPIC, 55,40,new ItemBuilder(Material.IRON_INGOT, 10, 0).displayName("§5§l10 Eisen").lore("§7Kategorie: §9Material", "§7Seltenheit: §5Episch", "", "§755 Coins", "", "§7Eisen zum bauen").create()),

    //Boots
    PREMIUM_BOOTS(187, "Premium Schuhe", Category.EXCLUSIVE, Level.USUAL, 0, 0,ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.ORANGE).displayName("§7§lPremium Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §7Gewöhnlich", "", "§7Premium Boots").create()),
    PREMIUM_PLUS_BOOTS(188, "Premium+ Schuhe", Category.EXCLUSIVE, Level.UNUSUAL, 0,0, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.ORANGE).displayName("§3§lPremium+ Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §3Ungewöhnlich", "", "§7Premium+ Boots").create()),
    YOUTUBER_BOOTS(189, "Youtuber Schuhe", Category.EXCLUSIVE, Level.EPIC, 0,0, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.PURPLE).displayName("§5§lYoutuber Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §5Episch", "", "§7Youtuber Boots").create()),
    JR_SUPPORTER_BOOTS(190, "Jr Supporter Schuhe", Category.EXCLUSIVE, Level.MYSTICAL, 0,0, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.LIME).displayName("§c§lJunior Supporter Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Junior Supporter Boots").create()),
    SUPPORTER_BOOTS(191, "Supporter Schuhe", Category.EXCLUSIVE, Level.MYSTICAL, 0,0, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.LIME).displayName("§c§lSupporter Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Supporter Boots").create()),
    MODERATOR_BOOTS(192, "Moderator Schuhe", Category.EXCLUSIVE, Level.MYSTICAL, 0,0, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.OLIVE).displayName("§c§lModerator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Moderator Boots").create()),
    SR_MODERATOR_BOOTS(193, "Senior Moderator Schuhe", Category.EXCLUSIVE, Level.MYSTICAL, 0,0, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.OLIVE).displayName("§c§lSenior Moderator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Senior Moderator Boots").create()),
    BUILDER_BOOTS(194, "Builder Schuhe", Category.EXCLUSIVE, Level.MYSTICAL, 0,0, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.YELLOW).displayName("§c§lBuilder Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Builder Boots").create()),
    DEVELOPER_BOOTS(195, "Developer Schuhe", Category.EXCLUSIVE, Level.MYSTICAL, 0, 0,ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.AQUA).displayName("§c§lDeveloper Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7Nike Developer Ultra Boost").create()),
    ADMIN_BOOTS(196, "Admin Schuhe", Category.EXCLUSIVE, Level.MYSTICAL, 0,0, ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.RED).displayName("§c§lAdminstrator Schuhe").lore("§7Kategorie: §bExklusives Item", "§7Seltenheit: §cMythisch", "", "§7ADMIN SCHUHE ICH BIN DER BOSS").create()),

    //Armor
    IRON_SWORD(226, "eisenschwert", Category.ARMOR, Level.UNUSUAL, 0,0, new ItemBuilder(Material.IRON_SWORD, 1, 0).displayName("§3§lEisen Schwerd").lore("§7Kategorie: §bSchwerter", "§7Seltenheit: §3Ungewöhnlich", "§74 Eisen", "§7Mit dem Eisen Schwerdt in den Krieg").create()),
    IRON_HEAD(227, "eisenhelm", Category.ARMOR, Level.UNUSUAL, 0,0, new ItemBuilder(Material.IRON_HELMET, 1, 0).displayName("§3§lEisen Helm").lore("§7Kategorie: §bRüstung", "§7Seltenheit: §3Ungewöhnlich", "§72 Eisen", "§7Mit dem Eisen Helm in den Krieg").create()),
    IRON_CHESTPLATE(228, "eisenbrustpanzer", Category.ARMOR, Level.UNUSUAL, 0,0, new ItemBuilder(Material.IRON_CHESTPLATE, 1, 0).displayName("§3§lEisen Brustpanzer").lore("§7Kategorie: §bRüstung", "§7Seltenheit: §3Ungewöhnlich", "§76 Eisen", "§7Mit der Eisen Platte in den Krieg").create()),
    IRON_LEGGINS(229, "eisenhose", Category.ARMOR, Level.UNUSUAL, 0,0, new ItemBuilder(Material.IRON_LEGGINGS, 1, 0).displayName("§3§lEisen Hose").lore("§7Kategorie: §bRüstung", "§7Seltenheit: §3Ungewöhnlich", "§74 Eisen", "§7Mit der Eisen Hose in den Krieg").create()),
    IRON_BOOTS(230, "eisenstiefel", Category.ARMOR, Level.UNUSUAL, 0,0, new ItemBuilder(Material.IRON_BOOTS, 1, 0).displayName("§3§lEisen Schuhe").lore("§7Kategorie: §bRüstung", "§7Seltenheit: §3Ungewöhnlich", "§74 Eisen", "§7Mit den Eisen Schuhe in den Krieg").create()),

    //Ingame Items
    BEDROCK_CB(245, "Bedrock-block", Category.INGAME, Level.LEGENDARY, 0,120, new ItemBuilder(Material.BEDROCK, 1, 0).displayName("§6§lBedrock").lore("§7Kategorie: §bIngame Item", "§7Seltenheit: §6Legendär", "", "§7Ich baue ab und er zerbricht nicht!").create()),
    ENDSTONE_CB(246, "End-stone-block", Category.INGAME, Level.EPIC, 0,100, new ItemBuilder(Material.ENDER_STONE, 1, 0).displayName("§6§lBedrock").lore("§7Kategorie: §bIngame Item", "§7Seltenheit: §6Legendär", "", "§7Ich baue ab und er zerbricht nicht!").create()),
    SPAWNER_CB(247, "Mob-spawner-block", Category.INGAME, Level.LEGENDARY, 0,120, new ItemBuilder(Material.MOB_SPAWNER, 1, 0).displayName("§6§lMob Spawner").lore("§7Kategorie: §bIngame Item", "§7Seltenheit: §6Legendär", "", "§7Ohh da ist was gespawnt!").create()),
    DRAGON_EGG_CB(248, "Drachen-ei-block", Category.INGAME, Level.LEGENDARY, 0,120, new ItemBuilder(Material.DRAGON_EGG, 1, 0).displayName("§6§lDrachen Ei").lore("§7Kategorie: §bIngame Item", "§7Seltenheit: §6Legendär", "", "§7Das Ei ist wunderschön!").create()),
    BEACON_CB(249, "Beacon-block", Category.INGAME, Level.EPIC, 0,100, new ItemBuilder(Material.ENDER_STONE, 1, 0).displayName("§6§Mob Spawner").lore("§7Kategorie: §bIngame Item", "§7Seltenheit: §6Legendär", "", "§7Ich baue ab und er zerbricht nicht!").create()),
    BARRIERE_CB(247, "Barriere-block", Category.INGAME, Level.LEGENDARY, 120,0, new ItemBuilder(Material.BARRIER, 1, 0).displayName("§6§lBarriere").lore("§7Kategorie: §bIngame Item", "§7Seltenheit: §6Legendär", "", "§7Ich sehe den Block garnicht!").create()),
    END_PORTAL_CB(251, "End-portal-block", Category.INGAME, Level.LEGENDARY, 0,120, new ItemBuilder(Material.ENDER_PORTAL_FRAME, 1, 0).displayName("§6§lEnd Portal Rahmen").lore("§7Kategorie: §bIngame Item", "§7Seltenheit: §6Legendär", "", "§7Ohh wie cool ein Rahmen!").create()),


    SUPER_SWORD(260, "Super-sword", Category.INGAME, Level.MYSTICAL, 0, 140, new ItemBuilder(Material.DIAMOND_SWORD, 1, 0).displayName("§c§lSuper-Schwert").lore("§7Kategorie: §bIngame Item", "§7Seltenheit: §cMythisch", "", "§7Einer der gefährlichsten Schwerter der Welt!").create());

    @Getter
    private int id, buyemeralds, sellemeralds;
    @Getter
    private Category category;
    @Getter
    private Level level;
    @Getter
    private String name;
    @Getter
    private ItemStack itemStack;

    Item(int id, String name, Category category, Level level, int buyemeralds, int sellemeralds, ItemStack itemStack) {
        this.id = id;
        this.buyemeralds = buyemeralds;
        this.sellemeralds = sellemeralds;

        this.category = category;
        this.level = level;
        this.name = name;
        this.itemStack = itemStack;
    }

    public boolean hasCategory() {
        return category != null;
    }

    public static Item getItemByID(int id) {
        for (Item i : values()) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }
}
