package com.bh.learnsphere.service;

import com.bh.learnsphere.model.*;
import com.bh.learnsphere.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LiveSessionService {

    @Autowired
    private LiveSessionRepository liveSessionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public LiveSession scheduleLiveSession(LiveSession session, Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Verify instructor owns the course
        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Only course instructor can schedule live sessions");
        }

        session.setCourse(course);
        session.setInstructor(instructor);
        LiveSession savedSession = liveSessionRepository.save(session);

        // Notify enrolled students
        notifyStudentsAboutLiveSession(savedSession);

        return savedSession;
    }

    private void notifyStudentsAboutLiveSession(LiveSession session) {
        List<Enrollment> enrollments = session.getCourse().getEnrollments();
        for (Enrollment enrollment : enrollments) {
            notificationService.createNotification(
                    enrollment.getStudent().getId(),
                    "Live Session: " + session.getTitle(),
                    "A live session is scheduled for " + session.getScheduledTime(),
                    NotificationType.SYSTEM_ANNOUNCEMENT,
                    "/live-sessions/" + session.getId());
        }
    }

    public List<LiveSession> getUpcomingSessions(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return liveSessionRepository.findByCourseAndScheduledTimeAfter(course, LocalDateTime.now());
    }

    public void completeSession(Long sessionId, String recordingUrl, Long instructorId) {
        LiveSession session = liveSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Live session not found"));

        // Verify instructor owns the course
        if (!session.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Only session instructor can complete the session");
        }

        session.setIsCompleted(true);
        session.setRecordingUrl(recordingUrl);
        liveSessionRepository.save(session);
    }
}
