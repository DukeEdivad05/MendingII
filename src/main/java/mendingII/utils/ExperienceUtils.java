package mendingII.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExperienceUtils {
    public static int totalXpPoints(PlayerEntity playerEntity) {
        int level = playerEntity.experienceLevel;
        int xpPoints = 0;
        for (int a = 0; a < level; a++) {
            if (a >= 30) {
                xpPoints += 112 + (a - 30) * 9;
            } else {
                xpPoints += a >= 15 ? 37 + (a - 15) * 5 : 7 + a * 2;
            }
        }
        xpPoints += playerEntity.experienceProgress * playerEntity.getNextLevelExperience();
        return xpPoints;
    }
}
