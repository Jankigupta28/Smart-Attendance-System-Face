package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendRepo.UserRepo;
import org.attend.attend_ai.attendService.TeacherService;
import org.attend.attend_ai.model.EndUser;
import org.attend.attend_ai.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RequestMapping("/teacher")
@RestController
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping
    public ResponseEntity<?> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable String teacherId) {

        return teacherService.getTeacher(teacherId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{teacherId}")
    public ResponseEntity<Teacher> updateTeacher(@RequestBody Teacher teacher, @PathVariable String teacherId) {
        teacher.setTeacherId(teacherId);
        Teacher update = teacherService.updateTeacher(teacher);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        // new
        if (teacherService.existsByEmail(teacher.getEmail()) ||
                teacherService.existsByTeacherId(teacher.getTeacherId())) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        
        String encodedPassword = encoder.encode(teacher.getPassword());
        teacher.setPassword(encodedPassword);
        Teacher saved = teacherService.addTeacher(teacher);

        EndUser user = new EndUser();
        user.setEmail(teacher.getEmail());
        // user.setPassword(teacher.getPassword());
        user.setPassword(encoder.encode(teacher.getPassword()));
        user.setUserRefId(teacher.getTeacherId());
        user.setRole("TEACHER");
        userRepo.save(user);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{teacherId}")
    public String deleteTeacher(@PathVariable String teacherId) {
        teacherService.deleteTeacher(teacherId);

        return "deleted";
    }

}
