package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendService.ClassSessionService;
import org.attend.attend_ai.model.StartSessionRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/class/session")
public class ClassSessionController {

    @Autowired
    private ClassSessionService classSessionService;

    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestBody StartSessionRequestDTO request){
classSessionService.startService(request);
return ResponseEntity.ok("session started");
    }

    @PostMapping("/end")
    public ResponseEntity<?> endSession(@RequestParam String teacherId,@RequestParam int courseId){
        classSessionService.endSession(teacherId,courseId);
        return ResponseEntity.ok("session ended");
    }

}
