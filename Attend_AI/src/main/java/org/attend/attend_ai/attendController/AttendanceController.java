package org.attend.attend_ai.attendController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.attend.attend_ai.attendService.AttendanceService;
import org.attend.attend_ai.attendService.ClassSessionService;
import org.attend.attend_ai.attendService.CourseService;
import org.attend.attend_ai.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class AttendanceController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ClassSessionService classSessionService;

    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestBody MarkAttendanceDTO markAttendanceDTO) {
        String url = "http://localhost:5000/recognize";
        Map<String, String> pythonRequest = new HashMap<>();
        pythonRequest.put("imagePath", markAttendanceDTO.getImagePath());

        String pythonResponse = restTemplate.postForObject(url, pythonRequest, String.class);


        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> pyRes;
        try {
            pyRes = mapper.readValue(pythonResponse, Map.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Python error");
        }

        String recognizedId = pyRes.get("enrollmentNumber");

        if (recognizedId != null && recognizedId.equals(markAttendanceDTO.getEnrollmentNumber())) {
            Course course = courseService.getCourseById(markAttendanceDTO.getCourseId());
            String teacherId = course.getTeacher().getTeacherId();
            ClassSession activeSession = classSessionService.getActiveSession(teacherId, markAttendanceDTO.getCourseId());

            double distance = GeofencingUtils.calculateDistance(
                    markAttendanceDTO.getCurrentLat(),
                    markAttendanceDTO.getCurrentLng(),
                    activeSession.getCenterLat(),
                    activeSession.getCenterLng()
            );

            if (distance <= activeSession.getRadius()) {
                if (attendanceService.isAttendanceAlreadyMarked(markAttendanceDTO.getEnrollmentNumber(), markAttendanceDTO.getCourseId())) {
                    return ResponseEntity.badRequest().body("Already marked today");
                }
                attendanceService.saveAttendance(markAttendanceDTO.getEnrollmentNumber(),
                        String.valueOf(markAttendanceDTO.getCourseId()));
                return ResponseEntity.ok("Attendance marked successfully");
            } else {
                return ResponseEntity.badRequest().body("Outside allowed area");
            }
        }
        return ResponseEntity.badRequest().body("Face not recognized");
    }

    @GetMapping("/attendance/all")
    public ResponseEntity<?> getAllAttendance() {
        List<Attendance> attendanceAll = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendanceAll);
    }

    @GetMapping("/attendance/student/{enrollmentNumber}")
    public ResponseEntity<?> getStudentAttendance(@PathVariable String enrollmentNumber){
        List<Attendance> attendance = attendanceService.getStudentAttendance(enrollmentNumber);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/attendance/student/{enrollmentNumber}/course/{courseId}")
    public ResponseEntity<?> getCourseAttendance(@PathVariable String enrollmentNumber,@PathVariable int courseId){
        List<Attendance> attendance = attendanceService.getCourseAttendance(enrollmentNumber,courseId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/attendance/course/{courseId}")
    public ResponseEntity<?> getCourseAllAttendance(@PathVariable  int courseId){
        List<Attendance> attendance = attendanceService.getCourseAllAttendance(courseId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/attendance/date")
    public ResponseEntity<?> getAttendanceByDate(@RequestParam String date) {
        List<Attendance> attendance = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendance);
    }

}
