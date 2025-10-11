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
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(notificationService.getUserNotifications(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(notificationService.getUnreadCount(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId, Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            notificationService.markAsRead(notificationId, user.getId());
            return ResponseEntity.ok("Notification marked as read");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/mark-all-read")
    public ResponseEntity<?> markAllAsRead(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            notificationService.markAllAsRead(user.getId());
            return ResponseEntity.ok("All notifications marked as read");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getCurrentUser(Authentication authentication) {
        // Implementation to get current user
        return new User(); // Simplified
    }
}