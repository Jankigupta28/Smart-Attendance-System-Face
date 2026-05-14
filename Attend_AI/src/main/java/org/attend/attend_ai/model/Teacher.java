package org.attend.attend_ai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id

    private String teacherId;
    private String name;
    private String department;
    @OneToMany(mappedBy = "teacher")
    @JsonIgnoreProperties({"teacher"})
    private List<Course> courses;
    private String email;
    private String password;


    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
    return name;
    }
    public void setName(String name) {
    this.name = name;
    }
}
