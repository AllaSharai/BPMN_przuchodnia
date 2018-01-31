package com.przychodnia.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.przychodnia.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> { 

}
