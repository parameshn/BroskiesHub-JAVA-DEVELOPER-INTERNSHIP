package com.bh.learnsphere.repository;

import com.bh.learnsphere.model.User;
import com.bh.learnsphere.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.bh.learnsphere.model.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModuleOrderByOrderIndexAsc(Module module);
}