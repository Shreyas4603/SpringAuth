package com.example.Auth.Controllers;

import com.example.Auth.Classes.LoginRequest;
import com.example.Auth.Models.User;
import com.example.Auth.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // Use a more RESTful path
public class UserController {

    @Autowired
    private UserService userService;



    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @GetMapping("/token")
    public CsrfToken getToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    // Endpoint to create a new user
    @PostMapping("/")
    public ResponseEntity<Object> createUser(@RequestBody User userRequest) {

        return userService.register(userRequest);

    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest body) {
        return userService.login(body);
    }


    @GetMapping("/home")
    public String welcome(HttpServletRequest request) {
        return "Welcome, this endpoint Session id: " + request.getSession().getId();
    }
}
