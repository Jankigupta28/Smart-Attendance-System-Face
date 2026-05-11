package org.attend.attend_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.attend.attend_ai.attendRepo")
public class AttendAiApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(AttendAiApplication.class, args);
    }

}
