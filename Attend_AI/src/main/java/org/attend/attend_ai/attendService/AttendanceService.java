package org.attend.attend_ai.attendService;

import org.attend.attend_ai.attendRepo.AttendanceRepo;
import org.attend.attend_ai.attendRepo.CourseRepo;
import org.attend.attend_ai.attendRepo.StudentRepo;
import org.attend.attend_ai.model.Attendance;
import org.attend.attend_ai.model.Course;
import org.attend.attend_ai.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    AttendanceRepo attendanceRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private CourseRepo courseRepo;

    public void saveAttendance(String enrollmentNumber, String courseId) {

       Student student = studentRepo.findByEnrollmentNumber(enrollmentNumber)
                       .orElseThrow(()->new RuntimeException("student not found"));

        Course course = courseRepo.findById(Integer.valueOf(courseId))
                        .orElseThrow(()-> new RuntimeException("course not found"));

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setTimeStamp(LocalDateTime.now());
        attendance.setStatus("PRESENT");

        attendanceRepo.save(attendance);
    }


    public List<Attendance> getAllAttendance() {
        return attendanceRepo.findAll();
    }

    public List<Attendance> getStudentAttendance(String enrollmentNumber) {
        return attendanceRepo.findByStudent_EnrollmentNumber(enrollmentNumber);
    }

    public List<Attendance> getCourseAttendance(String enrollmentNumber, int courseId) {
        return attendanceRepo.findByStudent_EnrollmentNumberAndCourse_CourseId(enrollmentNumber,courseId);
    }

    public List<Attendance> getCourseAllAttendance(int courseId) {
        return attendanceRepo.findByCourse_CourseId(courseId);
    }

    public List<Attendance> getAttendanceByDate(String date) {
            LocalDate localDate = LocalDate.parse(date);
            LocalDateTime start = localDate.atStartOfDay();
            LocalDateTime end = localDate.atTime(23, 59, 59);
            return attendanceRepo.findByTimeStampBetween(start, end);
    }

    public boolean isAttendanceAlreadyMarked(String enrollmentNumber, int courseId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        List<Attendance> todayAttendance = attendanceRepo.findByStudent_EnrollmentNumberAndCourse_CourseIdAndTimeStampBetween(
                enrollmentNumber, courseId, start, end
        );

        return !todayAttendance.isEmpty();
    }
}
