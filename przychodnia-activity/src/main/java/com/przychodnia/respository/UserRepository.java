package com.przychodnia.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.przychodnia.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByLogin(String login);

}
