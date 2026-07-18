package com.radialkeys;

import com.radialkeys.action.ActionRegistry;
import com.radialkeys.events.ClientEvents;
import com.radialkeys.input.Keybinds;
import com.radialkeys.menu.MenuRegistry;
import com.radialkeys.integration.IntegrationLoader;
import com.radialkeys.integration.vanilla.VanillaActions;

import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RadialKeys implements ClientModInitializer {

    public static final Logger LOGGER =
            LoggerFactory.getLogger("radialkeys");

    @Override
    public void onInitializeClient() {

        VanillaActions.register();

        IntegrationLoader.load();

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