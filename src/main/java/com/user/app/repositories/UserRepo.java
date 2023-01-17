package com.user.app.repositories;

import com.user.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
}
