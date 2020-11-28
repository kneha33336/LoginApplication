package com.example.webApp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJPA extends JpaRepository<User, String>{
	User findByUserName(String username);
}
