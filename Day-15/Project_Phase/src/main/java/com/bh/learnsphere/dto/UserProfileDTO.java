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
public class UserProfileDTO {
    private Long id;
    private String email;
    private String fullName;
    private Role role;
    private String profilePicture;
    private Integer points;
    private List<BadgeDTO> badges;
    private Integer coursesCreated; // For instructors
    private Integer coursesEnrolled; // For students
    private Integer coursesCompleted; // For students
}