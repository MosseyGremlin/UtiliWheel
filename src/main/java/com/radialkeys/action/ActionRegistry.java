package com.radialkeys.action;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ActionRegistry {

    private static final Map<String, Action> ACTIONS =
            new LinkedHashMap<>();

    public static void register(Action action) {
        ACTIONS.put(action.getId(), action);
    }

    public static Action get(String id) {
        return ACTIONS.get(id);
    }

    public static Collection<Action> getActions() {
        return ACTIONS.values();
    }
}