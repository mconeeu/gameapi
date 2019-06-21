/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameSettingsConfig {

    private String lobbyWorld = "world";
    private int teams = 2;
    private int maximalPlayers = 2;
    private int minimalPlayers = 2;
    private int playersPreTeam = 1;
}
