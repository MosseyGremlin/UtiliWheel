package com.mosseygremlin.utiliwheel.util;

import net.minecraft.client.Minecraft;

public class DeferredActions {

    private static Runnable pendingAction;
    private static int remainingTicks;

    public static void queue(Runnable action, int ticks) {

        if (action == null) {
            return;
        }

        pendingAction = action;
        remainingTicks = Math.max(0, ticks);
    }

    public static void tick(Minecraft minecraft) {

        if (pendingAction == null) {
            return;
        }

        if (minecraft.screen != null) {
            return;
        }

        if (remainingTicks > 0) {
            remainingTicks--;
            return;
        }

        Runnable action = pendingAction;
        pendingAction = null;

        action.run();
    }

}
