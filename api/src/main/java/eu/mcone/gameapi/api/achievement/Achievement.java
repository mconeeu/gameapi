/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.achievement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {

    private String name, description;
    private int rewardCoins;

    @Override
    public boolean equals(Object obj) {
        Achievement other = (Achievement) obj;

        return other.name.equals(name)
                && other.description.equals(description)
                && other.rewardCoins == rewardCoins;
    }

}
