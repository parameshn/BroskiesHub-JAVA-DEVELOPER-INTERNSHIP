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
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Course createCourse(CreateCourseRequest request, Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .thumbnail(request.getThumbnail())
                .instructor(instructor)
                .price(request.getPrice())
                .difficultyLevel(request.getDifficultyLevel())
                .isPublished(false)
                .modules(new ArrayList<>())
                .build();

        return courseRepository.save(course);
    }

    public Module addModuleToCourse(Long courseId, String title, String description) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Module module = Module.builder()
                .title(title)
                .description(description)
                .course(course)
                .orderIndex(course.getModules().size())
                .lessons(new ArrayList<>())
                .build();

        return moduleRepository.save(module);
    }

    public Lesson addLessonToModule(Long moduleId, LessonDTO lessonDTO) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        Lesson lesson = Lesson.builder()
                .title(lessonDTO.getTitle())
                .content(lessonDTO.getContent())
                .contentType(lessonDTO.getContentType())
                .contentUrl(lessonDTO.getContentUrl())
                .module(module)
                .orderIndex(module.getLessons().size())
                .durationMinutes(lessonDTO.getDurationMinutes())
                .scheduledReleaseDate(lessonDTO.getScheduledReleaseDate())
                .isFree(lessonDTO.getIsFree())
                .build();

        return lessonRepository.save(lesson);
    }

    public List<CourseDTO> getPublishedCourses() {
        return courseRepository.findByIsPublishedTrue().stream()
                .map(this::convertToCourseDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> searchCourses(String keyword) {
        return courseRepository.searchPublishedCourses(keyword).stream()
                .map(this::convertToCourseDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseDTOById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return convertToCourseDTO(course);
    }

    private CourseDTO convertToCourseDTO(Course course) {
        Long enrollmentCount = enrollmentRepository.countByCourse(course);

        return CourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnail(course.getThumbnail())
                .price(course.getPrice())
                .isPublished(course.getIsPublished())
                .difficultyLevel(course.getDifficultyLevel())
                .instructorName(course.getInstructor().getFullName())
                .instructorId(course.getInstructor().getId())
                .createdAt(course.getCreatedAt())
                .totalModules(course.getModules().size())
                .totalLessons(course.getModules().stream()
                        .mapToInt(module -> module.getLessons().size())
                        .sum())
                .enrollmentCount(enrollmentCount)
                .build();
    }
}