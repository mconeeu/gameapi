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
