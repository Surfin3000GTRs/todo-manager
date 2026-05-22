package com.surfin3000gtrs.todomanager.controller;

import com.surfin3000gtrs.todomanager.model.Task;
import com.surfin3000gtrs.todomanager.model.TaskForm;
import com.surfin3000gtrs.todomanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String newTask(Model model) {
        model.addAttribute("taskForm", new TaskForm());
        model.addAttribute("formAction", "/tasks");
        return "tasks/form";
    }

    @PostMapping
    public String create(@Valid TaskForm taskForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/tasks");
            return "tasks/form";
        }
        taskService.create(taskForm.getTitle(), taskForm.getDescription());
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id);
        TaskForm form = new TaskForm();
        form.setTitle(task.getTitle());
        form.setDescription(task.getDescription());
        form.setCompleted(task.isCompleted());

        model.addAttribute("taskForm", form);
        model.addAttribute("taskId", id);
        model.addAttribute("formAction", "/tasks/" + id);
        return "tasks/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid TaskForm taskForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("taskId", id);
            model.addAttribute("formAction", "/tasks/" + id);
            return "tasks/form";
        }
        taskService.update(id, taskForm.getTitle(), taskForm.getDescription(), taskForm.isCompleted());
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        taskService.toggleCompleted(id);
        return "redirect:/tasks";
    }
}
