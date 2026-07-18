package com.mosseygremlin.utiliwheel.action;

public class Action {

    private final String id;
    private final String name;
    private final Runnable runnable;

    public Action(String id, String name, Runnable runnable) {
        this.id = id;
        this.name = name;
        this.runnable = runnable;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void run() {
        runnable.run();
    }
}