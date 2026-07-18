package com.mosseygremlin.utiliwheel.integration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mosseygremlin.utiliwheel.UtiliWheel;
import com.mosseygremlin.utiliwheel.action.ActionRegistry;
import com.mosseygremlin.utiliwheel.menu.MenuEntry;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class IntegrationLoader {

    private static final String INTEGRATION_PATH =
            "utiliwheel/integrations";

    private static final List<MenuEntry> MENU_ENTRIES =
            new ArrayList<>();

    public static void load() {

        MENU_ENTRIES.clear();

        for (ModContainer mod :
                FabricLoader.getInstance().getAllMods()) {

            mod.findPath(INTEGRATION_PATH).ifPresent(directory -> {
                try (Stream<Path> files = Files.list(directory)) {

                    files.filter(Files::isRegularFile)
                            .filter(path ->
                                    path.toString().endsWith(".json")
                            )
                            .forEach(path ->
                                    loadIntegration(path, mod)
                            );

                } catch (Exception exception) {
                    UtiliWheel.LOGGER.error(
                            "Could not scan integrations from mod '{}'",
                            mod.getMetadata().getId(),
                            exception
                    );
                }
            });
        }
    }

    public static List<MenuEntry> getMenuEntries() {
        return List.copyOf(MENU_ENTRIES);
    }

    private static void loadIntegration(
            Path path,
            ModContainer sourceMod
    ) {
        try (Reader reader = Files.newBufferedReader(path)) {

            JsonObject root =
                    JsonParser.parseReader(reader).getAsJsonObject();

            String integrationId =
                    root.get("id").getAsString();

            String integrationName =
                    root.get("name").getAsString();

            JsonArray actions =
                    root.getAsJsonArray("actions");

            Map<String, IntegrationActionInfo> actionsById =
                    new HashMap<>();

            int availableActions = 0;

            for (JsonElement element : actions) {

                JsonObject action =
                        element.getAsJsonObject();

                String actionId =
                        action.get("id").getAsString();

                String actionName =
                        action.get("name").getAsString();

                String actionDescription =
                        action.get("description").getAsString();

                if (ActionRegistry.get(actionId) != null) {

                    actionsById.put(
                            actionId,
                            new IntegrationActionInfo(
                                    actionName,
                                    actionDescription
                            )
                    );

                    availableActions++;

                } else {
                    UtiliWheel.LOGGER.warn(
                            "Integration '{}' references unknown action '{}'",
                            integrationId,
                            actionId
                    );
                }
            }

            if (root.has("menu")) {
                MENU_ENTRIES.addAll(
                        readMenuEntries(
                                root.getAsJsonArray("menu"),
                                integrationId,
                                actionsById
                        )
                );
            }

            UtiliWheel.LOGGER.info(
                    "Loaded integration '{}' from '{}' with {}/{} available actions",
                    integrationName,
                    sourceMod.getMetadata().getId(),
                    availableActions,
                    actions.size()
            );

        } catch (Exception exception) {
            UtiliWheel.LOGGER.error(
                    "Could not load integration file {}",
                    path,
                    exception
            );
        }
    }

    private static List<MenuEntry> readMenuEntries(
            JsonArray entries,
            String integrationId,
            Map<String, IntegrationActionInfo> actionsById
    ) {
        List<MenuEntry> menuEntries = new ArrayList<>();

        for (JsonElement element : entries) {

            JsonObject entry =
                    element.getAsJsonObject();

            String type =
                    entry.get("type").getAsString();

            if (type.equals("folder")) {

                String name =
                        entry.get("name").getAsString();

                String description =
                        entry.get("description").getAsString();

                List<MenuEntry> children =
                        readMenuEntries(
                                entry.getAsJsonArray("children"),
                                integrationId,
                                actionsById
                        );

                menuEntries.add(
                        new MenuEntry(
                                name,
                                description,
                                children
                        )
                );

            } else if (type.equals("action")) {

                String actionId =
                        entry.get("action").getAsString();

                IntegrationActionInfo actionInfo =
                        actionsById.get(actionId);

                if (actionInfo == null) {
                    UtiliWheel.LOGGER.warn(
                            "Integration '{}' menu references unavailable action '{}'",
                            integrationId,
                            actionId
                    );

                    continue;
                }

                menuEntries.add(
                        new MenuEntry(
                                actionInfo.name(),
                                actionInfo.description(),
                                actionId
                        )
                );
            }
        }

        return menuEntries;
    }

    private record IntegrationActionInfo(
            String name,
            String description
    ) {
    }
}