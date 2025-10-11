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
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private String thumbnail;
    private Double price;
    private Boolean isPublished;
    private DifficultyLevel difficultyLevel;
    private String instructorName;
    private Long instructorId;
    private LocalDateTime createdAt;
    private Integer totalModules;
    private Integer totalLessons;
    private Long enrollmentCount;
}