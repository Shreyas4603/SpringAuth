package com.example.Auth.Models;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;




@Entity
@Table(name = "user")
public class User {


    @Id //Says the primary key is Id
    private String id;

    private  String email;
    private String password;

    private String ROLE;

    public String getROLE() {
        return ROLE;
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tasks> tasks = new ArrayList<>();

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }

    // Default constructor
    public User() {
    }

    //Default Parameterised constructor
    public User(String email,String password){
        this.email=email;
        this.password=password;
    }


    //Generate Getters
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //Generate Setters
    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }


    @PrePersist
    public void generateUUID(){
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public Object userData(){
        HashMap<Object,Object> data=new HashMap<>();
        data.put("id",this.id);
        data.put("email",this.email);
        return data;
    }



}
