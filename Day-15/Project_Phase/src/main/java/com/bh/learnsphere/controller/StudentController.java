package com.bh.learnsphere.controller;

import com.bh.learnsphere.dto.AnalyticsDTO;
import com.bh.learnsphere.model.User;
import com.bh.learnsphere.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/enrollments")
    public ResponseEntity<List<EnrollmentDTO>> getMyEnrollments(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(enrollmentService.getStudentEnrollments(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/progress")
    public ResponseEntity<?> updateProgress(@Valid @RequestBody ProgressUpdateRequest request,
            @RequestParam Long courseId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            enrollmentService.updateProgress(request, user.getId(), courseId);
            return ResponseEntity.ok("Progress updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendationDTO>> getRecommendations(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(recommendationService.getCourseRecommendations(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    private User getCurrentUser(Authentication authentication) {
        // Implementation to get current user from authentication
        return new User(); // Simplified - implement properly
    }
}