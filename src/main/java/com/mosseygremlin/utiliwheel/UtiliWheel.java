package com.mosseygremlin.utiliwheel;

import com.mosseygremlin.utiliwheel.action.ActionRegistry;
import com.mosseygremlin.utiliwheel.events.ClientEvents;
import com.mosseygremlin.utiliwheel.input.Keybinds;
import com.mosseygremlin.utiliwheel.menu.MenuRegistry;
import com.mosseygremlin.utiliwheel.integration.IntegrationLoader;
import com.mosseygremlin.utiliwheel.integration.vanilla.VanillaActions;
import com.mosseygremlin.utiliwheel.util.DeferredActions;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtiliWheel implements ClientModInitializer {

    public static final Logger LOGGER =
            LoggerFactory.getLogger("utiliwheel");

    @Override
    public void onInitializeClient() {

        VanillaActions.register();

        IntegrationLoader.load();

        MenuRegistry.load();

        ClientTickEvents.END_CLIENT_TICK.register(DeferredActions::tick);

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