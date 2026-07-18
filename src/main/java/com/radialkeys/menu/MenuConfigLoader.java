package com.radialkeys.menu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.radialkeys.RadialKeys;
import net.fabricmc.loader.api.FabricLoader;
import com.radialkeys.action.ActionRegistry;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MenuConfigLoader {

    private static final Path MENU_FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("radialkeys")
                    .resolve("menu.json");

    public static List<MenuEntry> load() {

        try {

            try (Reader reader = Files.newBufferedReader(MENU_FILE)) {
                JsonObject root =
                        JsonParser.parseReader(reader).getAsJsonObject();

                List<MenuEntry> entries =
                        readEntries(root.getAsJsonArray("entries"));

                if (entries.isEmpty()) {
                    throw new IllegalStateException(
                            "Menu config contains no usable entries"
                    );
                }

                return entries;
            }

        } catch (Exception exception) {
            RadialKeys.LOGGER.error(
                    "Could not load {}. Using emergency menu.",
                    MENU_FILE,
                    exception
            );

            return List.of(
                    new MenuEntry(
                            "Inventory",
                            "Open your inventory",
                            "minecraft.inventory"
                    )
            );
        }
    }

    private static List<MenuEntry> readEntries(JsonArray entries) {

        List<MenuEntry> menuEntries = new ArrayList<>();

        for (JsonElement element : entries) {

            JsonObject entry = element.getAsJsonObject();

            String type = entry.get("type").getAsString();
            String name = entry.get("name").getAsString();
            String description =
                    entry.get("description").getAsString();

            if (type.equals("folder")) {

                List<MenuEntry> children =
                        readEntries(entry.getAsJsonArray("children"));

                menuEntries.add(
                        new MenuEntry(name, description, children)
                );

            } else if (type.equals("action")) {

                String actionId =
                        entry.get("action").getAsString();

                if (ActionRegistry.get(actionId) == null) {
                    RadialKeys.LOGGER.warn(
                            "Skipping menu entry '{}' because action '{}' is not registered",
                            name,
                            actionId
                    );

                    continue;
                }

                menuEntries.add(
                        new MenuEntry(name, description, actionId)
                );
            }

        }

        return menuEntries;
    }
}