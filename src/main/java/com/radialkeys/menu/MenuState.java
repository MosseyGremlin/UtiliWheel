package com.radialkeys.menu;

import java.util.List;

public class MenuState {

    public final List<MenuEntry> menu;
    public final String title;

    public MenuState(List<MenuEntry> menu, String title) {
        this.menu = menu;
        this.title = title;
    }
}