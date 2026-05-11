package org.attend.attend_ai.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceRegisterRequest {
    private String enrollmentNumber;
    private String imagePath;

    public void setEnrollmentNumber(String enrollmentNumber) {
        this.enrollmentNumber = enrollmentNumber;
    }

    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }
}
