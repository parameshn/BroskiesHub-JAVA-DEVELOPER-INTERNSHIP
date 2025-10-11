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
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/public")
    public ResponseEntity<List<CourseDTO>> getPublishedCourses() {
        return ResponseEntity.ok(courseService.getPublishedCourses());
    }

    @GetMapping("/public/search")
    public ResponseEntity<List<CourseDTO>> searchCourses(@RequestParam String keyword) {
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseDTOById(courseId));
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody CreateCourseRequest request,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user.getRole() != com.bh.learnsphere.model.Role.INSTRUCTOR &&
                    user.getRole() != com.bh.learnsphere.model.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only instructors can create courses");
            }

            return ResponseEntity.ok(courseService.createCourse(request, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<?> enrollInCourse(@PathVariable Long courseId, Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(enrollmentService.enrollInCourse(user.getId(), courseId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getCurrentUser(Authentication authentication) {
        // Implementation to get current user from authentication
        // This would typically involve a service call to get user by email
        return new User(); // Simplified - implement properly
    }
}