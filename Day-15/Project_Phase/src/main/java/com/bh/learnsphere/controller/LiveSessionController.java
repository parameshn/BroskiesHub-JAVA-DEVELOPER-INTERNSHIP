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
@RequestMapping("/api/live-sessions")
public class LiveSessionController {

    @Autowired
    private LiveSessionService liveSessionService;

    @PostMapping("/course/{courseId}")
    public ResponseEntity<?> scheduleLiveSession(@PathVariable Long courseId,
            @Valid @RequestBody LiveSession session,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(liveSessionService.scheduleLiveSession(session, courseId, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}/upcoming")
    public ResponseEntity<List<LiveSession>> getUpcomingSessions(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(liveSessionService.getUpcomingSessions(courseId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<?> completeSession(@PathVariable Long sessionId,
            @RequestParam String recordingUrl,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            liveSessionService.completeSession(sessionId, recordingUrl, user.getId());
            return ResponseEntity.ok("Session marked as completed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getCurrentUser(Authentication authentication) {
        return new User(); // Simplified
    }
}