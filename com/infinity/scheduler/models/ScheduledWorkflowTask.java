package com.infinity.scheduler.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a single executable task within the Workflow Engine. ⚙️
 * Implements Comparable to allow automatic sorting based on TaskPriorityLevel.
 * 📉
 */
public class ScheduledWorkflowTask implements Comparable<ScheduledWorkflowTask> {

    private final String uniqueTaskId;
    private final String associatedUserId; // Links the task to the user who created it 👤
    private String taskTitleName;
    private String taskDetailedDescription;

    private TaskCategoryGroup taskCategory;
    private TaskPriorityLevel taskPriority;
    private TaskStatusState currentTaskStatus;

    private LocalDateTime taskCreationTimestamp;
    private LocalDateTime taskDeadlineDate;

    private boolean isRecurringTask;
    private final List<String> dependentTaskIds; // Stores IDs of parent tasks that must finish first 🔗

    public ScheduledWorkflowTask(String associatedUserId, String taskTitleName, TaskCategoryGroup taskCategory,
            TaskPriorityLevel taskPriority) {
        this.uniqueTaskId = UUID.randomUUID().toString();
        this.associatedUserId = associatedUserId;
        this.taskTitleName = taskTitleName;
        this.taskCategory = taskCategory;
        this.taskPriority = taskPriority;
        this.currentTaskStatus = TaskStatusState.PENDING_EXECUTION;
        this.taskCreationTimestamp = LocalDateTime.now();
        this.isRecurringTask = false;
        this.dependentTaskIds = new ArrayList<>();
    }

    // --- Core Business Logic Methods --- 🛠️

    public void addDependency(String parentTaskId) {
        if (!this.dependentTaskIds.contains(parentTaskId)) {
            this.dependentTaskIds.add(parentTaskId);
        }
    }

    public void markAsCompleted() {
        this.currentTaskStatus = TaskStatusState.COMPLETED_SUCCESSFULLY;
    }

    public void markAsFailed() {
        this.currentTaskStatus = TaskStatusState.FAILED_EXECUTION;
    }

    // --- Getters and Setters --- 🗄️

    public String getUniqueTaskId() {
        return uniqueTaskId;
    }

    public String getAssociatedUserId() {
        return associatedUserId;
    }

    public String getTaskTitleName() {
        return taskTitleName;
    }

    public void setTaskTitleName(String taskTitleName) {
        this.taskTitleName = taskTitleName;
    }

    public String getTaskDetailedDescription() {
        return taskDetailedDescription;
    }

    public void setTaskDetailedDescription(String taskDetailedDescription) {
        this.taskDetailedDescription = taskDetailedDescription;
    }

    public TaskCategoryGroup getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(TaskCategoryGroup taskCategory) {
        this.taskCategory = taskCategory;
    }

    public TaskPriorityLevel getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriorityLevel taskPriority) {
        this.taskPriority = taskPriority;
    }

    public TaskStatusState getCurrentTaskStatus() {
        return currentTaskStatus;
    }

    public void setCurrentTaskStatus(TaskStatusState currentTaskStatus) {
        this.currentTaskStatus = currentTaskStatus;
    }

    public LocalDateTime getTaskDeadlineDate() {
        return taskDeadlineDate;
    }

    public void setTaskDeadlineDate(LocalDateTime taskDeadlineDate) {
        this.taskDeadlineDate = taskDeadlineDate;
    }

    public boolean getIsRecurringTask() {
        return isRecurringTask;
    }

    public void setIsRecurringTask(boolean isRecurringTask) {
        this.isRecurringTask = isRecurringTask;
    }

    public List<String> getDependentTaskIds() {
        return dependentTaskIds;
    }

    /**
     * Determines execution order. Lower priority weight = Higher actual urgency. 🚦
     */
    @Override
    public int compareTo(ScheduledWorkflowTask otherTask) {
        return Integer.compare(this.taskPriority.getPriorityWeight(), otherTask.getTaskPriority().getPriorityWeight());
    }
}