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
@RequestMapping("/api/discussions")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<DiscussionDTO>> getCourseDiscussions(@PathVariable Long courseId) {
        return ResponseEntity.ok(discussionService.getCourseDiscussions(courseId));
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<?> createDiscussion(@PathVariable Long courseId,
            @RequestParam String message,
            @RequestParam(required = false) Long parentId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(discussionService.createDiscussion(courseId, user.getId(), message, parentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getCurrentUser(Authentication authentication) {
        return new User(); // Simplified - implement properly
    }
}