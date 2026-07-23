package com.infinity.scheduler.services;

import com.infinity.scheduler.models.ScheduledWorkflowTask;
import com.infinity.scheduler.models.TaskStatusState;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Background worker that continuously polls for tasks that are ready to run. 🔄
 * Uses a Priority Queue to ensure CRITICAL tasks execute before LOW tasks. 📉
 */
public class BackgroundExecutionWorker {

    private final PriorityQueue<ScheduledWorkflowTask> pendingExecutionQueue;
    private final ScheduledExecutorService backgroundThreadManager;
    private final WorkflowDependencyManager dependencyManager;

    public BackgroundExecutionWorker(WorkflowDependencyManager dependencyManager) {
        this.pendingExecutionQueue = new PriorityQueue<>();
        this.backgroundThreadManager = Executors.newScheduledThreadPool(3); // Thread Pool size 3 🧵
        this.dependencyManager = dependencyManager;
    }

    /**
     * Adds a task to the execution queue. 📥
     */
    public synchronized void submitTaskForExecution(ScheduledWorkflowTask readyTask) {
        this.pendingExecutionQueue.offer(readyTask);
        System.out.println("📥 Task Submitted to Queue: " + readyTask.getTaskTitleName());
    }

    /**
     * Starts the background engine to process tasks every 3 seconds. ⏱️
     */
    public void startBackgroundEngine() {
        System.out.println("🚀 Background Execution Engine Started...");

        backgroundThreadManager.scheduleAtFixedRate(() -> {
            processHighestPriorityReadyTask();
        }, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * Polls the queue, finds the first task whose dependencies are met, and runs
     * it. ⚙️
     */
    private synchronized void processHighestPriorityReadyTask() {
        if (pendingExecutionQueue.isEmpty()) {
            return; // Nothing to process 😴
        }

        List<ScheduledWorkflowTask> unreadyTasksBuffer = new ArrayList<>();
        ScheduledWorkflowTask taskToExecuteNow = null;

        // Search through the heap for the highest priority task that is unblocked 🔎
        while (!pendingExecutionQueue.isEmpty()) {
            ScheduledWorkflowTask evaluatedTask = pendingExecutionQueue.poll();

            if (dependencyManager.areAllPrerequisitesMet(evaluatedTask)) {
                taskToExecuteNow = evaluatedTask;
                break; // Found our task! 🎉
            } else {
                unreadyTasksBuffer.add(evaluatedTask); // Not ready yet, save it for later ⏳
            }
        }

        // Put the unready tasks back into the Priority Queue 🔄
        pendingExecutionQueue.addAll(unreadyTasksBuffer);

        // Execute the task if we found a valid one ⚡
        if (taskToExecuteNow != null) {
            executeTaskLogic(taskToExecuteNow);
        }
    }

    private void executeTaskLogic(ScheduledWorkflowTask taskExecuting) {
        taskExecuting.setCurrentTaskStatus(TaskStatusState.IN_PROGRESS);
        System.out.println(
                "⚡ EXECUTING: [" + taskExecuting.getTaskPriority().name() + "] - " + taskExecuting.getTaskTitleName());

        try {
            // Simulating real work being done 🛠️
            Thread.sleep(1500);

            taskExecuting.markAsCompleted();
            System.out.println("✅ SUCCESS: Task Finished - " + taskExecuting.getTaskTitleName());

        } catch (InterruptedException threadException) {
            taskExecuting.markAsFailed();
            System.err.println("❌ FAILED: Task Interrupted - " + taskExecuting.getTaskTitleName());
            Thread.currentThread().interrupt();
        }
    }

    public void gracefullyShutdownEngine() {
        System.out.println("🛑 Shutting down execution engine...");
        backgroundThreadManager.shutdown();
    }
}