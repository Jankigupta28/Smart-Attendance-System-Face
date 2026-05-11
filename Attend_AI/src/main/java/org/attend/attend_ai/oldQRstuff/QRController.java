//package org.attend.attend_ai.oldQRstuff;
//
//import com.google.zxing.WriterException;
//import org.attend.attend_ai.attendService.AttendanceService;
//import org.attend.attend_ai.attendService.CourseService;
//import org.attend.attend_ai.attendService.StudentService;
//import org.attend.attend_ai.model.Attendance;
//import org.attend.attend_ai.model.Course;
//import org.attend.attend_ai.model.Student;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.net.URI;
//import java.time.LocalDate;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/api/qr")
//public class QRController {
//
//      @Autowired
//    private  QRService qrService;
//      @Autowired
//      private AttendanceService attendanceService;
//    @Autowired
//    private StudentService studentService;
//    @Autowired
//    private CourseService courseService;
//
//
//    public QRController(QRService qrService) {
//        this.qrService = qrService;
//    }
//
//
//    @GetMapping(value="/generate/{id}/{courseId}", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<byte[]> getQrCode(@PathVariable String id, @PathVariable String courseId) throws IOException, WriterException {
//        long timestamp = System.currentTimeMillis();
//        String redirectUrl = "http://localhost:8080/api/qr/redirect/" + id + "/" + courseId + "/" + timestamp;
//        return ResponseEntity.ok(qrService.generateQRCode(redirectUrl, 250, 250));
//    }
//
//    @GetMapping("/redirect/{id}/{courseId}/{timestamp}")
//    public ResponseEntity<Void> redirect(@PathVariable String id,@PathVariable String courseId,@PathVariable long timestamp) {
//
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - timestamp > 60000) {
//            return ResponseEntity.status(HttpStatus.GONE).build();
//        }
//
//        LocalDate today = LocalDate.now();
//        boolean alreadyMarked = attendanceService.existsByStudentAndCourseAndDate(id, courseId, today);
//
//        if (alreadyMarked) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//
//        String targetUrl = "https://google.com";
//        Student student = studentService.findStudent(id)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//        Course course = courseService.getCourse(Integer.parseInt(courseId))
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//        Attendance attendance = new Attendance();
//
//
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .location(URI.create("https://google.com"))
//                .build();
//    }
//}
