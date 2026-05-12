package org.attend.attend_ai.attendRepo;

import org.attend.attend_ai.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher,String> {
    Optional<Teacher> findByTeacherId(String teacherId);

    void deleteByTeacherId(String teacherId);
}
