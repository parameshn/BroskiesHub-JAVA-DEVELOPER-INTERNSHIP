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
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public Assignment createAssignment(Assignment assignment, Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Verify instructor owns the course
        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Only course instructor can create assignments");
        }

        assignment.setCourse(course);
        Assignment savedAssignment = assignmentRepository.save(assignment);

        // Notify enrolled students
        notifyStudentsAboutNewAssignment(savedAssignment);

        return savedAssignment;
    }

    private void notifyStudentsAboutNewAssignment(Assignment assignment) {
        List<Enrollment> enrollments = assignment.getCourse().getEnrollments();
        for (Enrollment enrollment : enrollments) {
            notificationService.createNotification(
                    enrollment.getStudent().getId(),
                    "New Assignment: " + assignment.getTitle(),
                    "A new assignment has been posted for " + assignment.getCourse().getTitle(),
                    NotificationType.NEW_ASSIGNMENT,
                    "/assignments/" + assignment.getId());
        }
    }

    public AssignmentSubmission submitAssignment(Long assignmentId, Long studentId,
            String submissionText, String attachmentUrl) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if student is enrolled in the course
        boolean isEnrolled = assignment.getCourse().getEnrollments().stream()
                .anyMatch(enrollment -> enrollment.getStudent().getId().equals(studentId));

        if (!isEnrolled) {
            throw new RuntimeException("Student is not enrolled in this course");
        }

        // Check if already submitted
        Optional<AssignmentSubmission> existingSubmission = submissionRepository.findByAssignmentAndStudent(assignment,
                student);

        if (existingSubmission.isPresent()) {
            throw new RuntimeException("Assignment already submitted");
        }

        AssignmentSubmission submission = AssignmentSubmission.builder()
                .assignment(assignment)
                .student(student)
                .submissionText(submissionText)
                .attachmentUrl(attachmentUrl)
                .isGraded(false)
                .build();

        return submissionRepository.save(submission);
    }

    public AssignmentSubmission gradeAssignment(Long submissionId, Integer points, String feedback, Long instructorId) {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        // Verify instructor owns the course
        if (!submission.getAssignment().getCourse().getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Only course instructor can grade assignments");
        }

        submission.setPoints(points);
        submission.setInstructorFeedback(feedback);
        submission.setIsGraded(true);

        // Notify student about grade
        notificationService.createNotification(
                submission.getStudent().getId(),
                "Assignment Graded: " + submission.getAssignment().getTitle(),
                "Your assignment has been graded. You scored " + points + "/"
                        + submission.getAssignment().getMaxPoints(),
                NotificationType.QUIZ_RESULT,
                "/assignments/" + submission.getAssignment().getId() + "/submission");

        return submissionRepository.save(submission);
    }

    public List<AssignmentSubmission> getAssignmentSubmissions(Long assignmentId, Long instructorId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Verify instructor owns the course
        if (!assignment.getCourse().getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Only course instructor can view submissions");
        }

        return submissionRepository.findByAssignment(assignment);
    }
}