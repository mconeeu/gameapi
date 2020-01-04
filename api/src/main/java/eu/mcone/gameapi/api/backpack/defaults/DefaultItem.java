/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.backpack.Level;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum DefaultItem {

    //Gadgets
    LOVEGUN(0, "LoveGun", DefaultCategory.GADGET, Level.USUAL, 50,20, new ItemBuilder(Material.REDSTONE, 1, 0).displayName("§7§lLove Gun").lore("§7Kategorie: §bGadget", "§7Seltenheit: §7Gewöhnlich", "", "", "§7Herze über Herze").create()),
    EASTERGUN(1, "OsterGun", DefaultCategory.GADGET, Level.UNUSUAL, 80,50, new ItemBuilder(Material.FIREWORK_CHARGE, 1, 0).displayName("§3§lOster Gun").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7Schieße das Osterfieber dürch die Lobby!").create()),
    SNOWGUN(2, "SnowGun", DefaultCategory.GADGET, Level.EPIC, 120,70, new ItemBuilder(Material.WOOD_HOE, 1, 0).displayName("§5§lSnowGun").lore("§7Kategorie: §bGadget", "§7Seltenheit: §5Episch", "", "", "§7Schieße das Weinachtfieber dürch die Lobby!").create()),
    ENDERPEARL(3, "Ender Perle", DefaultCategory.GADGET, Level.USUAL, 40, 15, new ItemBuilder(Material.ENDER_PEARL, 1, 0).displayName("§7§lEnder Perle").lore("§7Kategorie: §bGadget", "§7Seltenheit: §7Gewöhnlich", "", "", "§7Telepotiere dich hin und her").create()),
    COINBOMB(4, "CoinBombe", DefaultCategory.GADGET, Level.UNUSUAL, 80,50, new ItemBuilder(Material.IRON_INGOT).displayName("§3§lCoin Bombe").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7COIIIIIINSSS FÜR ALLE").create()),
    BOMB(5, "Bombe", DefaultCategory.GADGET, Level.UNUSUAL, 80,50, new ItemBuilder(Material.MAGMA_CREAM, 1).displayName("§3§lBombe").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7Achtung Sprengung!").create()),
    GRAPPLING_HOOK(6, "Enterhacken", DefaultCategory.GADGET, Level.EPIC, 120,70, new ItemBuilder(Material.FISHING_ROD, 1, 0).displayName("§3§lEnterhacken").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7Fühle dich wie Spiderman!").unbreakable(true).itemFlags(ItemFlag.HIDE_UNBREAKABLE).create()),
    COBWEBGUN(7, "Spinnenwebengun", DefaultCategory.GADGET, Level.EPIC, 110, 80, new ItemBuilder(Material.BONE, 1, 0).displayName("§5§lSpinnenweben Gun").lore("§7Kategorie: §bGadget", "§7Seltenheit: §5Episch", "", "", "§7Schieße grussellig Spinnenweben durch die Insel").create()),
    SPLASH_POTION(8, "Hexen Wurf Trank", DefaultCategory.GADGET, Level.UNUSUAL, 70, 45, new ItemBuilder(Material.POTION, 1, 16430).displayName("§3§lGeister Trank").lore("§7Kategorie: §bGadget", "§7Seltenheit: §3Ungewöhnlich", "", "", "§7Mache dich unsichtbar und erschrecke\nAhnungslose Spieler!").create()),

    //Heads
    HEAD_PALUTEN(0, "Palutens Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/33121196cf12ee65354016861da064948d4ce0912f662bca2e2a6b2a932038", 1).toItemBuilder().displayName("§5§lPalutens Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "§7Rang:§5 Youtuber", "", "§7Edgar bist du da ??").create()),
    HEAD_DNER(1, "Dners Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/6092d4ea3448f3b2fbf355fcdfa2d36e51b2587a09e41e58eaa51e3daad4de5", 1).toItemBuilder().displayName("§5§lDners Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "§7Rang:§5 Youtuber", "", "§7Spielkind Zeit").create()),
    HEAD_RUFI(2, "Rufis Kopf", DefaultCategory.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/367ca878127ae2268b954dc3b32c6f22ed3a45937a843f149e268cb273fa", 1).toItemBuilder().displayName("§6§lRufis Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "§7Rang:§4 Admin", "", "§7Hat wer probleme?").create()),
    HEAD_TWINSTER(3, "Twinsters Kopf", DefaultCategory.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/27693780b6406c86d228af5fff6cb389ad671be27c218332fdbbc9bb1c8839e0", 1).toItemBuilder().displayName("§6§lTwinsters Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "§7Rang:§4 Admin", "", "§7grüß god").create()),
    HEAD_DRMARV(4, "DrMarvs Kopf", DefaultCategory.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/71b22baec7d57a2e4a16895094faa972ca602085270de652485ce43802b15cda", 1).toItemBuilder().displayName("§6§lDrMarvs Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "§7Rang:§b Developer ", "", "§7I bims der DrMarv").create()),
    HEAD_POKEMON(5, "Pokemon Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80,95, Skull.fromUrl("http://textures.minecraft.net/texture/d43d4b7ac24a1d650ddf73bd140f49fc12d2736fc14a8dc25c0f3f29d85f8f", 1).toItemBuilder().displayName("§3§lPokémon Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Da ist ein wildes Pikachu").create()),
    HEAD_EYE(6, "Augen Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/2cef87772afd85b468f4c7fb9571e31435ef765ad413fe460262150423e2021", 1).toItemBuilder().displayName("§5§lAugen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Nur 1 Auge").create()),
    HEAD_GERMAN(7, "Deutschland Kopf", DefaultCategory.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f", 1).toItemBuilder().displayName("§7§lDeutschland Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Deutsches Vaterland").create()),
    HEAD_PISTON(8, "Kolben Kopf", DefaultCategory.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/aa868ce917c09af8e4c350a5807041f6509bf2b89aca45e591fbbd7d4b117d", 1).toItemBuilder().displayName("§7§lKolben Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Kolben raus Kolben rein").create()),
    HEAD_DEATH_SMILEY(9, "Toter Kopf", DefaultCategory.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/b371e4e1cf6a1a36fdae27137fd9b8748e6169299925f9af2be301e54298c73", 1).toItemBuilder().displayName("§7§lToter Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Booom Tot").create()),
    HEAD_SPION(10, "Spion Kopf", DefaultCategory.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/4fc1d88be2528168f67da16a19b14f04e1e4963a99dfcb4e49d984a351313c", 1).toItemBuilder().displayName("§6§lSpion Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "", "§7Tötet jeden der im weg ist!").create()),
    HEAD_MRMOREGAME(11, "MrMoreGames Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/ce3a8afbc57d27a63ed4aa2e4d1c2d05274b5f6a04e85ee1fae56e36b187ad", 1).toItemBuilder().displayName("§5§lMrMoreGames Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "§7Rang:§5 Youtuber", "", "§7Bin ich ein Mr. ??").create()),
    HEAD_HAPPY_SMILLEY(12, "Fröhlicher Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/3e1debc73231f8ed4b69d5c3ac1b1f18f3656a8988e23f2e1bdbc4e85f6d46a", 1).toItemBuilder().displayName("§7§lGlücklicher Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Ahh heute ist ein wundervoller Tag").create()),
    HEAD_MARIO(13, "Mario Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/dba8d8e53d8a5a75770b62cce73db6bab701cc3de4a9b654d213d54af9615", 1).toItemBuilder().displayName("§3§lMario Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Mario").create()),
    HEAD_SKYPE(14, "Skype Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/2ec182da7d3c0a8acc3be9b77c29be47e08c20b050b13fd4c4c7d71f66273", 1).toItemBuilder().displayName("§7§lSkype Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Beeepp Beeeepp").create()),
    HEAD_BEDROCK(15, "Bedrock Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/36d1fabdf3e342671bd9f95f687fe263f439ddc2f1c9ea8ff15b13f1e7e48b9", 1).toItemBuilder().displayName("§7§lBedrock Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Bewegt sich nix").create()),
    HEAD_GIFT(16, "Geschenk Kopf", DefaultCategory.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/f0afa4fffd10863e76c698da2c9c9e799bcf9ab9aa37d8312881734225d3ca", 1).toItemBuilder().displayName("§7§lGeschenk Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Herzlichen Glückwunsch").create()),
    HEAD_YOUTUBE(17, "Youtube Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/b4353fd0f86314353876586075b9bdf0c484aab0331b872df11bd564fcb029ed", 1).toItemBuilder().displayName("§3§lYoutube Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Jaaa neues Video").create()),
    HEAD_LUIGI(18, "Luigi Kopf", DefaultCategory.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/ff1533871e49ddab8f1ca82edb1153a5e2ed3764fd1ce029bf829f4b3caac3", 1).toItemBuilder().displayName("§7§lLuigi Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Mario wo bist den du ??").create()),
    HEAD_BEE(19, "Bienen Kopf", DefaultCategory.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/947322f831e3c168cfbd3e28fe925144b261e79eb39c771349fac55a8126473", 1).toItemBuilder().displayName("§7§lBienen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7 Bwwwww pix...  Ahhhhh!!").create()),
    HEAD_RABBIT(20, "Hasen Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/dc7a317ec5c1ed7788f89e7f1a6af3d2eeb92d1e9879c05343c57f9d863de130", 1).toItemBuilder().displayName("§7§lHasen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7 Hier ein Ei da ein Ei").create()),
    HEAD_PIG(21, "Edgars Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4", 1).toItemBuilder().displayName("§5§lEdgars Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Paluten wo bist du ??").create()),
    HEAD_TUBILP(22, "Tubis Kopf", DefaultCategory.HAT, Level.USUAL, 25,20, Skull.fromUrl("http://textures.minecraft.net/texture/ee5c31f12b34652e5ca5f8dc94b7a15476d10528f7a714304de7c398da", 1).toItemBuilder().displayName("§7§lTubis Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Bin ich so billig oder wieso will mich jeder haben?").create()),
    HEAD_UNGESPIELT(23, "Ungespielts Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/74b0a3b34ae1d736bc0620fa1f1d8766e63a7f6f326d3271e8f3b1958aec75", 1).toItemBuilder().displayName("§5§lUngespielts Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "§7Rang:§5 Youtuber ", "", "§7Milch ist Gift!").create()),
    HEAD_MONSTER(24, "Monster Kopf", DefaultCategory.HAT, Level.LEGENDARY, 155,95, Skull.fromUrl("http://textures.minecraft.net/texture/c73ad1ebeb9b7525708a933bdae086599a8dcd66d8b414531ce63bf9953bd3e", 1).toItemBuilder().displayName("§6§lMonster Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §6Legendär", "", "§7Arhhhhh").create()),
    HEAD_DARTH_VADER(25, "Dath Vader Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/c1c3e1f224b446ccac6a6cc3cd9891019a122f99691c3907992a3af99a21b0", 1).toItemBuilder().displayName("§3§lDarth Vader Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Mit dir geht die schwarze Macht!").create()),
    HEAD_KISS_SMILEY(26, "Kuss Kopf", DefaultCategory.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/545bd18a2aaf469fad72e52cde6cfb02bfbaa5bfed2a8151277f779ebcdcec1", 1).toItemBuilder().displayName("§7§lKuss Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Muuuu aaa!").create()),
    HEAD_CHICKEN(27, "Huhn Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", 1).toItemBuilder().displayName("§7§lHuhn Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Gagagaga!").create()),
    HEAD_MUSHROOM_COW(28, "Pilz Kopf", DefaultCategory.HAT, Level.USUAL, 50,20, Skull.fromUrl("http://textures.minecraft.net/texture/d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db", 1).toItemBuilder().displayName("§7§lPilzKuh Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Muhh").create()),
    HEAD_PANDA(29, "Panda Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20, Skull.fromUrl("http://textures.minecraft.net/texture/d188c980aacfa94cf33088512b1b9517ba826b154d4cafc262aff6977be8a", 1).toItemBuilder().displayName("§7§lPanda Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Panda Musik thick thack").create()),
    HEAD_COMPUTER(30, "Computer Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/45e9bd425fbd39a94c30bf5aeb301db186713322761fc82a76fb6168793490", 1).toItemBuilder().displayName("§7§lComputer Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7FORTNITE SPIELEN...").create()),
    HEAD_BURGER(31, "Bürger Kopf", DefaultCategory.HAT, Level.UNUSUAL, 85,50, Skull.fromUrl("http://textures.minecraft.net/texture/a6ef1c25f516f2e7d6f7667420e33adcf3cdf938cb37f9a41a8b35869f569b", 1).toItemBuilder().displayName("§3§lBürger Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§750 Jahre BigMac").create()),
    HEAD_DISCO_BALL(32, "Disco Kugel Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80, 50, Skull.fromUrl("http://textures.minecraft.net/texture/b462ddfa553ce78683be477b8d8654f3dfc3aa2969808478c987ab88c376a0", 1).toItemBuilder().displayName("§3§lDisco Kugel Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Tanz Baby Tanz").create()),
    HEAD_LISTENING_SMILEY(33, "Lauschender Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/3baabe724eae59c5d13f442c7dc5d2b1c6b70c2f83364a488ce5973ae80b4c3", 1).toItemBuilder().displayName("§3§lLauschender Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Aha du hast 1 Millionen Euro auf der Bank").create()),
    HEAD_MELON(34, "Melonen Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/c3fed514c3e238ca7ac1c94b897ff6711b1dbe50174afc235c8f80d029", 1).toItemBuilder().displayName("§7§lMelonen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Hohl wie Bedrock").create()),
    HEAD_PENGUIN(35, "Pinguin Kopf", DefaultCategory.HAT, Level.USUAL, 80, 50,Skull.fromUrl("http://textures.minecraft.net/texture/d3c57facbb3a4db7fd55b5c0dc7d19c19cb0813c748ccc9710c714727551f5b9", 1).toItemBuilder().displayName("§7§lPinguin Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Bohr ist das warm hier!").create()),
    HEAD_JOKER(36, "Joker Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/af4f6825ef6d5e46d794697d1bf86d144bf6fb3da4e55f7cf55271f637eaa7", 1).toItemBuilder().displayName("§5§lJoker Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Wo ist Batman!").create()),
    HEAD_SHEEP(37, "Schaf Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70", 1).toItemBuilder().displayName("§7§lSchaf Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Mahh").create()),
    HEAD_VILLAGER(38, "Villager Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80, 50,Skull.fromUrl("http://textures.minecraft.net/texture/822d8e751c8f2fd4c8942c44bdb2f5ca4d8ae8e575ed3eb34c18a86e93b", 1).toItemBuilder().displayName("§3§lVillager Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Will wer was kaufen").create()),
    HEAD_GARADOS(39, "Garados Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80, 50,Skull.fromUrl("http://textures.minecraft.net/texture/1ab93af668cb83e379e9edbcdc4532f1294f90cb13de6a582efab87696c36dd", 1).toItemBuilder().displayName("§3§lGarados Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Pokémon sind cool").create()),
    HEAD_PORTAL(40, "Portal Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/a4a319deafefd6adb37f21449ea56d3ea5a83857fb9616fa7d4f9ea625177", 1).toItemBuilder().displayName("§7§lPortal Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Rein in das Portal").create()),
    HEAD_GHOST(41, "Ghost Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/23c71a85eeb3cd6449159675aa89278a2a1d587b4d0b768174fc2e15c9be4d", 1).toItemBuilder().displayName("§5§lGeist Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Uhh hier sind Geister").create()),
    HEAD_CRYING(42, "Wein Kopf", DefaultCategory.HAT, Level.USUAL, 50, 20,Skull.fromUrl("http://textures.minecraft.net/texture/1f1b875de49c587e3b4023ce24d472ff27583a1f054f37e73a1154b5b5498", 1).toItemBuilder().displayName("§7§lWein Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §7Gewöhnlich", "", "§7Ahhhh ich bin so traurig").create()),
    HEAD_DINOSAUR(43, "Dino Kopf", DefaultCategory.HAT, Level.EPIC, 120,70, Skull.fromUrl("http://textures.minecraft.net/texture/d582ce1d9f6f34c087b4fbec5bdb758732dc0658b86e275a9b46bacd58ae899", 1).toItemBuilder().displayName("§5§lDino Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7UAHHHHH").create()),
    HEAD_PLAYCUBEHD(44, "Playcubes Kopf", DefaultCategory.HAT, Level.UNUSUAL, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/ff747a6c6757cc7720059d7f5cdbbc32d6d45e1984262d11a92213f1812a4d0e", 1).toItemBuilder().displayName("§3§lPlaycubes Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7Will wer GTA").create()),
    HEAD_SANTA(45, "Weinachtsmann Kopf", DefaultCategory.HAT, Level.UNUSUAL, 85,55, Skull.fromUrl("http://textures.minecraft.net/texture/df783877fc2581ead734847a6cea5bfd6e23939616b1e004459332b5b3933bcd", 1).toItemBuilder().displayName("§3§lWeinachtsmann Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §3Ungewöhnlich", "", "§7HoHo wo ist mein Roter Mantel?").create()),

    //Gadget Heads
    HEAD_SECRET_STRIPCLUB(60, "Stripclub Kopf", DefaultCategory.HAT, Level.EPIC, 80,50, Skull.fromUrl("http://textures.minecraft.net/texture/36aae86da0cd317a47fa6668fd4785b5a7a7e4ed9e7bc68652bae27984b84c", 1).toItemBuilder().displayName("§5§lLeichen Kopf").lore("§7Kategorie: §bKopf", "§7Seltenheit: §5Episch", "", "§7Kirphas 100 Jahre alter Kopf").create()),

    //Outfits
    OUTFIT_RABBIT(0, "Hasen Outfit", DefaultCategory.OUTFIT, Level.UNUSUAL, 125,75, Skull.fromUrl("http://textures.minecraft.net/texture/dc7a317ec5c1ed7788f89e7f1a6af3d2eeb92d1e9879c05343c57f9d863de130", 1).toItemBuilder().displayName("§3§lHasen Outfit").lore("§7Kategorie: §bKleidung / Outfits", "§7Seltenheit: §3Ungewöhnlich", "", "§7Bringe die Eier raus").create()),
    OUTFIT_DINOSAUR(1, "Dino Outfit", DefaultCategory.OUTFIT, Level.EPIC, 145,85, Skull.fromUrl("http://textures.minecraft.net/texture/d582ce1d9f6f34c087b4fbec5bdb758732dc0658b86e275a9b46bacd58ae899", 1).toItemBuilder().displayName("§5§lDino Outfit").lore("§7Kategorie: §bKleidung / Outfits", "§7Seltenheit: §5Episch", "", "§7AUUHHHH").create()),
    OUTFIT_SANTA(2, "Weinachstmann Outfit", DefaultCategory.OUTFIT, Level.EPIC, 145,85, Skull.fromUrl("http://textures.minecraft.net/texture/d582ce1d9f6f34c087b4fbec5bdb758732dc0658b86e275a9b46bacd58ae899", 1).toItemBuilder().displayName("§5§lWeinachtsmann Outfit").lore("§7Kategorie: §bKleidung / Outfits", "§7Seltenheit: §5Episch", "", "§7Wer möchte Geschenke").create()),

    //Trails
    TRAIL_COOKIES(0, "Cookie-Trail", DefaultCategory.TRAIL, Level.UNUSUAL, 60,35, new ItemBuilder(Material.COOKIE, 1, 0).displayName("§3§lCookie-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §3Ungewöhnlich", "", "§7Spiel das Krümmel Monster").create()),
    TRAIL_GLOW(1, "Glow-Trail", DefaultCategory.TRAIL, Level.UNUSUAL, 60,35, new ItemBuilder(Material.GLOWSTONE_DUST, 1, 0).displayName("§3§lGlow-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §3Ungewöhnlich", "", "§7Spiele Millionär und zeige den anderen Spieler dein Vermögen").create()),
    TRAIL_ENDER(2, "Ender-Trail", DefaultCategory.TRAIL, Level.UNUSUAL, 60, 35,new ItemBuilder(Material.ENDER_PEARL, 1, 0).displayName("§3§lEnder-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §3Ungewöhnlich", "", "§7Teleport Teleport ...").create()),
    TRAIL_MUSIC(3, "Musik-Trail", DefaultCategory.TRAIL, Level.EPIC, 90, 55,new ItemBuilder(Material.JUKEBOX, 1, 0).displayName("§5§lMusik-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §5Episch", "", "§7Wap Bap").create()),
    TRAIL_HEART(4, "Herzen-Trail", DefaultCategory.TRAIL, Level.USUAL, 90,55, new ItemBuilder(Material.REDSTONE, 1, 0).displayName("§7§lHerzen-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §7Gewöhnlich", "", "§7Liebe über Liebe").create()),
    TRAIL_LAVA(5, "Lava-Trail", DefaultCategory.TRAIL, Level.EPIC, 90,55, new ItemBuilder(Material.LAVA_BUCKET, 1, 0).displayName("§3§lLava-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §3Ungewöhnlich", "", "§7HEIß HEIß").create()),
    TRAIL_SNOW(6, "Schnee-Trail", DefaultCategory.TRAIL, Level.USUAL, 40,15, new ItemBuilder(Material.SNOW_BALL, 1, 0).displayName("§7§lSchnee-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §7Gewöhnlich", "", "§7Ist schon Weinachten?").create()),
    TRAIL_WATER(7, "Wasser-Trail", DefaultCategory.TRAIL, Level.USUAL, 40,15, new ItemBuilder(Material.WATER_BUCKET, 1, 0).displayName("§7§lWasser-Trail").lore("§7Kategorie: §bTrails", "§7Seltenheit: §7Gewöhnlich", "", "§7Huu ist das kalt").create()),

    //Animals
    ANIMAL_PIG(0, "Schwein", DefaultCategory.PET, Level.UNUSUAL, 110,60, Skull.fromUrl("http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4", 1).toItemBuilder().displayName("§3§lSchwein").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme dein Schwein mit auf reisen").create()),
    ANIMAL_WITHER(1, "Wither", DefaultCategory.PET, Level.LEGENDARY, 160,90, new ItemBuilder(Material.SKULL_ITEM, 1, 1).displayName("§6§lWither").lore("§7Kategorie: §bTiere", "§7Seltenheit: §6Legendär", "", "§7Buhh").create()),
    ANIMAL_SHEEP(2, "Schaf", DefaultCategory.PET, Level.UNUSUAL, 110,60, Skull.fromUrl("http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70", 1).toItemBuilder().displayName("§3§lSchaf").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme dein Schaf mit auf reisen").create()),
    ANIMAL_MUSHROOM_COW(3, "Pilz Kuh", DefaultCategory.PET, Level.EPIC, 140,70, Skull.fromUrl("http://textures.minecraft.net/texture/d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db", 1).toItemBuilder().displayName("§5§lPilz Kuh").lore("§7Kategorie: §bTiere", "§7Seltenheit: §5Episch", "", "§7Nehme deine Pilz Kuh mit auf reisen").create()),
    ANIMAL_CHICKEN(4, "Huhn", DefaultCategory.PET, Level.UNUSUAL, 110,60, Skull.fromUrl("http://textures.minecraft.net/texture/1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", 1).toItemBuilder().displayName("§3§lHuhn").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme deine Huhn Kuh mit auf reisen").create()),
    ANIMAL_MAGMA(5, "Magma", DefaultCategory.PET, Level.EPIC, 140,70, Skull.fromUrl("http://textures.minecraft.net/texture/ff111158a481fa6cad6e2ce298fa3b5fdb448c058b09cd57c28ab1ea9bd887", 1).toItemBuilder().displayName("§5§lMagma").lore("§7Kategorie: §bTiere", "§7Seltenheit: §5Episch", "", "§7Nehme dein Magma mit auf reisen").create()),
    ANIMAL_SPIDER(6, "Spinne", DefaultCategory.PET, Level.UNUSUAL, 110,60, Skull.fromUrl("http://textures.minecraft.net/texture/cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1", 1).toItemBuilder().displayName("§3§lSpinne").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme deine Spinne mit auf reisen").create()),
    ANIMAL_RABBIT(7, "Hase", DefaultCategory.PET, Level.UNUSUAL, 100,45, Skull.fromUrl("http://textures.minecraft.net/texture/dc7a317ec5c1ed7788f89e7f1a6af3d2eeb92d1e9879c05343c57f9d863de130", 1).toItemBuilder().displayName("§3§lHase").lore("§7Kategorie: §bTiere", "§7Seltenheit: §3Ungewöhnlich", "", "§7Nehme dein Hase mit auf reisen").create());

    @Setter
    private static BackpackManager manager;

    @Getter
    private int id, buyemeralds, sellemeralds;
    @Getter
    private DefaultCategory category;
    @Getter
    private Level level;
    @Getter
    private String name;
    @Getter
    private ItemStack itemStack;

    DefaultItem(int id, String name, DefaultCategory category, Level level, int buyemeralds, int sellemeralds, ItemStack itemStack) {
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

    public static DefaultItem getItemByID(DefaultCategory category, int id) {
        for (DefaultItem i : values()) {
            if (i.getCategory().equals(category) && i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    public boolean has(GamePlayer gp) {
        return gp.hasBackpackItem(category.name(), id);
    }

    public void add(GamePlayer gp) {
        gp.addBackpackItem(
                category.name(),
                manager.getBackpackItem(category.name(), id)
        );
    }

    public void remove(GamePlayer gp) {
        gp.removeBackpackItem(
                category.name(),
                manager.getBackpackItem(category.name(), id)
        );
    }

}
