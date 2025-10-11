package com.bh.learnsphere.repository;

import com.bh.learnsphere.model.User;
import com.bh.learnsphere.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import com.bh.learnsphere.model.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByName(String name);
}