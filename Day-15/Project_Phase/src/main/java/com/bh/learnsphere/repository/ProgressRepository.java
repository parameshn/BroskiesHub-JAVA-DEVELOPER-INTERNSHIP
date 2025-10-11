package com.bh.learnsphere.repository;

import com.bh.learnsphere.model.User;
import com.bh.learnsphere.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.bh.learnsphere.model.Enrollment;
import com.bh.learnsphere.model.Progress;
import com.bh.learnsphere.model.Lesson;


@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);

    List<Progress> findByEnrollment(Enrollment enrollment);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.enrollment = :enrollment AND p.isCompleted = true")
    Long countCompletedLessons(@Param("enrollment") Enrollment enrollment);
}