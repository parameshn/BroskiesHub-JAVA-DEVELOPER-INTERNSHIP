package com.bh.learnsphere.service;

import org.springframework.stereotype.Service;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ProctoringService {

    private Map<Long, Timer> userTimers = new HashMap<>();
    private Map<Long, Integer> userAbsenceCount = new HashMap<>();

    public void startProctoring(Long userId, Long quizId) {
        Timer timer = new Timer();
        userTimers.put(userId, timer);
        userAbsenceCount.put(userId, 0);

        // Schedule periodic checks for user presence
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkUserPresence(userId, quizId);
            }
        }, 0, 30000); // Check every 30 seconds
    }

    public void stopProctoring(Long userId) {
        Timer timer = userTimers.get(userId);
        if (timer != null) {
            timer.cancel();
            userTimers.remove(userId);
            userAbsenceCount.remove(userId);
        }
    }

    private void checkUserPresence(Long userId, Long quizId) {
        // In a real implementation, this would check webcam feed or browser focus
        // For this lite version, we'll simulate presence checks

        boolean isUserPresent = checkUserBrowserFocus(userId); // Simulated

        if (!isUserPresent) {
            Integer absenceCount = userAbsenceCount.get(userId);
            userAbsenceCount.put(userId, absenceCount + 1);

            if (absenceCount >= 3) { // 3 consecutive absences
                flagSuspiciousActivity(userId, quizId);
            }
        } else {
            userAbsenceCount.put(userId, 0); // Reset absence count
        }
    }

    private boolean checkUserBrowserFocus(Long userId) {
        // Simulated implementation
        // In production, this would use browser APIs to check if tab is active
        return Math.random() > 0.1; // 90% chance user is present
    }

    private void flagSuspiciousActivity(Long userId, Long quizId) {
        // Log suspicious activity
        System.out.println("Suspicious activity detected for user " + userId + " during quiz " + quizId);

        // In production, this would:
        // 1. Send notification to instructor
        // 2. Record the incident
        // 3. Possibly pause the quiz
    }
}