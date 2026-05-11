package org.attend.attend_ai.attendService;


import org.attend.attend_ai.attendRepo.StudentRepo;
import org.attend.attend_ai.model.Student;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class StudentService {

    @Autowired
    StudentRepo repo;

    public List<Student> getAllStudents() {
      return  repo.findAll();
    }

    public Student addStudent(Student student) {
      return repo.save(student);
    }

    public Optional<Student> findStudent(String enrollmentNumber) {
       return  repo.findByEnrollmentNumber(enrollmentNumber);
    }

    public void deleteStudent(String enrollmentNumber) {
       repo.deleteByEnrollmentNumber(enrollmentNumber);
    }

    public Student updateStudent(Student student) {
        repo.save(student);
        return student;
    }
}
