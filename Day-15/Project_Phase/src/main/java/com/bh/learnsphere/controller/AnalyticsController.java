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
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<AnalyticsDTO> getCourseAnalytics(@PathVariable Long courseId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user.getRole() != com.bh.learnsphere.model.enums.Role.INSTRUCTOR &&
                    user.getRole() != com.bh.learnsphere.model.enums.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            AnalyticsDTO analytics = analyticsService.getCourseAnalytics(courseId);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentAnalytics(@PathVariable Long studentId,
            Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);

            // Students can only view their own analytics, instructors/admins can view any
            if (currentUser.getRole() == com.bh.learnsphere.model.enums.Role.STUDENT &&
                    !currentUser.getId().equals(studentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Implementation for student analytics
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/instructor/dashboard")
    public ResponseEntity<?> getInstructorDashboard(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user.getRole() != com.bh.learnsphere.model.enums.Role.INSTRUCTOR &&
                    user.getRole() != com.bh.learnsphere.model.enums.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Implementation for instructor dashboard analytics
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/overview")
    public ResponseEntity<?> getAdminOverview(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user.getRole() != com.bh.learnsphere.model.enums.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Implementation for admin overview analytics
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}/student-engagement")
    public ResponseEntity<?> getStudentEngagement(@PathVariable Long courseId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user.getRole() != com.bh.learnsphere.model.enums.Role.INSTRUCTOR &&
                    user.getRole() != com.bh.learnsphere.model.enums.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Implementation for student engagement analytics
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}/completion-rates")
    public ResponseEntity<?> getCompletionRates(@PathVariable Long courseId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user.getRole() != com.bh.learnsphere.model.enums.Role.INSTRUCTOR &&
                    user.getRole() != com.bh.learnsphere.model.enums.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Implementation for completion rate analytics
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getCurrentUser(Authentication authentication) {
        // In a real implementation, this would extract the user from the authentication
        // object
        // For now, return a mock user - you'll need to implement this based on your
        // UserService
        return User.builder()
                .id(1L)
                .email(authentication.getName())
                .role(com.bh.learnsphere.model.enums.Role.INSTRUCTOR) // Default for testing
                .build();
    }
}
