package com.iosProject.iosProject.controllers;

import com.iosProject.iosProject.bo.child.Child;
import com.iosProject.iosProject.bo.child.ChildWithCaseDetails;
import com.iosProject.iosProject.entity.ChildEntity;
import com.iosProject.iosProject.service.child.ChildService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/child")
public class ChildController {

    private static final Logger log = LoggerFactory.getLogger(ChildController.class);

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerChildForUser(@RequestBody Child child, @RequestParam Long userId) {
        log.info("Starting registerChildForUser");
        log.info("Calling childService registerChildForUser");
        childService.registerChildForUser(child, userId);
        log.info("Completed registerChildForUser");
        return ResponseEntity.status(HttpStatus.CREATED).body("Child registered successfully.");
    }

    @GetMapping("/{childId}")
    public ResponseEntity<ChildWithCaseDetails> getChildById(@PathVariable Long childId) {
        log.info("Starting getChildById");
        log.info("Calling childService getChildById");
        ChildWithCaseDetails child = childService.getChildById(childId);
        if (child != null) {
            return ResponseEntity.ok(child);
        } else {
            log.info("Completed getChildById");
            return ResponseEntity.notFound().build();
        }
    }
}
