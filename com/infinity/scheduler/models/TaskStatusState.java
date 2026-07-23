package com.infinity.scheduler.models;

/**
 * Represents the current lifecycle state of a Scheduled Task. 🔄
 */
public enum TaskStatusState {
    PENDING_EXECUTION,
    IN_PROGRESS,
    COMPLETED_SUCCESSFULLY,
    FAILED_EXECUTION,
    POSTPONED
}