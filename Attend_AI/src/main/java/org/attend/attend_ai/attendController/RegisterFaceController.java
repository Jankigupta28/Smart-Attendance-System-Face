package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendRepo.StudentRepo;
import org.attend.attend_ai.attendService.StudentService;
import org.attend.attend_ai.model.FaceRegisterRequest;
import org.attend.attend_ai.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}, allowCredentials = "true", allowedHeaders = "*")

public class RegisterFaceController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody FaceRegisterRequest request) {
        String pythonUrl = "http://localhost:5000/get_encoding";

        try {

            ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();

                if ("success".equals(body.get("status"))) {

                    String encodingArray = body.get("encoding").toString();


                    Optional<Student> studentOpt = studentService.findStudent(request.getEnrollmentNumber());
                    if (studentOpt.isPresent()) {
                        Student s = studentOpt.get();
                        s.setFaceEncoding(encodingArray); // Save the string "[...]"
                        studentService.addStudent(s);
                        return ResponseEntity.ok("Face registered and saved in DB");
                    }
                    return ResponseEntity.status(404).body("Student ID not found");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("AI could not detect face");
    }

}
