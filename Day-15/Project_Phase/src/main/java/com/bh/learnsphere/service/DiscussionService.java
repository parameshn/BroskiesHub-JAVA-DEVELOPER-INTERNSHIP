package com.bh.learnsphere.service;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Discussion createDiscussion(Long courseId, Long userId, String message, Long parentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Discussion discussion = Discussion.builder()
                .course(course)
                .user(user)
                .message(message)
                .parent(parentId != null ? discussionRepository.findById(parentId).orElse(null) : null)
                .createdAt(new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                .build();

        // Award points for participation
        if (parentId == null) { // Only award for new posts, not replies
            UserService userService = new UserService(); // This should be injected properly
            // userService.addPoints(userId, 5);
        }

        return discussionRepository.save(discussion);
    }

    public List<DiscussionDTO> getCourseDiscussions(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Discussion> topLevelDiscussions = discussionRepository
                .findByCourseAndParentIsNullOrderByCreatedAtDesc(course);

        return topLevelDiscussions.stream()
                .map(this::convertToDiscussionDTO)
                .collect(Collectors.toList());
    }

    private DiscussionDTO convertToDiscussionDTO(Discussion discussion) {
        List<DiscussionDTO> replies = discussionRepository.findByParent(discussion).stream()
                .map(this::convertToDiscussionDTO)
                .collect(Collectors.toList());

        return DiscussionDTO.builder()
                .id(discussion.getId())
                .courseId(discussion.getCourse().getId())
                .userId(discussion.getUser().getId())
                .userName(discussion.getUser().getFullName())
                .message(discussion.getMessage())
                .createdAt(discussion.getCreatedAt())
                .parentId(discussion.getParent() != null ? discussion.getParent().getId() : null)
                .replies(replies)
                .build();
    }
}
