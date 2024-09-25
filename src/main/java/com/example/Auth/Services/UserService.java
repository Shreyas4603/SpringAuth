package com.example.Auth.Services;

import com.example.Auth.Classes.LoginRequest;
import com.example.Auth.Models.User;
import com.example.Auth.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;



    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);




    public ResponseEntity<Object> register(User userRequest){
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());

        if (existingUser.isPresent()) {
            Map<String, String> res = new HashMap<>();
            res.put("data", "User already exists");
            return new ResponseEntity<>(res, HttpStatus.CONFLICT);
        }

        userRequest.setPassword(encoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(userRequest);

        Map<Object, Object> res = new HashMap<>();
        res.put("data",savedUser.userData());

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    public ResponseEntity<Object> login(LoginRequest body){
        Optional<User> userOpt = userRepository.findByEmail(body.getEmail());
        Map<String, String> res = new HashMap<>();
        System.out.println("email : "+body.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (encoder.matches(body.getPassword(), user.getPassword())) {
                // Generate Token
                String token = jwtService.generateToken(body.getEmail());

                res.put("data", "Login Successful");
                res.put("token", token);

                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                res.put("data", "Invalid Password");
                return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
            }
        } else {
            res.put("data", "User doesn't exist");
            return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
        }
    }





}
