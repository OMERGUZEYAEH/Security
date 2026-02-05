package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @GetMapping
    public ResponseEntity<List<String>> getTasks() {
        // Return a simple list to prove it works
        List<String> demoTasks = new ArrayList<>();
        demoTasks.add("Security Lab Complete");
        return ResponseEntity.ok(demoTasks);
    }
}