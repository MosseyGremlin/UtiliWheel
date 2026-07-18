package com.mosseygremlin.utiliwheel.menu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mosseygremlin.utiliwheel.action.ActionRegistry;
import net.fabricmc.loader.api.FabricLoader;
import com.mosseygremlin.utiliwheel.utiliwheel;

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
                    .resolve("utiliwheel")
                    .resolve("menu.json");

    public static List<MenuEntry> load() {

        try {
            createDefaultFile();

            try (Reader reader = Files.newBufferedReader(MENU_FILE)) {

                JsonObject root =
                        JsonParser.parseReader(reader)
                                .getAsJsonObject();

                return readEntries(
                        root.getAsJsonArray("entries")
                );
            }

        } catch (Exception exception) {

            utiliwheel.LOGGER.error(
                    "Could not load {}. Using empty user menu.",
                    MENU_FILE,
                    exception
            );

            return List.of();
        }
    }

    private static void createDefaultFile() throws IOException {

        if (Files.exists(MENU_FILE)) {
            return;
        }

        Files.createDirectories(
                MENU_FILE.getParent()
        );

        String defaultMenu = """
                {
                  "entries": []
                }
                """;

        Files.writeString(
                MENU_FILE,
                defaultMenu
        );

        utiliwheel.LOGGER.info(
                "Created default menu config at {}",
                MENU_FILE
        );
    }

    private static List<MenuEntry> readEntries(
            JsonArray entries
    ) {

        List<MenuEntry> menuEntries =
                new ArrayList<>();

        if (entries == null) {
            return menuEntries;
        }

        for (JsonElement element : entries) {

            JsonObject entry =
                    element.getAsJsonObject();

            String type =
                    entry.get("type").getAsString();

            String name =
                    entry.get("name").getAsString();

            String description =
                    entry.has("description")
                            ? entry.get("description").getAsString()
                            : "";

            if (type.equals("folder")) {

                JsonArray children =
                        entry.getAsJsonArray("children");

                menuEntries.add(
                        new MenuEntry(
                                name,
                                description,
                                readEntries(children)
                        )
                );

            } else if (type.equals("action")) {

                String actionId =
                        entry.get("action").getAsString();

                if (ActionRegistry.get(actionId) == null) {

                    utiliwheel.LOGGER.warn(
                            "Skipping menu entry '{}' because action '{}' is not registered",
                            name,
                            actionId
                    );

                    continue;
                }

                menuEntries.add(
                        new MenuEntry(
                                name,
                                description,
                                actionId
                        )
                );
            }
        }

        return menuEntries;
    }
}