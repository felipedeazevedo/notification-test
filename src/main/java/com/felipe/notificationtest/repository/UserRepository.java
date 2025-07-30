package com.felipe.notificationtest.repository;

import com.felipe.notificationtest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}