package com.bh.learnsphere.service;

import com.bh.learnsphere.dto.*;
import com.bh.learnsphere.model.*;
import com.bh.learnsphere.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .role(registerRequest.getRole())
                .points(0)
                .badges(new HashSet<>())
                .build();

        return userRepository.save(user);
    }

    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<BadgeDTO> badgeDTOs = user.getBadges().stream()
                .map(this::convertToBadgeDTO)
                .collect(Collectors.toList());

        Integer coursesCreated = 0;
        Integer coursesEnrolled = 0;
        Integer coursesCompleted = 0;

        if (user.getRole() == Role.INSTRUCTOR) {
            coursesCreated = courseRepository.findByInstructor(user).size();
        } else if (user.getRole() == Role.STUDENT) {
            List<Enrollment> enrollments = enrollmentRepository.findByStudent(user);
            coursesEnrolled = enrollments.size();
            coursesCompleted = (int) enrollments.stream()
                    .filter(Enrollment::getIsCompleted)
                    .count();
        }

        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .profilePicture(user.getProfilePicture())
                .points(user.getPoints())
                .badges(badgeDTOs)
                .coursesCreated(coursesCreated)
                .coursesEnrolled(coursesEnrolled)
                .coursesCompleted(coursesCompleted)
                .build();
    }

    public void awardBadge(Long userId, String badgeName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Badge badge = badgeRepository.findByName(badgeName)
                .orElseThrow(() -> new RuntimeException("Badge not found"));

        user.getBadges().add(badge);
        userRepository.save(user);
    }

    public void addPoints(Long userId, Integer points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
    }

    private BadgeDTO convertToBadgeDTO(Badge badge) {
        return BadgeDTO.builder()
                .id(badge.getId())
                .name(badge.getName())
                .description(badge.getDescription())
                .iconUrl(badge.getIconUrl())
                .pointsRequired(badge.getPointsRequired())
                .build();
    }
}