package com.mosseygremlin.utiliwheel.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class Keybinds {

    public static KeyMapping UtiliWheelMenuKey;

    public static void register() {

        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(
                        "utiliwheel",
                        "main"
                )
        );

        UtiliWheelMenuKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.radialkeys.open",
                        GLFW.GLFW_KEY_R,
                        category
                )
        );
    }
}