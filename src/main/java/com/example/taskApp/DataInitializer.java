package com.example.taskApp;

import com.example.taskApp.model.Task;
import com.example.taskApp.model.Status;
import com.example.taskApp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TaskService taskService;

    @Override
    public void run(String... args) throws Exception {

        if (taskService.getAllTasks().isEmpty()) {
            System.out.println("Инициализация тестовых данных...");


            Task task1 = new Task();
            task1.setTitle("Изучить Spring Boot");
            task1.setDescription("Пройти курс по Spring Boot и создать первое приложение");
            task1.setStatus(Status.NEW);
            taskService.createTask(task1);

            Task task2 = new Task();
            task2.setTitle("Настроить базу данных");
            task2.setDescription("Настроить подключение к PostgreSQL/MySQL");
            task2.setStatus(Status.IN_PROGRESS);
            taskService.createTask(task2);

            Task task3 = new Task();
            task3.setTitle("Написать тесты");
            task3.setDescription("Создать unit и integration тесты для API");
            task3.setStatus(Status.NEW);
            taskService.createTask(task3);

            Task task4 = new Task();
            task4.setTitle("Создать документацию");
            task4.setDescription("Написать README и API документацию");
            task4.setStatus(Status.COMPLETED);
            taskService.createTask(task4);

            Task task5 = new Task();
            task5.setTitle("Деплой на сервер");
            task5.setDescription("Развернуть приложение на production сервере");
            task5.setStatus(Status.CANCELLED);
            taskService.createTask(task5);

            System.out.println("Тестовые данные успешно загружены!");
        } else {
            System.out.println("Данные уже существуют, инициализация пропущена.");
        }
    }
}
