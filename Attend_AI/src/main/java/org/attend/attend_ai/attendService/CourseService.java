
package org.attend.attend_ai.attendService;

import org.attend.attend_ai.attendRepo.CourseRepo;
import org.attend.attend_ai.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    public List<Course> getCourses() {
        return courseRepo.findAll();
    }

    public Optional<Course> getCourse(int courseId) {
        return courseRepo.findById(courseId);
    }

    public void addCourse(Course course) {
        courseRepo.save(course);
    }

    public Course updateCourse(Course course) {
        courseRepo.save(course);
        return course;
    }

    public void deleteCourse(int courseId) {
        courseRepo.deleteById(courseId);
    }

    public Course getCourseById(int courseId) {  // int parameter
        return courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
    }
}
