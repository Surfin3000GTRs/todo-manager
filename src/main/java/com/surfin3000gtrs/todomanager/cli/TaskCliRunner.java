package com.surfin3000gtrs.todomanager.cli;

import com.surfin3000gtrs.todomanager.model.Task;
import com.surfin3000gtrs.todomanager.service.TaskService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TaskCliRunner implements ApplicationRunner {

    private final TaskService taskService;

    public TaskCliRunner(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.getNonOptionArgs().isEmpty() || !"cli".equalsIgnoreCase(args.getNonOptionArgs().get(0))) {
            return;
        }

        if (args.getNonOptionArgs().size() < 2) {
            printHelp();
            return;
        }

        String command = args.getNonOptionArgs().get(1);
        switch (command) {
            case "list" -> listTasks();
            case "add" -> addTask(args);
            case "update" -> updateTask(args);
            case "delete" -> deleteTask(args);
            default -> printHelp();
        }
    }

    private void listTasks() {
        if (taskService.findAll().isEmpty()) {
            System.out.println("タスクはありません。");
            return;
        }

        for (Task task : taskService.findAll()) {
            System.out.printf("[%d] %s | 完了: %s | 作成日: %s%n",
                    task.getId(), task.getTitle(), task.isCompleted(), task.getCreatedAt());
        }
    }

    private void addTask(ApplicationArguments args) {
        if (args.getNonOptionArgs().size() < 4) {
            System.out.println("使い方: cli add <title> <description>");
            return;
        }

        Task task = taskService.create(args.getNonOptionArgs().get(2), args.getNonOptionArgs().get(3));
        System.out.println("追加しました: " + task.getId());
    }

    private void updateTask(ApplicationArguments args) {
        if (args.getNonOptionArgs().size() < 6) {
            System.out.println("使い方: cli update <id> <title> <description> <completed:true|false>");
            return;
        }

        Long id = Long.parseLong(args.getNonOptionArgs().get(2));
        String title = args.getNonOptionArgs().get(3);
        String description = args.getNonOptionArgs().get(4);
        boolean completed = Boolean.parseBoolean(args.getNonOptionArgs().get(5));

        taskService.update(id, title, description, completed);
        System.out.println("更新しました: " + id);
    }

    private void deleteTask(ApplicationArguments args) {
        if (args.getNonOptionArgs().size() < 3) {
            System.out.println("使い方: cli delete <id>");
            return;
        }

        Long id = Long.parseLong(args.getNonOptionArgs().get(2));
        taskService.delete(id);
        System.out.println("削除しました: " + id);
    }

    private void printHelp() {
        System.out.println("CLI コマンド:");
        System.out.println("  cli list");
        System.out.println("  cli add <title> <description>");
        System.out.println("  cli update <id> <title> <description> <completed:true|false>");
        System.out.println("  cli delete <id>");
    }
}
