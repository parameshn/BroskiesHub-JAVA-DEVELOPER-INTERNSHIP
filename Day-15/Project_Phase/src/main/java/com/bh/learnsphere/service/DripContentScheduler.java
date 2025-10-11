package com.bh.learnsphere.service;

import org.springframework.stereotype.Service;

import com.bh.learnsphere.model.Lesson;
import com.bh.learnsphere.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DripContentScheduler {

    @Autowired
    private LessonRepository lessonRepository;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void releaseScheduledContent() {
        List<Lesson> scheduledLessons = lessonRepository.findAll().stream()
                .filter(lesson -> lesson.getScheduledReleaseDate() != null)
                .filter(lesson -> lesson.getScheduledReleaseDate().isBefore(LocalDateTime.now()))
                .filter(lesson -> !lesson.getIsFree()) // Assuming scheduled release only for paid content
                .collect(java.util.stream.Collectors.toList());

        for (Lesson lesson : scheduledLessons) {
            lesson.setIsFree(true); // Make lesson available
            lessonRepository.save(lesson);
        }
    }
}
