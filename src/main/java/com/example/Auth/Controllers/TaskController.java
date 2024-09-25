package com.example.Auth.Controllers;


import com.example.Auth.Models.Tasks;
import com.example.Auth.Services.JWTService;
import com.example.Auth.Services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JWTService jwtService;


    @GetMapping("/all")
    private ResponseEntity<Object> getAllTasks(HttpServletRequest request){
        return taskService.getAllTasks(jwtService.extractEmail(request));
    }

    @PostMapping("/add")
    private ResponseEntity<Object> createTask(HttpServletRequest request, @RequestBody Tasks body){
        String email= jwtService.extractEmail(request);
        return taskService.addTask(email,body.getDescription());
    }

    @PutMapping("/update")
    private ResponseEntity<Object> updateTask(HttpServletRequest request, @RequestBody Tasks body){
        String email= jwtService.extractEmail(request);
        return taskService.updateTask(email,body);
    }

}
