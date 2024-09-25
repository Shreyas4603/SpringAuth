package com.example.Auth.Services;

import com.example.Auth.Models.Tasks;
import com.example.Auth.Models.User;
import com.example.Auth.Repositories.TaskRepository;
import com.example.Auth.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> getAllTasks(String email) {
        // Fetch user by email
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("email in tasks: " + email + " ---------- " + user.getId());

        // Fetch tasks based on the user
        List<Tasks> userTasks = taskRepository.findByUserId(user.getId());

        System.out.println("tasks: " + userTasks);

        // Return the tasks as the response
        return ResponseEntity.ok(userTasks);
    }




    public ResponseEntity<Object> addTask(String email, String description) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Tasks newTask = new Tasks();
        newTask.setDescription(description);
        newTask.setStatus(Tasks.Status.PENDING);
        newTask.setUser(user);
        Tasks savedTask = taskRepository.save(newTask);
        return ResponseEntity.ok("Task created successfully with ID: " + savedTask.getTaskId());

    }

    public ResponseEntity<Object> updateTask(String email, Tasks body) {
        Tasks task = taskRepository.findById(body.getTaskId()).get();
        task.setStatus(body.getStatus());
        task.setDescription(body.getDescription());

        taskRepository.save(task);
        return ResponseEntity.ok("Task updated successfully with ID: " + task.getTaskId());

    }


}
