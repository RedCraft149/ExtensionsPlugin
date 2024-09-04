package org.redcraft.extensions.block;

import org.bukkit.entity.Player;

public interface BlockBreakTime {
    double breakTime(Player player, UnifiedBlock block, double standard);

    static BlockBreakTime.Reduced CONSTANT(double time) {
        return () -> time;
    }

    static double UNSET = -1d; //every value d<0 is interpreted as unset
    static boolean isUnset(double d) {
        return d<0;
    }

    interface Reduced extends BlockBreakTime {
        double breakTime();
        default double breakTime(Player player, UnifiedBlock block, double standard) {
            return breakTime();
        }
    }
}
