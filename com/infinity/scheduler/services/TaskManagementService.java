package com.infinity.scheduler.services;

import com.infinity.scheduler.models.ScheduledWorkflowTask;
import com.infinity.scheduler.models.TaskCategoryGroup;
import com.infinity.scheduler.models.TaskPriorityLevel;
import com.infinity.scheduler.exceptions.TaskNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Central service for handling all CRUD operations for tasks. 📋
 * Includes logic for filtering by category and retrieving user-specific tasks.
 * 🔍
 */
public class TaskManagementService {

    // Simulating a database table for tasks. Key = TaskID, Value = Task Object 🗃️
    private final Map<String, ScheduledWorkflowTask> taskDatabaseMock;

    public TaskManagementService() {
        this.taskDatabaseMock = new HashMap<>();
    }

    /**
     * Creates a new task and saves it to the database. ➕
     */
    public ScheduledWorkflowTask createNewTask(String userId, String title, TaskCategoryGroup category,
            TaskPriorityLevel priority) {
        ScheduledWorkflowTask newTask = new ScheduledWorkflowTask(userId, title, category, priority);
        taskDatabaseMock.put(newTask.getUniqueTaskId(), newTask);
        System.out
                .println("✅ Task Created: " + newTask.getTaskTitleName() + " [ID: " + newTask.getUniqueTaskId() + "]");
        return newTask;
    }

    /**
     * Retrieves a task by its unique identifier. 🔎
     */
    public ScheduledWorkflowTask getTaskById(String taskId) throws TaskNotFoundException {
        if (!taskDatabaseMock.containsKey(taskId)) {
            throw new TaskNotFoundException("Could not find task with ID: " + taskId);
        }
        return taskDatabaseMock.get(taskId);
    }

    /**
     * Updates an existing task's title and description. ✏️
     */
    public void updateTaskDetails(String taskId, String newTitle, String newDescription) throws TaskNotFoundException {
        ScheduledWorkflowTask existingTask = getTaskById(taskId);
        existingTask.setTaskTitleName(newTitle);
        existingTask.setTaskDetailedDescription(newDescription);
        System.out.println("🔄 Task Updated: " + taskId);
    }

    /**
     * Deletes a task from the system. 🗑️
     */
    public void deleteExistingTask(String taskId) throws TaskNotFoundException {
        if (!taskDatabaseMock.containsKey(taskId)) {
            throw new TaskNotFoundException("Cannot delete. Task not found: " + taskId);
        }
        taskDatabaseMock.remove(taskId);
        System.out.println("❌ Task Deleted: " + taskId);
    }

    /**
     * Retrieves all tasks belonging to a specific user. 🧑‍💻
     * Uses Java Streams API for efficient filtering. 🌊
     */
    public List<ScheduledWorkflowTask> getAllTasksForUser(String userId) {
        return taskDatabaseMock.values().stream()
                .filter(task -> task.getAssociatedUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves tasks filtered by their category for a specific user. 🗂️
     */
    public List<ScheduledWorkflowTask> getTasksByCategory(String userId, TaskCategoryGroup targetCategory) {
        return taskDatabaseMock.values().stream()
                .filter(task -> task.getAssociatedUserId().equals(userId))
                .filter(task -> task.getTaskCategory() == targetCategory)
                .collect(Collectors.toList());
    }
}