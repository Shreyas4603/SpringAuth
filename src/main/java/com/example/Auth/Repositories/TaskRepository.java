package com.example.Auth.Repositories;

import com.example.Auth.Models.Tasks;
import com.example.Auth.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public interface TaskRepository extends JpaRepository<Tasks, String> {
    //    Optional<Tasks> findById(String taskId);
    List<Tasks> findByUserId(String userId);
    List<Tasks> findByUser(User user);


}
