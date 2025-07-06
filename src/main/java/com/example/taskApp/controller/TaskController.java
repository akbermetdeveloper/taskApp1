package com.example.taskApp.controller;//package com.example.taskApp.controller;
//import com.example.taskApp.model.Task;
//import com.example.taskApp.service.TaskService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/tasks")
//public class TaskController {
//    private final TaskService taskService;
//
//    public TaskController(TaskService taskService) {
//        this.taskService = taskService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Task> createTask(@RequestBody Task task) {
//        return ResponseEntity.ok(taskService.createTask(task));
//    }
//
//    @GetMapping
//    public  ResponseEntity<List<Task>> getAllTasks() {
//        return ResponseEntity.ok(taskService.getAllTasks());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
//        return taskService.getTaskById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
//        return ResponseEntity.ok(taskService.updateTask(id, task));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
//        taskService.deleteTask(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//
//}

import com.example.taskApp.model.Task;
import com.example.taskApp.model.Status;
import com.example.taskApp.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        logger.info("REST: Запрос на создание задачи - {}", task.getTitle());

        try {
            Task createdTask = taskService.createTask(task);
            logger.info("REST: Задача успешно создана с ID: {}, статус: {}",
                    createdTask.getId(), createdTask.getStatus());
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("REST: Ошибка при создании задачи: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        logger.info("REST: Запрос на получение всех задач");

        try {
            List<Task> tasks = taskService.getAllTasks();
            logger.info("REST: Возвращено {} задач", tasks.size());
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("REST: Ошибка при получении задач: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        logger.info("REST: Запрос на получение задачи с ID: {}", id);

        try {
            Optional<Task> task = taskService.getTaskById(id);
            if (task.isPresent()) {
                logger.info("REST: Задача найдена и возвращена: {}, статус: {}",
                        task.get().getTitle(), task.get().getStatus());
                return new ResponseEntity<>(task.get(), HttpStatus.OK);
            } else {
                logger.warn("REST: Задача с ID {} не найдена", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("REST: Ошибка при получении задачи: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        logger.info("REST: Запрос на обновление задачи с ID: {}", id);

        try {
            Task updatedTask = taskService.updateTask(id, taskDetails);
            logger.info("REST: Задача успешно обновлена: {}, статус: {}",
                    updatedTask.getTitle(), updatedTask.getStatus());
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("REST: Задача с ID {} не найдена для обновления", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("REST: Ошибка при обновлении задачи: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("REST: Запрос на удаление задачи с ID: {}", id);

        try {
            taskService.deleteTask(id);
            logger.info("REST: Задача с ID {} успешно удалена", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("REST: Задача с ID {} не найдена для удаления", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("REST: Ошибка при удалении задачи: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Status status) {
        logger.info("REST: Запрос на получение задач со статусом: {}", status.name());

        try {
            List<Task> tasks = taskService.getTasksByStatus(status);
            logger.info("REST: Найдено {} задач со статусом {}", tasks.size(), status.name());
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("REST: Ошибка при получении задач по статусу: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/completed/{completed}")
    public ResponseEntity<List<Task>> getTasksByCompleted(@PathVariable Boolean completed) {
        logger.info("REST: Запрос на получение задач со статусом завершения: {}", completed);

        try {
            List<Task> tasks = taskService.getTasksByStatus(Status.COMPLETED);
            logger.info("REST: Найдено {} задач со статусом завершения {}", tasks.size(), completed);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("REST: Ошибка при получении задач по статусу завершения: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Task>> getActiveTasks() {
        logger.info("REST: Запрос на получение активных задач");

        try {
            List<Task> tasks = taskService.getActiveTasks();
            logger.info("REST: Найдено {} активных задач", tasks.size());
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("REST: Ошибка при получении активных задач: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody Status status) {
        logger.info("REST: Запрос на изменение статуса задачи с ID: {} на {}", id, status.name());

        try {
            Task updatedTask = taskService.updateTaskStatus(id, status);
            logger.info("REST: Статус задачи успешно изменен: {}, новый статус: {}",
                    updatedTask.getTitle(), updatedTask.getStatus().name());
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("REST: Задача с ID {} не найдена для изменения статуса", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("REST: Ошибка при изменении статуса задачи: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String title) {
        logger.info("REST: Запрос на поиск задач по названию: {}", title);

        try {
            List<Task> tasks = taskService.searchTasksByTitle(title);
            logger.info("REST: Найдено {} задач по поисковому запросу '{}'", tasks.size(), title);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("REST: Ошибка при поиске задач: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}