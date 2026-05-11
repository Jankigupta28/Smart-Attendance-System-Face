package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendRepo.UserRepo;
import org.attend.attend_ai.model.EndUser;
import org.attend.attend_ai.model.LoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {


@Autowired
private UserRepo userRepo;

  @Autowired
    private BCryptPasswordEncoder encoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        System.out.println("Login attempt - Email: " + request.getEmail());

        EndUser user = userRepo.findByEmail(request.getEmail());
        System.out.println("User found: " + (user != null ? user.getEmail() : "null"));

        if (user != null) {
            System.out.println("Password match: " + user.getPassword().equals(request.getPassword()));
        }

        if (user != null && user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.ok(Map.of(
                    "status", "Login success",
                    "role", user.getRole(),
                    "userId", user.getUserRefId()
            ));
        }
        return ResponseEntity.badRequest().body(Map.of("status", "Invalid credentials"));
    }

    }
