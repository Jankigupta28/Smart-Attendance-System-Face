package org.attend.attend_ai.attendRepo;

import org.attend.attend_ai.model.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassSessionRepo extends JpaRepository<ClassSession,Long> {

    Optional<ClassSession> findByTeacherIdAndCourseIdAndIsActiveTrue(String teacherId,int courseId);
}
