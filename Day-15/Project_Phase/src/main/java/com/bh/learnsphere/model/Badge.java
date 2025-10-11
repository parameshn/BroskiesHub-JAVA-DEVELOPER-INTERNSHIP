package com.bh.learnsphere.model;

import jakarta.persistence.*; import lombok.*;

@Entity
@Table(name = "badges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private String iconUrl;

    @Builder.Default
    private Integer pointsRequired = 0;
}