package com.mosseygremlin.utiliwheel.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.mosseygremlin.utiliwheel.UtiliWheel;
import com.mosseygremlin.utiliwheel.input.Keybinds;
import com.mosseygremlin.utiliwheel.screen.RadialMenuScreen;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import org.lwjgl.glfw.GLFW;

public class ClientEvents {

    private static boolean radialOpen = false;

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            InputConstants.Key configuredKey =
                    InputConstants.getKey(
                            Keybinds.radialMenuKey.saveString()
                    );

            long windowHandle = client.getWindow().handle();

            boolean radialKeyIsDown;

            if (configuredKey.getType() == InputConstants.Type.MOUSE) {

                radialKeyIsDown =
                        GLFW.glfwGetMouseButton(
                                windowHandle,
                                configuredKey.getValue()
                        ) == GLFW.GLFW_PRESS;

            } else {

                radialKeyIsDown =
                        GLFW.glfwGetKey(
                                windowHandle,
                                configuredKey.getValue()
                        ) == GLFW.GLFW_PRESS;
            }

            if (radialKeyIsDown) {

                if (!radialOpen && client.screen == null) {
                    UtiliWheel.LOGGER.info("OPENING RADIAL");

                    radialOpen = true;
                    client.setScreen(new RadialMenuScreen());
                    client.mouseHandler.releaseMouse();
                }

            } else if (radialOpen) {

                UtiliWheel.LOGGER.info("RADIAL: KEY RELEASED");

                radialOpen = false;

                if (client.screen instanceof RadialMenuScreen radial) {
                    radial.releaseF();
                }
            }
        });
    }
}