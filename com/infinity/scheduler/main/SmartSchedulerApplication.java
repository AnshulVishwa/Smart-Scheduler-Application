package com.infinity.scheduler.main;

import com.infinity.scheduler.models.ScheduledWorkflowTask;
import com.infinity.scheduler.models.TaskCategoryGroup;
import com.infinity.scheduler.models.TaskPriorityLevel;
import com.infinity.scheduler.services.BackgroundExecutionWorker;
import com.infinity.scheduler.services.TaskManagementService;
import com.infinity.scheduler.services.UserAuthenticationService;
import com.infinity.scheduler.services.WorkflowDependencyManager;
import com.infinity.scheduler.utils.DateFormatterUtil;

import java.time.LocalDateTime;

public class SmartSchedulerApplication {

    public static void main(String[] args) {
        System.out.println("🌟 Initializing Smart Task Scheduler & Workflow Engine...");
        System.out.println("System Time: " + DateFormatterUtil.formatDateTimeToString(LocalDateTime.now()));
        System.out.println("---------------------------------------------------------");

        try {
            // 1. Initialize Services ⚙️
            UserAuthenticationService authService = new UserAuthenticationService();
            TaskManagementService taskService = new TaskManagementService();
            WorkflowDependencyManager dependencyManager = new WorkflowDependencyManager(taskService);
            BackgroundExecutionWorker executionWorker = new BackgroundExecutionWorker(dependencyManager);

            // 2. Mock User Login 👤
            authService.registerNewUser("admin@infinity.com", "AdminPass123!", "System Admin");
            authService.loginUser("admin@infinity.com", "AdminPass123!");
            String currentUserId = authService.getCurrentlyLoggedInUser().getUniqueUserId();

            // 3. Create Tasks 📝
            System.out.println("\n--- Creating System Tasks ---");
            ScheduledWorkflowTask databaseBackup = taskService.createNewTask(
                    currentUserId, "Nightly Database Backup", TaskCategoryGroup.DATABASE_OPERATION,
                    TaskPriorityLevel.HIGH_PRIORITY);

            ScheduledWorkflowTask logCleanup = taskService.createNewTask(
                    currentUserId, "Clear Old Server Logs", TaskCategoryGroup.SYSTEM_MAINTENANCE,
                    TaskPriorityLevel.LOW_PRIORITY);

            ScheduledWorkflowTask generateReport = taskService.createNewTask(
                    currentUserId, "Generate Analytics Report", TaskCategoryGroup.DATA_ANALYTICS,
                    TaskPriorityLevel.MEDIUM_PRIORITY);

            // 4. Establish Dependencies (Report MUST wait for Backup to finish) 🔗
            System.out.println("\n--- Establishing Workflow Graph ---");
            dependencyManager.establishDependency(generateReport.getUniqueTaskId(), databaseBackup.getUniqueTaskId());

            // 5. Submit to Execution Queue 📥
            System.out.println("\n--- Submitting Tasks to Engine ---");
            executionWorker.submitTaskForExecution(generateReport); // Submitted first, but will be blocked! 🛑
            executionWorker.submitTaskForExecution(logCleanup);
            executionWorker.submitTaskForExecution(databaseBackup);

            // 6. Start the Engine 🚀
            System.out.println("\n--- Starting Background Worker ---");
            executionWorker.startBackgroundEngine();

            // Allow the engine to run for 10 seconds to simulate processing, then shut down
            // safely ⏱️
            Thread.sleep(10000);

            executionWorker.gracefullyShutdownEngine();
            authService.logoutCurrentUser();

            System.out.println("---------------------------------------------------------");
            System.out.println("🛑 System offline.");

        } catch (Exception exception) {
            System.err.println("❌ Critical System Failure: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}