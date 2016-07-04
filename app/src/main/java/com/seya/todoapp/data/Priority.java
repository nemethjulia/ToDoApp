package com.seya.todoapp.data;

public enum Priority {
    LOW     ("LOW", 0),
    MEDIUM  ("MEDIUM", 1),
    HIGH  ("HIGH", 2);

    private String name;
    private int priority;

    Priority(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public static Priority getPriority(int value) {
        switch (value) {
            case 0:
                return LOW;
            case 1:
                return MEDIUM;
            case 2:
                return HIGH;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
