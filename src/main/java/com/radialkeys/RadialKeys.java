package com.radialkeys;

import com.radialkeys.action.Action;
import com.radialkeys.action.ActionRegistry;
import com.radialkeys.events.ClientEvents;
import com.radialkeys.input.Keybinds;
import com.radialkeys.menu.MenuRegistry;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RadialKeys implements ClientModInitializer {

    public static final Logger LOGGER =
            LoggerFactory.getLogger("radialkeys");

    @Override
    public void onInitializeClient() {

        ActionRegistry.register(new Action(
                "radialkeys.place_blocks",
                "Place Blocks",
                () -> System.out.println("Placed blocks!")
        ));

        ActionRegistry.register(new Action(
                "radialkeys.copy_area",
                "Copy Area",
                () -> System.out.println("Copied area!")
        ));

        ActionRegistry.register(new Action(
                "radialkeys.hello",
                "Hello",
                () -> System.out.println("Hello from RadialKeys!")
        ));

        ActionRegistry.register(new Action(
                "minecraft.inventory",
                "Inventory",
                () -> {
                    Minecraft minecraft = Minecraft.getInstance();

                    if (minecraft.player != null) {
                        minecraft.setScreen(
                                new InventoryScreen(minecraft.player)
                        );
                    }
                }
        ));

        MenuRegistry.load();

        LOGGER.info("Available Actions:");

        ActionRegistry.getActions().forEach(action ->
                LOGGER.info(
                        " - {} ({})",
                        action.getName(),
                        action.getId()
                )
        );

        Keybinds.register();
        ClientEvents.register();
    }
}