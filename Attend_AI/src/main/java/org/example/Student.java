package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
public class Student {
    @Id
    int Student_id;
    String Enrollment_Number;
    String Name;
    String Email;
    String Department;
    int Semester;
    @ManyToMany
    List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public int getStudent_id() {
        return Student_id;
    }

    public void setStudent_id(int student_id) {
        Student_id = student_id;
    }

    public String getEnrollment_Number() {
        return Enrollment_Number;
    }

    public void setEnrollment_Number(String enrollment_Number) {
        Enrollment_Number = enrollment_Number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public int getSemester() {
        return Semester;
    }

    public void setSemester(int semester) {
        Semester = semester;
    }

    @Override
    public String toString() {
        return "Student{" +
                "Student_id=" + Student_id +
                ", Enrollment_Number='" + Enrollment_Number + '\'' +
                ", Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", Department='" + Department + '\'' +
                ", Semester=" + Semester +

                '}';
    }
}
