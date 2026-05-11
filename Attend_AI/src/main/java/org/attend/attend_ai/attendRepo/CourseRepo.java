package org.attend.attend_ai.attendRepo;

import org.attend.attend_ai.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course,Integer> {
    List<Course> findByTeacher_TeacherId(String teacherId);

}
