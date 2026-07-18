package com.radialkeys.integration.vanilla;

import com.radialkeys.action.Action;
import com.radialkeys.action.ActionRegistry;
import com.radialkeys.mixin.KeyboardHandlerInvoker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;

import org.lwjgl.glfw.GLFW;

public class VanillaActions {

    public static void register() {

        ActionRegistry.register(new Action(
                "minecraft.debug.overlay",
                "Debug Screen",
                () -> Minecraft.getInstance()
                        .debugEntries
                        .toggleDebugOverlay()
        ));

        ActionRegistry.register(new Action(
                "minecraft.debug.chunk_borders",
                "Chunk Borders",
                () -> runDebugAction(GLFW.GLFW_KEY_G)
        ));

        ActionRegistry.register(new Action(
                "minecraft.debug.reload_chunks",
                "Reload Chunks",
                () -> runDebugAction(GLFW.GLFW_KEY_A)
        ));

        ActionRegistry.register(new Action(
                "minecraft.debug.hitboxes",
                "Hitboxes",
                () -> runDebugAction(GLFW.GLFW_KEY_B)
        ));

        ActionRegistry.register(new Action(
                "minecraft.debug.clear_chat",
                "Clear Chat",
                () -> runDebugAction(GLFW.GLFW_KEY_D)
        ));

        ActionRegistry.register(new Action(
                "minecraft.debug.advanced_tooltips",
                "Advanced Tooltips",
                () -> runDebugAction(GLFW.GLFW_KEY_H)
        ));

        ActionRegistry.register(new Action(
                "minecraft.debug.pause_on_lost_focus",
                "Pause on Lost Focus",
                () -> runDebugAction(GLFW.GLFW_KEY_P)
        ));

        ActionRegistry.register(new Action(
                "minecraft.debug.help",
                "Debug Help",
                () -> runDebugAction(GLFW.GLFW_KEY_Q)
        ));

        ActionRegistry.register(new Action(
                "minecraft.debug.reload_resources",
                "Reload Resources",
                () -> runDebugAction(GLFW.GLFW_KEY_T)
        ));
    }

    private static void runDebugAction(int key) {

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        ((KeyboardHandlerInvoker) minecraft.keyboardHandler)
                .radialkeys$handleDebugKeys(
                        new KeyEvent(key, 0, 0)
                );
    }
}