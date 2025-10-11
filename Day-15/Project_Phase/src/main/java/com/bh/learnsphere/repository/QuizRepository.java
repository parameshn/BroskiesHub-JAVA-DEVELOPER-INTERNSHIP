package com.bh.learnsphere.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import com.bh.learnsphere.model.Lesson;
import com.bh.learnsphere.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByLesson(Lesson lesson);
}
