package org.attend.attend_ai.attendService;

import org.attend.attend_ai.attendRepo.AttendanceRepo;
import org.attend.attend_ai.attendRepo.CourseRepo;
import org.attend.attend_ai.model.Attendance;
import org.attend.attend_ai.model.Course;
import org.attend.attend_ai.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportsService {

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private CourseRepo courseRepo;


    public List<Attendance> findStudent(String enrollmentNumber) {
        return  attendanceRepo.findByStudent_EnrollmentNumber(enrollmentNumber);

    }

    public List<Attendance> getAllStudentReport(String teacherId) {
        List<Course> courses = courseRepo.findByTeacher_TeacherId(teacherId);
        List<Attendance> allAttendance = new ArrayList<>();

        for (Course course : courses) {
            List<Attendance> attendanceList = attendanceRepo.findByCourse_CourseId(course.getCourseId());
            allAttendance.addAll(attendanceList);
        }

        return allAttendance;
    }

    public Map<String, Double> getCourseAttendancePercentage(int courseId) {
        List<Attendance> attendanceList = attendanceRepo.findByCourse_CourseId(courseId);
        long totalClasses = attendanceList.size();
        long presentCount = attendanceList.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();

        double percentage = (totalClasses == 0) ? 0 : (presentCount * 100.0 / totalClasses);

        Map<String, Double> result = new HashMap<>();
        result.put("percentage", percentage);
        return result;
    }

    public Map<String, Double> getAllCoursesPercentageByTeacher(String teacherId) {
        List<Course> courses = courseRepo.findByTeacher_TeacherId(teacherId);
        Map<String, Double> coursePercentages = new HashMap<>();

        for (Course course : courses) {
            double percentage = getCourseAttendancePercentage(course.getCourseId()).get("percentage");
            coursePercentages.put(course.getCourseName(), percentage);
        }
        return coursePercentages;
    }
}
