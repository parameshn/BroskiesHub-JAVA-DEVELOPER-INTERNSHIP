package com.bh.learnsphere.service;

import org.springframework.stereotype.Service;

import com.bh.learnsphere.dto.RecommendationDTO;
import com.bh.learnsphere.model.*;
import com.bh.learnsphere.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.associations.Apriori;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<RecommendationDTO> getCourseRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get user's enrolled courses and their categories/difficulties
        List<Enrollment> userEnrollments = enrollmentRepository.findByStudent(user);
        Set<String> enrolledCategories = new HashSet<>();
        Set<DifficultyLevel> preferredDifficulties = new HashSet<>();

        for (Enrollment enrollment : userEnrollments) {
            Course course = enrollment.getCourse();
            // Extract categories from course title/description (simplified)
            enrolledCategories.addAll(extractKeywords(course.getTitle() + " " + course.getDescription()));
            preferredDifficulties.add(course.getDifficultyLevel());
        }

        // Get all published courses not enrolled by user
        List<Course> allCourses = courseRepository.findByIsPublishedTrue();
        List<Course> unenrolledCourses = allCourses.stream()
                .filter(course -> userEnrollments.stream()
                        .noneMatch(enrollment -> enrollment.getCourse().equals(course)))
                .collect(Collectors.toList());

        // Calculate recommendation scores
        List<RecommendationDTO> recommendations = new ArrayList<>();
        for (Course course : unenrolledCourses) {
            double score = calculateRecommendationScore(course, enrolledCategories, preferredDifficulties,
                    userEnrollments);

            if (score > 0.3) { // Only recommend if score is above threshold
                recommendations.add(RecommendationDTO.builder()
                        .courseId(course.getId())
                        .courseTitle(course.getTitle())
                        .courseDescription(course.getDescription())
                        .thumbnail(course.getThumbnail())
                        .recommendationScore(score)
                        .recommendationReason(
                                generateRecommendationReason(course, enrolledCategories, preferredDifficulties))
                        .build());
            }
        }

        // Sort by recommendation score descending
        recommendations.sort((r1, r2) -> Double.compare(r2.getRecommendationScore(), r1.getRecommendationScore()));

        return recommendations.stream().limit(10).collect(Collectors.toList());
    }

    private double calculateRecommendationScore(Course course, Set<String> enrolledCategories,
            Set<DifficultyLevel> preferredDifficulties,
            List<Enrollment> userEnrollments) {
        double score = 0.0;

        // Category matching (50% weight)
        Set<String> courseKeywords = extractKeywords(course.getTitle() + " " + course.getDescription());
        double categoryMatch = calculateJaccardSimilarity(enrolledCategories, courseKeywords);
        score += categoryMatch * 0.5;

        // Difficulty preference (30% weight)
        if (preferredDifficulties.contains(course.getDifficultyLevel())) {
            score += 0.3;
        }

        // Popularity (20% weight)
        long enrollmentCount = enrollmentRepository.countByCourse(course);
        long maxEnrollment = courseRepository.findByIsPublishedTrue().stream()
                .mapToLong(c -> enrollmentRepository.countByCourse(c))
                .max()
                .orElse(1L);
        double popularityScore = (double) enrollmentCount / maxEnrollment;
        score += popularityScore * 0.2;

        return score;
    }

    private Set<String> extractKeywords(String text) {
        // Simplified keyword extraction - in production, use NLP libraries
        String[] commonWords = { "the", "and", "or", "but", "in", "on", "at", "to", "for", "of", "with", "by" };
        Set<String> commonWordsSet = Set.of(commonWords);

        return Arrays.stream(text.toLowerCase().split("\\s+"))
                .filter(word -> word.length() > 3 && !commonWordsSet.contains(word))
                .collect(Collectors.toSet());
    }

    private double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty())
            return 0.0;

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    private String generateRecommendationReason(Course course, Set<String> enrolledCategories,
            Set<DifficultyLevel> preferredDifficulties) {
        List<String> reasons = new ArrayList<>();

        Set<String> courseKeywords = extractKeywords(course.getTitle() + " " + course.getDescription());
        Set<String> commonKeywords = new HashSet<>(enrolledCategories);
        commonKeywords.retainAll(courseKeywords);

        if (!commonKeywords.isEmpty()) {
            reasons.add("matches your interests in " + String.join(", ", commonKeywords));
        }

        if (preferredDifficulties.contains(course.getDifficultyLevel())) {
            reasons.add("matches your preferred difficulty level");
        }

        long enrollmentCount = enrollmentRepository.countByCourse(course);
        if (enrollmentCount > 50) {
            reasons.add("popular among other learners");
        }

        return reasons.isEmpty() ? "Recommended based on trending courses" : String.join(" and ", reasons);
    }

    // Advanced ML-based recommendations using Weka
    public List<RecommendationDTO> getMLBasedRecommendations(Long userId) {
        try {
            // This is a simplified example - in production, you'd use more sophisticated ML
            List<Enrollment> allEnrollments = enrollmentRepository.findAll();

            // Create dataset for association rule mining
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("user_id", (ArrayList<String>) null));
            attributes.add(new Attribute("course_id", (ArrayList<String>) null));

            Instances data = new Instances("Course_Recommendations", attributes, allEnrollments.size());
            data.setClassIndex(data.numAttributes() - 1);

            for (Enrollment enrollment : allEnrollments) {
                double[] values = new double[data.numAttributes()];
                values[0] = data.attribute(0).addStringValue(enrollment.getStudent().getId().toString());
                values[1] = data.attribute(1).addStringValue(enrollment.getCourse().getId().toString());
                data.add(new DenseInstance(1.0, values));
            }

            // Apply Apriori algorithm
            Apriori apriori = new Apriori();
            apriori.setClassIndex(data.classIndex());
            apriori.buildAssociations(data);

            // Process rules to generate recommendations
            return processAssociationRules(apriori, userId);

        } catch (Exception e) {
            // Fallback to content-based filtering if ML fails
            return getCourseRecommendations(userId);
        }
    }

    private List<RecommendationDTO> processAssociationRules(Apriori apriori, Long userId) {
        // Process association rules to find courses frequently taken together
        // This is a simplified implementation
        return getCourseRecommendations(userId);
    }
}