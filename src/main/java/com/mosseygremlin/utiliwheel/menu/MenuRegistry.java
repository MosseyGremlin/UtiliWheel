package com.mosseygremlin.utiliwheel.menu;

import com.mosseygremlin.utiliwheel.integration.IntegrationLoader;

import java.util.ArrayList;
import java.util.List;

public class MenuRegistry {

    public static List<MenuEntry> ROOT_MENU = List.of();

    public static void load() {

        List<MenuEntry> combinedMenu = new ArrayList<>();

        combinedMenu.addAll(
                MenuConfigLoader.load()
        );

        combinedMenu.addAll(
                IntegrationLoader.getMenuEntries()
        );

        ROOT_MENU = List.copyOf(combinedMenu);
    }
}