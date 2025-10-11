package com.bh.learnsphere.repository;

import com.bh.learnsphere.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;

@Repository
public interface LiveSessionRepository extends JpaRepository<LiveSession, Long> {
    List<LiveSession> findByCourse(Course course);

    List<LiveSession> findByInstructorAndScheduledTimeAfter(User instructor, LocalDateTime date);

    List<LiveSession> findByCourseAndScheduledTimeAfter(Course course, LocalDateTime date);
}