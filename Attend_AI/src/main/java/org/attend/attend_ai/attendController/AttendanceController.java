package org.attend.attend_ai.attendController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.attend.attend_ai.attendService.AttendanceService;
import org.attend.attend_ai.attendService.ClassSessionService;
import org.attend.attend_ai.attendService.CourseService;
import org.attend.attend_ai.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
@CrossOrigin

@RestController
public  class AttendanceController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ClassSessionService classSessionService;

    @PostMapping("/attendance/mark-active")
    public ResponseEntity<?> markAttendance(@RequestBody MarkAttendanceDTO dto) {
        System.out.println("Initiating attendance process for Student: " + dto.getEnrollmentNumber());

        // 1. Face Recognition Verification via Python Flask Server
        try {
            String flaskUrl = "http://localhost:5000/recognize";
            Map<String, String> payload = new HashMap<>();
            payload.put("enrollmentNumber", dto.getEnrollmentNumber());

            // Call the Flask service
            Map<String, Object> flaskResponse = restTemplate.postForObject(flaskUrl, payload, Map.class);

            if (flaskResponse == null || flaskResponse.get("enrollmentNumber") == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Face recognition failed: No response from verification server.");
            }

            String recognizedId = flaskResponse.get("enrollmentNumber").toString();
            if (!recognizedId.equals(dto.getEnrollmentNumber())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Identity mismatch: Face does not match the provided Enrollment Number.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Connection to Face Recognition service failed: " + e.getMessage());
        }

        // 2. Retrieve the Currently Active Class Session
        // Logic: We use the session's Course ID to ensure data integrity, ignoring potentially incorrect DTO values.
        ClassSession activeSession = classSessionService.findCurrentActiveSession();
        if (activeSession == null) {
            return ResponseEntity.badRequest()
                    .body("Access Denied: No active attendance session is currently running for this course.");
        }

        int verifiedCourseId = activeSession.getCourseId();
        System.out.println("Attendance linked to Active Course ID: " + verifiedCourseId);

        // 3. Geofencing/Location Verification
        double distanceInMeters = GeofencingUtils.calculateDistance(
                dto.getCurrentLat(), dto.getCurrentLng(),
                activeSession.getCenterLat(), activeSession.getCenterLng()
        );

        if (distanceInMeters > activeSession.getRadius()) {
            return ResponseEntity.badRequest()
                    .body("Location Error: You are outside the designated campus radius (" + Math.round(distanceInMeters) + "m away).");
        }

        // 4. Record Attendance
        // Final Step: Verify if attendance was already recorded to prevent duplicate entries.
        if (attendanceService.isAttendanceAlreadyMarked(dto.getEnrollmentNumber(), (int) verifiedCourseId)) {
            return ResponseEntity.badRequest()
                    .body("Duplicate Entry: Attendance has already been recorded for this session.");
        }

        try {
            attendanceService.saveAttendance(dto.getEnrollmentNumber(), String.valueOf(verifiedCourseId));
            return ResponseEntity.ok("Success: Attendance successfully recorded for Course ID " + verifiedCourseId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database Error: Failed to save attendance record.");
        }
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
