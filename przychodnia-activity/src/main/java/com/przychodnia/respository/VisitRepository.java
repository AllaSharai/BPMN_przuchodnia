package com.przychodnia.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.przychodnia.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> { 

}
