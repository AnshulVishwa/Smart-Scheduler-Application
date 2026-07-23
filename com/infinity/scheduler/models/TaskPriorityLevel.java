package com.infinity.scheduler.models;

/**
 * Defines the priority level of a scheduled task.
 * Lower integer value indicates higher urgency for the Priority Queue.
 */
public enum TaskPriorityLevel {
    CRITICAL_URGENCY(1, "Critical - Must be executed immediately"),
    HIGH_PRIORITY(2, "High - Important business logic"),
    MEDIUM_PRIORITY(3, "Medium - Standard background task"),
    LOW_PRIORITY(4, "Low - Execute when system resources are idle");

    private final int priorityWeight;
    private final String priorityDescription;

    TaskPriorityLevel(int priorityWeight, String priorityDescription) {
        this.priorityWeight = priorityWeight;
        this.priorityDescription = priorityDescription;
    }

    public int getPriorityWeight() {
        return priorityWeight;
    }

    public String getPriorityDescription() {
        return priorityDescription;
    }
}