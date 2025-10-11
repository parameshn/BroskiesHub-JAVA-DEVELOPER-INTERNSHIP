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
public class ProgressUpdateRequest {
    private Long lessonId;
    private Boolean isCompleted;
    private Integer timeSpentMinutes;
}