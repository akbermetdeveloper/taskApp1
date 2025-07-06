package com.example.taskApp.repository;
import com.example.taskApp.model.Status;
import com.example.taskApp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByStatus(Status status);


    List<Task> findByTitleContainingIgnoreCase(String title);


    List<Task> findByStatusIn(List<Status> statuses);
}
