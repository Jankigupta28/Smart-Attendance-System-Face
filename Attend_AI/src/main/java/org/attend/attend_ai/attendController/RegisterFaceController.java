package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendRepo.StudentRepo;
import org.attend.attend_ai.attendService.StudentService;
import org.attend.attend_ai.model.FaceRegisterRequest;
import org.attend.attend_ai.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}, allowCredentials = "true", allowedHeaders = "*")

public class RegisterFaceController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StudentService registerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody FaceRegisterRequest request) {


        String url = "http://localhost:5000/register";
        String result = restTemplate.postForObject(url, request, String.class);

        if (result != null && result.contains("success")) {
            Optional<Student> student = registerService.findStudent(request.getEnrollmentNumber());
            if (student.isPresent()) {
                Student s = student.get();
                s.setFaceEncoding("REGISTERED");
                registerService.addStudent(s);
            }
            return ResponseEntity.ok("Face registered");
        }
        return ResponseEntity.badRequest().body("Failed");
    }


}
