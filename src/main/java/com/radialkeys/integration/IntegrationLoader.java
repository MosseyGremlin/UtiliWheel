package com.radialkeys.integration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.radialkeys.RadialKeys;
import com.radialkeys.action.ActionRegistry;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class IntegrationLoader {

    private static final String INTEGRATION_PATH =
            "radialkeys/integrations";

    public static void load() {

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
                    RadialKeys.LOGGER.error(
                            "Could not scan integrations from mod '{}'",
                            mod.getMetadata().getId(),
                            exception
                    );
                }
            });
        }
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

            int availableActions = 0;

            for (JsonElement element : actions) {

                JsonObject action =
                        element.getAsJsonObject();

                String actionId =
                        action.get("id").getAsString();

                if (ActionRegistry.get(actionId) != null) {
                    availableActions++;
                } else {
                    RadialKeys.LOGGER.warn(
                            "Integration '{}' references unknown action '{}'",
                            integrationId,
                            actionId
                    );
                }
            }

            RadialKeys.LOGGER.info(
                    "Loaded integration '{}' from '{}' with {}/{} available actions",
                    integrationName,
                    sourceMod.getMetadata().getId(),
                    availableActions,
                    actions.size()
            );

        } catch (Exception exception) {
            RadialKeys.LOGGER.error(
                    "Could not load integration file {}",
                    path,
                    exception
            );
        }
    }
}