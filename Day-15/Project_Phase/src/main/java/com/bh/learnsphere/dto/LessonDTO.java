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
public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private ContentType contentType;
    private String contentUrl;
    private Long moduleId;
    private Integer orderIndex;
    private Integer durationMinutes;
    private LocalDateTime scheduledReleaseDate;
    private Boolean isFree;
    private Boolean isCompleted;
}
