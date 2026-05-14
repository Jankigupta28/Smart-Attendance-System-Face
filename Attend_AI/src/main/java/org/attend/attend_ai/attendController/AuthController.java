package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendRepo.TeacherRepo;
import org.attend.attend_ai.model.Teacher;
import org.attend.attend_ai.attendRepo.UserRepo;
import org.attend.attend_ai.attendService.StudentService;
import org.attend.attend_ai.model.EndUser;
import org.attend.attend_ai.model.LoginRequestDTO;
import org.attend.attend_ai.model.Teacher;
import org.attend.attend_ai.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    
    @Autowired
    private StudentService studentService;

    @Autowired
    private RestTemplate restTemplate;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        System.out.println("Login attempt - Email: " + request.getEmail());

        EndUser user = userRepo.findByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "User not found"));
        }

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
        if (user != null && encoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(Map.of(
                    "status", "Login success",
                    "role", user.getRole(),
                    "userId", user.getUserRefId(),
                    "name", name
            ));
        }
        return ResponseEntity.badRequest().body(Map.of("status", "Invalid credentials"));
    }

    @PostMapping("/face-login")
    public ResponseEntity<?> faceLogin(@RequestBody Map<String, String> request) {
        String enrollmentNumber = request.get("enrollmentNumber");
        String capturedPhoto = request.get("imagePath");

        Optional<Student> studentOpt = studentService.findStudent(enrollmentNumber);

        if (studentOpt.isPresent()) {
            Student s = studentOpt.get();
            String savedEncoding = s.getFaceEncoding();

            if (savedEncoding == null || savedEncoding.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("status", "Face not registered"));
            }

            // Python AI Server ko verification ke liye call karein
            String pythonUrl = "http://localhost:5000/verify";
            Map<String, String> pyPayload = Map.of(
                    "imagePath", capturedPhoto,
                    "savedEncoding", savedEncoding
            );

            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl, pyPayload, Map.class);
                Map<String, Object> body = response.getBody();

                if (body != null && (Boolean) body.get("match")) {
                    return ResponseEntity.ok(Map.of(
                            "status", "success",
                            "role", "STUDENT",
                            "userId", s.getEnrollmentNumber(),
                            "name", s.getName()
                    ));
                } else {
                    return ResponseEntity.status(401).body(Map.of("status", "Face mismatch"));
                }
            } catch (Exception e) {
                return ResponseEntity.status(500).body(Map.of("status", "AI Server Error"));
            }
        }
        return ResponseEntity.status(404).body(Map.of("status", "Student not found"));
    }


    }
