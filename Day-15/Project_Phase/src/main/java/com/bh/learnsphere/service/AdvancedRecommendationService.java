package com.bh.learnsphere.service;

import com.bh.learnsphere.model.*;
import com.bh.learnsphere.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdvancedRecommendationService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ProgressRepository progressRepository;

    public Map<String, Object> getPersonalizedLearningPath(Long userId) {
        User user = getUserById(userId);
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(user);

        // Analyze user's learning patterns
        LearningProfile profile = analyzeLearningProfile(userId, enrollments);

        // Generate personalized recommendations
        List<Course> recommendedCourses = getRecommendedCourses(profile, userId);
        List<LearningGoal> learningGoals = generateLearningGoals(profile);
        Map<String, Double> skillGaps = identifySkillGaps(profile);

        Map<String, Object> learningPath = new HashMap<>();
        learningPath.put("userProfile", profile);
        learningPath.put("recommendedCourses", recommendedCourses);
        learningPath.put("learningGoals", learningGoals);
        learningPath.put("skillGaps", skillGaps);
        learningPath.put("estimatedCompletionTime", estimateCompletionTime(profile, recommendedCourses));
        learningPath.put("difficultyProgression", generateDifficultyProgression(profile));

        return learningPath;
    }

    private LearningProfile analyzeLearningProfile(Long userId, List<Enrollment> enrollments) {
        LearningProfile profile = new LearningProfile();

        // Calculate average progress and performance
        double avgProgress = enrollments.stream()
                .mapToDouble(Enrollment::getProgress)
                .average()
                .orElse(0.0);

        // Analyze quiz performance
        double avgQuizScore = calculateAverageQuizScore(userId);

        // Analyze learning pace
        double learningPace = calculateLearningPace(userId);

        // Identify preferred content types
        Map<ContentType, Double> contentPreferences = analyzeContentPreferences(userId);

        profile.setAverageProgress(avgProgress);
        profile.setAverageQuizScore(avgQuizScore);
        profile.setLearningPace(learningPace);
        profile.setContentPreferences(contentPreferences);
        profile.setPreferredDifficulty(identifyPreferredDifficulty(enrollments));
        profile.setLearningStrengths(identifyLearningStrengths(userId));
        profile.setLearningWeaknesses(identifyLearningWeaknesses(userId));

        return profile;
    }

    private double calculateAverageQuizScore(Long userId) {
        // Implementation to calculate average quiz score
        return 75.0; // Placeholder
    }

    private double calculateLearningPace(Long userId) {
        // Implementation to calculate learning pace based on progress over time
        return 1.0; // Placeholder - 1.0 is normal pace
    }

    private Map<ContentType, Double> analyzeContentPreferences(Long userId) {
        // Analyze which content types user engages with most
        Map<ContentType, Double> preferences = new HashMap<>();
        // Implementation would analyze time spent on different content types
        return preferences;
    }

    private DifficultyLevel identifyPreferredDifficulty(List<Enrollment> enrollments) {
        // Analyze which difficulty levels user performs best in
        return DifficultyLevel.INTERMEDIATE; // Placeholder
    }

    private List<String> identifyLearningStrengths(Long userId) {
        // Analyze topics where user performs well
        return Arrays.asList("Programming", "Problem Solving"); // Placeholder
    }

    private List<String> identifyLearningWeaknesses(Long userId) {
        // Analyze topics where user struggles
        return Arrays.asList("Theoretical Concepts", "Advanced Mathematics"); // Placeholder
    }

    private List<Course> getRecommendedCourses(LearningProfile profile, Long userId) {
        // Use ML to recommend courses based on profile
        try {
            return getMLBasedCourseRecommendations(profile, userId);
        } catch (Exception e) {
            // Fallback to content-based filtering
            return getContentBasedRecommendations(profile, userId);
        }
    }

    private List<Course> getMLBasedCourseRecommendations(LearningProfile profile, Long userId) throws Exception {
        // Create dataset for classification
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("user_avg_score"));
        attributes.add(new Attribute("course_difficulty"));
        attributes.add(new Attribute("content_match_score"));
        attributes.add(new Attribute("completion_likelihood"));

        Instances data = new Instances("Course_Recommendations", attributes, 0);
        data.setClassIndex(data.numAttributes() - 1);

        // Train classifier (in production, this would be pre-trained)
        Classifier classifier = new J48();
        classifier.buildClassifier(data);

        // Use classifier to predict best courses
        return getContentBasedRecommendations(profile, userId); // Simplified
    }

    private List<Course> getContentBasedRecommendations(LearningProfile profile, Long userId) {
        List<Course> allCourses = courseRepository.findByIsPublishedTrue();
        List<Enrollment> userEnrollments = enrollmentRepository.findByStudent(getUserById(userId));

        Set<Long> enrolledCourseIds = userEnrollments.stream()
                .map(enrollment -> enrollment.getCourse().getId())
                .collect(Collectors.toSet());

        return allCourses.stream()
                .filter(course -> !enrolledCourseIds.contains(course.getId()))
                .sorted((c1, c2) -> Double.compare(
                        calculateCourseMatchScore(c2, profile),
                        calculateCourseMatchScore(c1, profile)))
                .limit(5)
                .collect(Collectors.toList());
    }

    private double calculateCourseMatchScore(Course course, LearningProfile profile) {
        double score = 0.0;

        // Match difficulty
        if (course.getDifficultyLevel() == profile.getPreferredDifficulty()) {
            score += 0.4;
        }

        // Content type preference matching
        score += calculateContentTypeMatch(course, profile.getContentPreferences()) * 0.3;

        // Skill gap addressing
        score += calculateSkillGapMatch(course, profile.getLearningWeaknesses()) * 0.3;

        return score;
    }

    private double calculateContentTypeMatch(Course course, Map<ContentType, Double> preferences) {
        // Simplified implementation
        return 0.7; // Placeholder
    }

    private double calculateSkillGapMatch(Course course, List<String> weaknesses) {
        // Check if course addresses any learning weaknesses
        return 0.6; // Placeholder
    }

    private List<LearningGoal> generateLearningGoals(LearningProfile profile) {
        List<LearningGoal> goals = new ArrayList<>();

        // Generate goals based on profile analysis
        if (profile.getAverageQuizScore() < 70) {
            goals.add(new LearningGoal("Improve quiz performance", "Achieve 80% or higher in quizzes", 30));
        }

        if (profile.getLearningPace() < 0.8) {
            goals.add(new LearningGoal("Increase learning pace", "Complete lessons 20% faster", 45));
        }

        // Add goals based on skill gaps
        for (String weakness : profile.getLearningWeaknesses()) {
            goals.add(new LearningGoal("Improve " + weakness, "Master concepts in " + weakness, 60));
        }

        return goals;
    }

    private Map<String, Double> identifySkillGaps(LearningProfile profile) {
        Map<String, Double> skillGaps = new HashMap<>();

        // Analyze performance across different topics to identify gaps
        skillGaps.put("Programming Fundamentals", 0.3);
        skillGaps.put("Data Structures", 0.6);
        skillGaps.put("Algorithms", 0.8);
        skillGaps.put("System Design", 0.9);

        return skillGaps;
    }

    private String estimateCompletionTime(LearningProfile profile, List<Course> recommendedCourses) {
        double totalHours = recommendedCourses.stream()
                .mapToDouble(course -> course.getModules().stream()
                        .mapToDouble(module -> module.getLessons().stream()
                                .mapToDouble(Lesson::getDurationMinutes)
                                .sum())
                        .sum())
                .sum() / 60; // Convert minutes to hours

        // Adjust based on learning pace
        totalHours /= profile.getLearningPace();

        return String.format("%.1f hours", totalHours);
    }

    private List<DifficultyLevel> generateDifficultyProgression(LearningProfile profile) {
        List<DifficultyLevel> progression = new ArrayList<>();

        // Create a progression path based on current level
        DifficultyLevel current = profile.getPreferredDifficulty();

        switch (current) {
            case BEGINNER:
                progression.addAll(Arrays.asList(DifficultyLevel.BEGINNER, DifficultyLevel.INTERMEDIATE,
                        DifficultyLevel.ADVANCED));
                break;
            case INTERMEDIATE:
                progression.addAll(Arrays.asList(DifficultyLevel.INTERMEDIATE, DifficultyLevel.ADVANCED));
                break;
            case ADVANCED:
                progression.add(DifficultyLevel.ADVANCED);
                break;
        }

        return progression;
    }

    private User getUserById(Long userId) {
        // Implementation to get user
        return new User(); // Placeholder
    }

    // Helper classes for learning profile
    @Data
    public static class LearningProfile {
        private double averageProgress;
        private double averageQuizScore;
        private double learningPace;
        private Map<ContentType, Double> contentPreferences;
        private DifficultyLevel preferredDifficulty;
        private List<String> learningStrengths;
        private List<String> learningWeaknesses;
    }

    @Data
    @AllArgsConstructor
    public static class LearningGoal {
        private String title;
        private String description;
        private Integer priority; // 1-100
    }
}