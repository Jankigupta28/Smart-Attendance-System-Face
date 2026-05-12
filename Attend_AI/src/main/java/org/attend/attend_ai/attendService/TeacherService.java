package org.attend.attend_ai.attendService;

import org.attend.attend_ai.attendRepo.TeacherRepo;
import org.attend.attend_ai.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepo teacherRepo;

    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    public Optional<Teacher> getTeacher(String teacherId) {
        return teacherRepo.findByTeacherId(teacherId);
    }

    public Teacher addTeacher(Teacher teacher) {
       return teacherRepo.save(teacher);
    }

    public void deleteTeacher(String teacherId) {
        teacherRepo.deleteByTeacherId(teacherId);
    }

    public Teacher updateTeacher(Teacher teacher) {
        return teacherRepo.save(teacher);
    }
}
