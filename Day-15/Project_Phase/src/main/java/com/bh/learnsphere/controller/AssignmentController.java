package com.bh.learnsphere.controller;

import com.bh.learnsphere.dto.*;
import com.bh.learnsphere.model.*;
import com.bh.learnsphere.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/course/{courseId}")
    public ResponseEntity<?> createAssignment(@PathVariable Long courseId,
            @Valid @RequestBody Assignment assignment,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(assignmentService.createAssignment(assignment, courseId, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<?> submitAssignment(@PathVariable Long assignmentId,
            @RequestParam String submissionText,
            @RequestParam(required = false) String attachmentUrl,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity
                    .ok(assignmentService.submitAssignment(assignmentId, user.getId(), submissionText, attachmentUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<?> gradeAssignment(@PathVariable Long submissionId,
            @RequestParam Integer points,
            @RequestParam String feedback,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(assignmentService.gradeAssignment(submissionId, points, feedback, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{assignmentId}/submissions")
    public ResponseEntity<?> getAssignmentSubmissions(@PathVariable Long assignmentId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(assignmentService.getAssignmentSubmissions(assignmentId, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getCurrentUser(Authentication authentication) {
        return new User(); // Simplified
    }
}