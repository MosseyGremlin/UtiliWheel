package com.mosseygremlin.utiliwheel.integration.vanilla;

import com.mosseygremlin.utiliwheel.action.Action;
import com.mosseygremlin.utiliwheel.action.ActionRegistry;
import com.mosseygremlin.utiliwheel.mixin.KeyboardHandlerInvoker;
import com.mosseygremlin.utiliwheel.util.DeferredActions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.Screenshot;

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

        ActionRegistry.register(new Action(
                "minecraft.screenshot",
                "Screenshot",
                VanillaActions::takeScreenshot
        ));

        ActionRegistry.register(new Action(
                "minecraft.toggle_sneak",
                "Toggle Sneak",
                VanillaActions::toggleSneak
        ));

        ActionRegistry.register(new Action(
                "minecraft.toggle_auto_jump",
                "Toggle AutoJump",
                VanillaActions::toggleautojump
        ));



    }

    private static void takeScreenshot() {

        Minecraft minecraft = Minecraft.getInstance();

        minecraft.execute(() ->
                DeferredActions.queue(
                        () -> Screenshot.grab(
                                minecraft.gameDirectory,
                                minecraft.getMainRenderTarget(),
                                message -> minecraft.execute(
                                        () -> minecraft.gui.getChat().addMessage(message)
                                )
                        ),
                        1
                ));
    }

    private static void toggleSneak() {

        Minecraft minecraft = Minecraft.getInstance();

        boolean currentlyToggle =
                minecraft.options
                        .toggleCrouch()
                        .get();

        minecraft.options
                .toggleCrouch()
                .set(!currentlyToggle);

        minecraft.options.save();
    }

    private static void toggleautojump() {

        Minecraft minecraft = Minecraft.getInstance();

        boolean currentlyEnabled =
                minecraft.options
                        .autoJump()
                        .get();

        minecraft.options
                .autoJump()
                .set(!currentlyEnabled);

        minecraft.options.save();
    }

    private static void runDebugAction(int key) {

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        ((KeyboardHandlerInvoker) minecraft.keyboardHandler)
                .utiliwheel$handleDebugKeys((
                        new KeyEvent(key, 0, 0)
                ));
    }
}
