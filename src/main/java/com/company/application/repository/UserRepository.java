package com.company.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.application.entity.User;

// Bu arayuz, User entity'si icin veritabani islemlerini saglar.
public interface UserRepository extends JpaRepository<User, Long> {

	// Email adresine gore kullanici kaydini bulmak icin kullanilir.
	Optional<User> findByEmail(String email);

	// Register sirasinda email benzersizlik kontrolu yapmak icin kullanilir.
	boolean existsByEmail(String email);
}
