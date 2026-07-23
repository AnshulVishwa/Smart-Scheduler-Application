package com.infinity.scheduler.services;

import com.infinity.scheduler.models.ScheduledWorkflowTask;
import com.infinity.scheduler.exceptions.CyclicDependencyException;
import com.infinity.scheduler.exceptions.TaskNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages the dependencies between different tasks. 🔗
 * Ensures that workflows are valid and can be executed topologically. 📈
 */
public class WorkflowDependencyManager {

    private final TaskManagementService centralizedTaskService;

    public WorkflowDependencyManager(TaskManagementService centralizedTaskService) {
        this.centralizedTaskService = centralizedTaskService;
    }

    /**
     * Adds a dependency between two tasks, ensuring no cycles are created. ➕
     * 
     * @param dependentTaskId    The task that must wait. ⏳
     * @param prerequisiteTaskId The task that must finish first. 🏁
     */
    public void establishDependency(String dependentTaskId, String prerequisiteTaskId)
            throws TaskNotFoundException, CyclicDependencyException {

        ScheduledWorkflowTask dependentTask = centralizedTaskService.getTaskById(dependentTaskId);
        ScheduledWorkflowTask prerequisiteTask = centralizedTaskService.getTaskById(prerequisiteTaskId);

        // Temporarily add dependency to check for cycles 🔄
        dependentTask.addDependency(prerequisiteTaskId);

        if (hasDependencyCycle(dependentTask)) {
            // Rollback if a cycle is detected ⏪
            dependentTask.getDependentTaskIds().remove(prerequisiteTaskId);
            throw new CyclicDependencyException(
                    "Cannot add dependency: It creates an infinite loop between " +
                            dependentTask.getTaskTitleName() + " and " + prerequisiteTask.getTaskTitleName());
        }

        System.out.println("🔗 Dependency Established: [" + dependentTask.getTaskTitleName() +
                "] now waiting for [" + prerequisiteTask.getTaskTitleName() + "]");
    }

    /**
     * Uses Depth-First Search (DFS) to detect cyclical dependencies. 🕵️‍♂️
     */
    private boolean hasDependencyCycle(ScheduledWorkflowTask startingTask) {
        Set<String> visitedTaskIds = new HashSet<>();
        Set<String> executionStackIds = new HashSet<>();

        return performDepthFirstSearchCycleCheck(startingTask.getUniqueTaskId(), visitedTaskIds, executionStackIds);
    }

    private boolean performDepthFirstSearchCycleCheck(String currentTaskId, Set<String> visitedTaskIds,
            Set<String> executionStackIds) {
        if (executionStackIds.contains(currentTaskId)) {
            return true; // Cycle detected! 🚨
        }
        if (visitedTaskIds.contains(currentTaskId)) {
            return false; // Already checked, safe. ✅
        }

        visitedTaskIds.add(currentTaskId);
        executionStackIds.add(currentTaskId);

        try {
            ScheduledWorkflowTask currentTask = centralizedTaskService.getTaskById(currentTaskId);
            List<String> prerequisiteIds = currentTask.getDependentTaskIds();

            for (String prerequisiteId : prerequisiteIds) {
                if (performDepthFirstSearchCycleCheck(prerequisiteId, visitedTaskIds, executionStackIds)) {
                    return true;
                }
            }
        } catch (TaskNotFoundException notFoundException) {
            System.err.println("⚠️ Warning: Dangling dependency found for ID: " + currentTaskId);
        }

        executionStackIds.remove(currentTaskId);
        return false;
    }

    /**
     * Checks if a task is ready to execute by verifying all prerequisites are
     * COMPLETED. ✅
     */
    public boolean areAllPrerequisitesMet(ScheduledWorkflowTask targetTask) {
        for (String prerequisiteId : targetTask.getDependentTaskIds()) {
            try {
                ScheduledWorkflowTask prerequisiteTask = centralizedTaskService.getTaskById(prerequisiteId);
                if (prerequisiteTask
                        .getCurrentTaskStatus() != com.infinity.scheduler.models.TaskStatusState.COMPLETED_SUCCESSFULLY) {
                    return false; // Still waiting on a parent task ⏳
                }
            } catch (TaskNotFoundException missingException) {
                // If the parent task was deleted, we assume the dependency is broken/failed ❌
                return false;
            }
        }
        return true;
    }
}