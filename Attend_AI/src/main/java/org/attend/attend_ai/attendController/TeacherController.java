package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendRepo.UserRepo;
import org.attend.attend_ai.attendService.TeacherService;
import org.attend.attend_ai.model.EndUser;
import org.attend.attend_ai.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers(){
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<Optional<Teacher>> getTeacher(@PathVariable String teacherId){
        Optional<Teacher> teacher = teacherService.getTeacher(teacherId);
      return ResponseEntity.ok(teacher);
    }

    @PutMapping("/teacher/{teacherId}")
    public String updateTeacher(@RequestBody Teacher teacher, @PathVariable String teacherId) {
        teacher.setTeacherId(teacherId);
        teacherService.addTeacher(teacher);


        if (userRepo.findByEmail(String.valueOf(teacherId)) == null) {
            EndUser user = new EndUser();
            user.setEmail(teacher.getEmail());
            user.setPassword(teacher.getPassword());
            user.setUserRefId(teacher.getTeacherId());
            user.setRole("TEACHER");
            userRepo.save(user);
        }

        return "Successfully added";
    }

    @PostMapping("/teacher")
    public String addTeacher(@RequestBody Teacher teacher) {
        teacherService.addTeacher(teacher);

        EndUser user = new EndUser();
        user.setEmail(teacher.getEmail());
        user.setPassword(teacher.getPassword());
        user.setRole("TEACHER");
        userRepo.save(user);

        return "Successfully added";
    }

    @DeleteMapping("/teacher/{teacherId}")
    public String deleteTeacher(@PathVariable String teacherId){
       teacherService.deleteTeacher(teacherId);

        return "deleted";
    }


}
