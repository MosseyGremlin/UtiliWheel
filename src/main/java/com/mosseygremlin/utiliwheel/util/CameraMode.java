package com.mosseygremlin.utiliwheel.util;

import net.minecraft.client.Minecraft;

public class CameraMode {

    private static boolean active = false;

    private static int previousFov;
    private static boolean previousHideGui;
    private static boolean previousSmoothCamera;

    public static void toggle() {

        Minecraft minecraft = Minecraft.getInstance();

        if (!active) {
            enable(minecraft);
        } else {
            disable(minecraft);
        }
    }

    public static boolean isActive() {
        return active;
    }

    private static void enable(Minecraft minecraft) {

        previousFov =
                minecraft.options
                        .fov()
                        .get();

        previousHideGui =
                minecraft.options
                        .hideGui;

        previousSmoothCamera =
                minecraft.options
                        .smoothCamera;

        minecraft.options
                .fov()
                .set(30);

        minecraft.options.hideGui = true;
        minecraft.options.smoothCamera = true;

        minecraft.options.save();

        active = true;
    }

    private static void disable(Minecraft minecraft) {

        minecraft.options
                .fov()
                .set(previousFov);

        minecraft.options.hideGui = previousHideGui;
        minecraft.options.smoothCamera = previousSmoothCamera;

        minecraft.options.save();

        active = false;
    }
}