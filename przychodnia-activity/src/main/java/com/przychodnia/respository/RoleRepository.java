package com.przychodnia.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.przychodnia.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);

}
