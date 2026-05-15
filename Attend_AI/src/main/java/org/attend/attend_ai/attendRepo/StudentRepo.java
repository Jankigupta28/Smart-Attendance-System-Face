package org.attend.attend_ai.attendRepo;

import org.attend.attend_ai.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, String> {
    Optional<Student> findByEnrollmentNumber(String enrollmentNumber);
    void deleteByEnrollmentNumber(String enrollmentNumber);

    // new
    boolean existsByEmail(String email);
    boolean existsByEnrollmentNumber(String enrollmentNumber);
}