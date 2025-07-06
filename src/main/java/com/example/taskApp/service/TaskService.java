package com.example.taskApp.service;
import com.example.taskApp.model.Status;
import com.example.taskApp.model.Task;
import com.example.taskApp.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @CacheEvict(value = "tasks", allEntries = true)
    public Task createTask(Task task) {
        logger.info("Создание новой задачи: {}", task.getTitle());
        Task savedTask = taskRepository.save(task);
        logger.info("Задача успешно создана с ID: {}, статус: {}", savedTask.getId(), savedTask.getStatus());
        return savedTask;
    }

    @Cacheable("tasks")
    public List<Task> getAllTasks() {
        logger.info("Получение списка всех задач");
        List<Task> tasks = taskRepository.findAll();
        logger.info("Найдено {} задач", tasks.size());
        return tasks;
    }

    public Optional<Task> getTaskById(Long id) {
        logger.info("Поиск задачи по ID: {}", id);
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            logger.info("Задача найдена: {}, статус: {}", task.get().getTitle(), task.get().getStatus());
        } else {
            logger.warn("Задача с ID {} не найдена", id);
        }
        return task;
    }

    public Task updateTask(Long id, Task taskDetails) {
        logger.info("Обновление задачи с ID: {}", id);

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();

            Status oldStatus = existingTask.getStatus();

            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setStatus(taskDetails.getStatus());

            Task updatedTask = taskRepository.save(existingTask);
            logger.info("Задача успешно обновлена: {}, статус изменен с {} на {}",
                    updatedTask.getTitle(), oldStatus, updatedTask.getStatus());
            return updatedTask;
        } else {
            logger.error("Не удалось обновить задачу - задача с ID {} не найдена", id);
            throw new RuntimeException("Задача с ID " + id + " не найдена");
        }
    }

    public void deleteTask(Long id) {
        logger.info("Удаление задачи с ID: {}", id);

        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            taskRepository.deleteById(id);
            logger.info("Задача '{}' с ID {} успешно удалена", task.get().getTitle(), id);
        } else {
            logger.error("Не удалось удалить задачу - задача с ID {} не найдена", id);
            throw new RuntimeException("Задача с ID " + id + " не найдена");
        }
    }

    public List<Task> getTasksByStatus(Status status) {
        logger.info("Поиск задач по статусу: {}", status.getDisplayName());
        List<Task> tasks = taskRepository.findByStatus(status);
        logger.info("Найдено {} задач со статусом {}", tasks.size(), status.getDisplayName());
        return tasks;
    }

    public List<Task> getActiveTasks() {
        logger.info("Поиск активных задач (NEW и IN_PROGRESS)");
        List<Status> activeStatuses = List.of(Status.NEW, Status.IN_PROGRESS);
        List<Task> tasks = taskRepository.findByStatusIn(activeStatuses);
        logger.info("Найдено {} активных задач", tasks.size());
        return tasks;
    }

    public Task updateTaskStatus(Long id, Status newStatus) {
        logger.info("Изменение статуса задачи с ID: {} на {}", id, newStatus.getDisplayName());

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            Status oldStatus = task.getStatus();

            task.setStatus(newStatus);
            Task updatedTask = taskRepository.save(task);

            logger.info("Статус задачи '{}' изменен с {} на {}",
                    task.getTitle(), oldStatus.getDisplayName(), newStatus.getDisplayName());
            return updatedTask;
        } else {
            logger.error("Не удалось изменить статус - задача с ID {} не найдена", id);
            throw new RuntimeException("Задача с ID " + id + " не найдена");
        }
    }

    public List<Task> searchTasksByTitle(String title) {
        logger.info("Поиск задач по названию: {}", title);
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        logger.info("Найдено {} задач по поисковому запросу '{}'", tasks.size(), title);
        return tasks;
    }


}
