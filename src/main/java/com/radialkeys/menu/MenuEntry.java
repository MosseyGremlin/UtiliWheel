package com.radialkeys.menu;

import com.radialkeys.action.Action;
import com.radialkeys.action.ActionRegistry;

import java.util.List;

public class MenuEntry {

    private final String name;
    private final String description;
    private final List<MenuEntry> children;
    private final String actionId;

    // Menu folder
    public MenuEntry(
            String name,
            String description,
            List<MenuEntry> children
    ) {
        this.name = name;
        this.description = description;
        this.children = children;
        this.actionId = null;
    }

    // Action button
    public MenuEntry(
            String name,
            String description,
            String actionId
    ) {
        this.name = name;
        this.description = description;
        this.children = List.of();
        this.actionId = actionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<MenuEntry> getChildren() {
        return children;
    }

    public Action getAction() {
        if (actionId == null) {
            return null;
        }

        return ActionRegistry.get(actionId);
    }
}