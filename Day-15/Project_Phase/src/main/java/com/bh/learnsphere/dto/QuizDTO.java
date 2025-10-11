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
public class QuizDTO {
    private Long id;
    private String title;
    private String description;
    private Long lessonId;
    private Integer passingScore;
    private Integer timeLimit;
    private List<QuestionDTO> questions;
}
