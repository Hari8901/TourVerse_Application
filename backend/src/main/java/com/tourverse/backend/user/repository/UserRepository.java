package com.tourverse.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourverse.backend.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
}
