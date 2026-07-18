package com.radialkeys.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class Keybinds {

    public static KeyMapping radialMenuKey;

    public static void register() {

        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(
                        "radialkeys",
                        "main"
                )
        );

        radialMenuKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.radialkeys.open",
                        GLFW.GLFW_KEY_R,
                        category
                )
        );
    }
}