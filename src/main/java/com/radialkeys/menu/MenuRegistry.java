package com.radialkeys.menu;

import java.util.List;

public class MenuRegistry {

    public static List<MenuEntry> ROOT_MENU = List.of();

    public static void load() {
        ROOT_MENU = MenuConfigLoader.load();
    }
}