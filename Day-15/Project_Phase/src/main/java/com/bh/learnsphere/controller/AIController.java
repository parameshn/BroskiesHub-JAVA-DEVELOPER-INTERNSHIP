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
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AdvancedRecommendationService recommendationService;

    @GetMapping("/learning-path")
    public ResponseEntity<Map<String, Object>> getPersonalizedLearningPath(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            return ResponseEntity.ok(recommendationService.getPersonalizedLearningPath(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/skill-analysis")
    public ResponseEntity<Map<String, Object>> getSkillAnalysis(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            // Implementation for detailed skill analysis
            return ResponseEntity.ok(new HashMap<>());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    private User getCurrentUser(Authentication authentication) {
        return new User(); // Simplified
    }
}