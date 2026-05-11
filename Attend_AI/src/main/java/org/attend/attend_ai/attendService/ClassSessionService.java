package org.attend.attend_ai.attendService;

import org.attend.attend_ai.attendRepo.ClassSessionRepo;
import org.attend.attend_ai.model.ClassSession;
import org.attend.attend_ai.model.StartSessionRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ClassSessionService {

    @Autowired
    private ClassSessionRepo classSessionRepo;


    public void startService(StartSessionRequestDTO request) {

        Optional<ClassSession> existing = classSessionRepo.findByTeacherIdAndCourseIdAndIsActiveTrue(
                request.getTeacherId(), request.getCourseId()
        );
        existing.ifPresent(s -> {
            s.setActive(false);
            classSessionRepo.save(s);
        });

        ClassSession session = new ClassSession();
        session.setTeacherId(request.getTeacherId());
        session.setCourseId(request.getCourseId());
        session.setCenterLat(request.getCenterLat());
        session.setCenterLng(request.getCenterLng());
        session.setRadius(request.getRadius());
        session.setStartTime(LocalDateTime.now());
        session.setActive(true);
        classSessionRepo.save(session);
    }

    public void endSession(String teacherId, int courseId) {
        Optional<ClassSession> activeSession = classSessionRepo.findByTeacherIdAndCourseIdAndIsActiveTrue(teacherId, courseId);
        if (activeSession.isPresent()) {
            ClassSession session = activeSession.get();
            session.setActive(false);
            classSessionRepo.save(session);
        }
    }


    public ClassSession getActiveSession(String teacherId, int courseId) {
        return classSessionRepo.findByTeacherIdAndCourseIdAndIsActiveTrue(teacherId, courseId)
                .orElseThrow(() -> new RuntimeException("No active session found for this course"));
    }
}
