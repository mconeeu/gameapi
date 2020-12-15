/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.onepass;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import lombok.Getter;

@Getter
public enum OnePassLevelAward {

    BREAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_BREAD.getId(),
            2,
            1,
            true
    ),


    SNAPCHAT(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_SNAPCHAT.getId(),
            7,
            1,
            true
    ),

    PORTAL_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_PORTAL.getId(),
            13,
            1,
            true
    ),

    GRUSEL_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_SPOOKY_MASK.getId(),
            17,
            1,
            true
    ),

    GRAPPLING_HOOK(
            DefaultCategory.GADGET.name(),
            DefaultItem.GRAPPLING_HOOK.getId(),
            23,
            1,
            true
    ),



    /* PREMIUM */



    TWITTER(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_TWITTER.getId(),
            1,
            1,
            false
    ),
    GIFT(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_GIFT.getId(),
            2,
            1,
            false
    ),
    BOAT(
            DefaultCategory.GADGET.name(),
            DefaultItem.BOAT.getId(),
            3,
            1,
            false
    ),
    COW(
            DefaultCategory.PET.name(),
            DefaultItem.ANIMAL_MUSHROOM_COW.getId(),
            4,
            1,
            false
    ),
    COOKIE_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_COOKIE.getId(),
            5,
            1,
            false
    ),
    COOKIE_OUTIFIT(
            DefaultCategory.OUTFIT.name(),
            DefaultItem.OUTIFT_KRUEMEL_MONSTER.getId(),
            6,
            1,
            false
    ),
    COOKIE_TRAIL(
            DefaultCategory.TRAIL.name(),
            DefaultItem.TRAIL_COOKIES.getId(),
            7,
            1,
            false
    ),
    PANDA_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_PANDA.getId(),
            8,
            1,
            false
    ),
    PLAYCUBE_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_PLAYCUBEHD.getId(),
            9,
            1,
            false
    ),
    COMPUTER_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_COMPUTER.getId(),
            10,
            1,
            false
    ),
    POTION_GADGET(
            DefaultCategory.GADGET.name(),
            DefaultItem.SPLASH_POTION.getId(),
            11,
            1,
            false
    ),
    SNOW_TRAIL(
            DefaultCategory.TRAIL.name(),
            DefaultItem.TRAIL_SNOW.getId(),
            12,
            1,
            false
    ),
    LUIGI(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_LUIGI.getId(),
            13,
            1,
            false
    ),
    PIG(
            DefaultCategory.PET.name(),
            DefaultItem.ANIMAL_PIG.getId(),
            14,
            1,
            false
    ),
    RABBIT_OUTFIT(
            DefaultCategory.OUTFIT.name(),
            DefaultItem.OUTFIT_RABBIT.getId(),
            15,
            1,
            false
    ),
    HEAD_YOUTUBE(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_YOUTUBE.getId(),
            16,
            1,
            false
    ),
    SMILEY_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_LISTENING_SMILEY.getId(),
            17,
            1,
            false
    ),
    LOVE_GADGET(
            DefaultCategory.GADGET.name(),
            DefaultItem.LOVEGUN.getId(),
            18,
            1,
            false
    ),
    GIFT_HEADD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_GIFT.getId(),
            19,
            1,
            false
    ),
    BEE_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_BEE.getId(),
            19,
            1,
            false
    ),
    UP_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_UP.getId(),
            20,
            1,
            false
    ),
    KISS_HEAD(
            DefaultCategory.HAT.name(),
            DefaultItem.HEAD_KISS_SMILEY.getId(),
            21,
            1,
            false
    ),
    PET_SHEEP(
            DefaultCategory.PET.name(),
            DefaultItem.ANIMAL_SHEEP.getId(),
            22,
            1,
            false
    ),
    DINO_OUTIFT(
            DefaultCategory.OUTFIT.name(),
            DefaultItem.OUTFIT_DINOSAUR.getId(),
            23,
            1,
            false
    ),

    CARPET(
            DefaultCategory.GADGET.name(),
            DefaultItem.FLY_CARPET.getId(),
            24,
            1,
            false
    );


    private final String backpackItemCategory;
    private final int backPackItemId, level, chapter;
    private final boolean freeAward;

    OnePassLevelAward(String backpackItemCategory, int backPackItemId, int level, int chapter, boolean freeAward) {
        this.backpackItemCategory = backpackItemCategory;
        this.backPackItemId = backPackItemId;
        this.level = level;
        this.chapter = chapter;
        this.freeAward = freeAward;
    }

    public BackpackItem getBackpackItem() {
        return GamePlugin.getGamePlugin().getBackpackManager().getBackpackItem(
                backpackItemCategory,
                backPackItemId
        );
    }

}
