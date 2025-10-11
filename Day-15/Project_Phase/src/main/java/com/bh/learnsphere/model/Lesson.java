package com.bh.learnsphere.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 5000)
    private String content;
    
    @Enumerated(EnumType.STRING)
    private ContentType contentType; // VIDEO, TEXT, PDF, QUIZ
    
    private String contentUrl;
    
    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
    
    @Builder.Default
    private Integer orderIndex = 0;
    
    @Builder.Default
    private Integer durationMinutes = 0;
    
    private LocalDateTime scheduledReleaseDate;
    
    @Builder.Default
    private Boolean isFree = false;
}
