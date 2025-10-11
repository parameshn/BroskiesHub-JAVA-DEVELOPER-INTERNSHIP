package com.bh.learnsphere.repository;

import com.bh.learnsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.bh.learnsphere.model.*;
import java.util.*;
import com.bh.learnsphere.model.enums.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findByRole(Role role);
}
