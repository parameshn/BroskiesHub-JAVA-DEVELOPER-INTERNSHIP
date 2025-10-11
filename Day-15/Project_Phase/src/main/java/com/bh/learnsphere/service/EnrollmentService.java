package com.bh.learnsphere.service;

import com.bh.learnsphere.dto.*;
import com.bh.learnsphere.model.*;
import com.bh.learnsphere.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private UserService userService;

    public Enrollment enrollInCourse(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if already enrolled
        if (enrollmentRepository.findByStudentAndCourse(student, course).isPresent()) {
            throw new RuntimeException("Already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .progress(0.0)
                .isCompleted(false)
                .lessonProgress(new ArrayList<>())
                .build();

        // Initialize progress for all lessons
        for (Module module : course.getModules()) {
            for (Lesson lesson : module.getLessons()) {
                Progress progress = Progress.builder()
                        .enrollment(enrollment)
                        .lesson(lesson)
                        .isCompleted(false)
                        .timeSpentMinutes(0)
                        .build();
                enrollment.getLessonProgress().add(progress);
            }
        }

        // Award points for enrollment
        userService.addPoints(studentId, 10);

        return enrollmentRepository.save(enrollment);
    }

    public void updateProgress(ProgressUpdateRequest request, Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(
                userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found")),
                courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found")))
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        Lesson lesson = enrollment.getCourse().getModules().stream()
                .flatMap(module -> module.getLessons().stream())
                .filter(l -> l.getId().equals(request.getLessonId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .orElseThrow(() -> new RuntimeException("Progress not found"));

        if (request.getIsCompleted() != null) {
            progress.setIsCompleted(request.getIsCompleted());
            if (request.getIsCompleted()) {
                progress.setCompletedAt(
                        new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                userService.addPoints(studentId, 25); // Points for completing a lesson
            }
        }

        if (request.getTimeSpentMinutes() != null) {
            progress.setTimeSpentMinutes(progress.getTimeSpentMinutes() + request.getTimeSpentMinutes());
        }

        progress.setLastAccessedAt(
                new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        progressRepository.save(progress);

        // Update overall course progress
        updateCourseProgress(enrollment);
    }

    private void updateCourseProgress(Enrollment enrollment) {
        Long totalLessons = enrollment.getCourse().getModules().stream()
                .mapToLong(module -> module.getLessons().size())
                .sum();

        Long completedLessons = progressRepository.countCompletedLessons(enrollment);

        double progress = totalLessons > 0 ? (double) completedLessons / totalLessons * 100 : 0;
        enrollment.setProgress(progress);

        // Check if course is completed
        if (progress >= 100 && !enrollment.getIsCompleted()) {
            enrollment.setIsCompleted(true);
            enrollment.setCompletedAt(
                    new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            userService.addPoints(enrollment.getStudent().getId(), 100); // Points for course completion
            userService.awardBadge(enrollment.getStudent().getId(), "Course Completer");
        }

        enrollmentRepository.save(enrollment);
    }

    public List<EnrollmentDTO> getStudentEnrollments(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return enrollmentRepository.findByStudent(student).stream()
                .map(this::convertToEnrollmentDTO)
                .collect(Collectors.toList());
    }

    private EnrollmentDTO convertToEnrollmentDTO(Enrollment enrollment) {
        return EnrollmentDTO.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getFullName())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .enrolledAt(enrollment.getEnrolledAt())
                .progress(enrollment.getProgress())
                .isCompleted(enrollment.getIsCompleted())
                .completedAt(enrollment.getCompletedAt())
                .build();
    }
}