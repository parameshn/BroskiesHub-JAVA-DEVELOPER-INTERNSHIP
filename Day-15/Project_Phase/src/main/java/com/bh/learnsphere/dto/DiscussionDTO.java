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
public class DiscussionDTO {
    private Long id;
    private Long courseId;
    private Long userId;
    private String userName;
    private String message;
    private LocalDateTime createdAt;
    private Long parentId;
    private List<DiscussionDTO> replies;
}
