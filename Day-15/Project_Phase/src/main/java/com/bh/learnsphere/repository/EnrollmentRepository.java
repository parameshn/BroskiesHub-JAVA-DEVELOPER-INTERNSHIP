package com.bh.learnsphere.repository;

import com.bh.learnsphere.model.User;
import com.bh.learnsphere.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.bh.learnsphere.model.Enrollment;
import com.bh.learnsphere.model.Course;
import org.springframework.data.repository.query.Param;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

    List<Enrollment> findByStudent(User student);

    List<Enrollment> findByCourse(Course course);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course = :course")
    Long countByCourse(@Param("course") Course course);
}
