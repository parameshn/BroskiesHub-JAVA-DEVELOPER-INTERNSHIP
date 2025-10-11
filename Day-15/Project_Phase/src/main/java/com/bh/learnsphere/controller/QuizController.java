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
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<?> createQuiz(@Valid @RequestBody QuizDTO quizDTO, Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user.getRole() != com.bh.learnsphere.model.Role.INSTRUCTOR &&
                    user.getRole() != com.bh.learnsphere.model.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only instructors can create quizzes");
            }

            return ResponseEntity.ok(quizService.createQuiz(quizDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<?> addQuestion(@PathVariable Long quizId,
            @Valid @RequestBody QuestionDTO questionDTO,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user.getRole() != com.bh.learnsphere.model.Role.INSTRUCTOR &&
                    user.getRole() != com.bh.learnsphere.model.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only instructors can add questions");
            }

            return ResponseEntity.ok(quizService.addQuestionToQuiz(quizId, questionDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@Valid @RequestBody QuizSubmissionRequest submissionRequest,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            QuizResultDTO result = quizService.submitQuiz(submissionRequest, user.getId());

            // Award points based on quiz performance
            if (result.getPassed()) {
                // userService.addPoints(user.getId(), 50); // Points for passing quiz
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private User getCurrentUser(Authentication authentication) {
        return new User(); // Simplified - implement properly
    }
}