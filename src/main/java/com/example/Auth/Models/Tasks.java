package com.example.Auth.Models;


import jakarta.persistence.*;

import java.util.HashMap;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tasks")
public class Tasks {


    public enum Status {
        PENDING,
        COMPLETED
    }


    @Id
    private String taskId;
    private String  description;

    @Enumerated(EnumType.STRING) // To store the enum as a String in the DB
    private Status status;       // Use the Status enum here

//
    @ManyToOne
    @JoinColumn(name = "id")
    @JsonIgnore
    private User user;

    public Tasks(){}

    public Tasks(String taskId, String description, User user, Status status) {
        this.taskId = taskId;
        this.description = description;
        this.user = user;
        this.status = status;

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Object taskData() {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("taskId", this.taskId);
        data.put("description", this.description);
        data.put("status", this.status);
        return data;
    }




    @PrePersist
    public void generateUUID(){
        if (this.taskId == null) {
            this.taskId = UUID.randomUUID().toString();
        }
    }


}
