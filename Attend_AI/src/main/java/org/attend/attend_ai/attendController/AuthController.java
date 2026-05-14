package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendRepo.TeacherRepo;
import org.attend.attend_ai.model.Teacher;
import org.attend.attend_ai.attendRepo.UserRepo;
import org.attend.attend_ai.model.EndUser;
import org.attend.attend_ai.model.LoginRequestDTO;
import org.attend.attend_ai.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private TeacherRepo teacherRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        System.out.println("Login attempt - Email: " + request.getEmail());

        EndUser user = userRepo.findByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "User not found"));
        }

        System.out.println("DB Email: " + user.getEmail());
        System.out.println("DB Password: " + user.getPassword());
        System.out.println("Input Password: " + request.getPassword());

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("status", "Invalid credentials"));
        }
            String refId = user.getUserRefId();
            String name = "Teacher";

            if (refId != null && !refId.isEmpty()) {
                Optional<Teacher> teacherOpt = teacherRepo.findByTeacherId(refId);
             if (teacherOpt.isPresent()) {
                name = teacherOpt.get().getName();
               }
            }
            return ResponseEntity.ok(Map.of(
                    "status", "Login success",
                    "role", user.getRole(),
                    "userId", user.getUserRefId(),
                    "name", name
            ));
        }
    //         return ResponseEntity.badRequest().body(Map.of("status","Invalid credentials"));
    // }
}
