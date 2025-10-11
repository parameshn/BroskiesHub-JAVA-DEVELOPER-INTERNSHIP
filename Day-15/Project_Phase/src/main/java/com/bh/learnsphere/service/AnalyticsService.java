package com.bh.learnsphere.service;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class AnalyticsService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ProgressRepository progressRepository;

    public AnalyticsDTO getCourseAnalytics(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);
        Long totalEnrollments = (long) enrollments.size();

        // Calculate average progress
        double averageProgress = enrollments.stream()
                .mapToDouble(Enrollment::getProgress)
                .average()
                .orElse(0.0);

        // Calculate completion rate
        long completedCount = enrollments.stream()
                .filter(Enrollment::getIsCompleted)
                .count();
        int completionRate = totalEnrollments > 0 ? (int) ((double) completedCount / totalEnrollments * 100) : 0;

        // Lesson analytics
        List<AnalyticsDTO.LessonAnalytics> lessonAnalytics = new ArrayList<>();
        for (Module module : course.getModules()) {
            for (Lesson lesson : module.getLessons()) {
                Long viewCount = progressRepository.findAll().stream()
                        .filter(p -> p.getLesson().equals(lesson) && p.getTimeSpentMinutes() > 0)
                        .count();

                Integer averageTimeSpent = (int) progressRepository.findAll().stream()
                        .filter(p -> p.getLesson().equals(lesson))
                        .mapToInt(Progress::getTimeSpentMinutes)
                        .average()
                        .orElse(0);

                Long completedCountForLesson = progressRepository.findAll().stream()
                        .filter(p -> p.getLesson().equals(lesson) && p.getIsCompleted())
                        .count();

                int lessonCompletionRate = totalEnrollments > 0
                        ? (int) ((double) completedCountForLesson / totalEnrollments * 100)
                        : 0;

                lessonAnalytics.add(AnalyticsDTO.LessonAnalytics.builder()
                        .lessonId(lesson.getId())
                        .lessonTitle(lesson.getTitle())
                        .viewCount(viewCount)
                        .averageTimeSpent(averageTimeSpent)
                        .completionRate(lessonCompletionRate)
                        .build());
            }
        }

        return AnalyticsDTO.builder()
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .totalEnrollments(totalEnrollments)
                .averageProgress(averageProgress)
                .completionRate(completionRate)
                .lessonAnalytics(lessonAnalytics)
                .build();
    }
}
