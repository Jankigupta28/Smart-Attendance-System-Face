package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendRepo.UserRepo;
import org.attend.attend_ai.attendService.StudentService;
import org.attend.attend_ai.model.EndUser;
import org.attend.attend_ai.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// @CrossOrigin
@CrossOrigin(origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500"
})
@RestController
public class StudentController {

    @Autowired
    StudentService service;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(service.getAllStudents(), HttpStatus.OK);
    }

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        // new
        if (service.existsByEmail(student.getEmail()) ||
                service.existsByEnrollmentNumber(student.getEnrollmentNumber())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        
        String encodedPassword = encoder.encode(student.getPassword());
        student.setPassword(encodedPassword);
        
        Student saved = service.addStudent(student);

        EndUser user = new EndUser();
        user.setEmail(student.getEmail());
        user.setPassword(encodedPassword);
        user.setUserRefId(student.getEnrollmentNumber());
        user.setRole("STUDENT");
        userRepo.save(user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/student/{enrollmentNumber}")
    public ResponseEntity<Student> findStudent(@PathVariable String enrollmentNumber) {
        Optional<Student> s = service.findStudent(enrollmentNumber);
        if (s.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        else
            return new ResponseEntity<>(s.get(), HttpStatus.OK);
    }

    @DeleteMapping("/students/{enrollmentNumber}")
    public ResponseEntity<String> deleteStudent(@PathVariable String enrollmentNumber) {
        service.deleteStudent(enrollmentNumber);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PutMapping("/students/{enrollmentNumber}")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable String enrollmentNumber) {
        student.setEnrollmentNumber(enrollmentNumber);
        Student updated = service.updateStudent(student);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}