package org.attend.attend_ai.attendRepo;

import org.attend.attend_ai.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface AttendanceRepo extends JpaRepository<Attendance,Integer> {

    List<Attendance> findByStudent_EnrollmentNumber(String enrollmentNumber);

    List<Attendance> findByStudent_EnrollmentNumberAndCourse_CourseId(String enrollmentNumber, int courseId);

    List<Attendance> findByCourse_CourseId(int courseId);

    List<Attendance> findByTimeStampBetween(LocalDateTime start, LocalDateTime end);

    List<Attendance> findByStudent_EnrollmentNumberAndCourse_CourseIdAndTimeStampBetween(String enrollmentNumber, int courseId, LocalDateTime start, LocalDateTime end);
}
