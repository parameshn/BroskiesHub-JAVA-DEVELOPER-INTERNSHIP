package com.bh.learnsphere.dto;


import com.bh.learnsphere.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsDTO {
    private Long courseId;
    private String courseTitle;
    private Long totalEnrollments;
    private Double averageProgress;
    private Integer completionRate;
    private List<LessonAnalytics> lessonAnalytics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LessonAnalytics {
        private Long lessonId;
        private String lessonTitle;
        private Long viewCount;
        private Integer averageTimeSpent;
        private Integer completionRate;
    }
}
