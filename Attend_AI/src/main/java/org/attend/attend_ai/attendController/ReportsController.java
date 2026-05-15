package org.attend.attend_ai.attendController;

import org.attend.attend_ai.attendService.ReportsService;
import org.attend.attend_ai.model.Attendance;
import org.attend.attend_ai.model.Student;
import org.openpdf.text.Document;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
// import org.openpdf.text.pdf.PdfTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

// @RestController
// public class ReportsController {
@CrossOrigin(origins = "*")
@RestController
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @GetMapping("/report/student/{enrollmentNumber}")
    public ResponseEntity<byte[]> getStudentReport(@PathVariable String enrollmentNumber) {
        List<Attendance> student = reportsService.findStudent(enrollmentNumber);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();
        Paragraph para = new Paragraph("Attendance Report - " + enrollmentNumber);
        document.add(para);
        PdfPTable table = new PdfPTable(3);
        table.addCell("Course Name");
        table.addCell("Date");
        table.addCell("Status");

        for (Attendance att : student) {
            table.addCell(att.getCourse().getCourseName());
            table.addCell(att.getTimeStamp().toString());
            table.addCell(att.getStatus());
        }

        document.add(table);
        document.close();

        // return ResponseEntity.ok(out.toByteArray());
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=attendance-report.pdf")
                .header("Content-Type", "application/pdf")
                .body(out.toByteArray());
    }

    @GetMapping("/report/teacher/{teacherId}")
    public ResponseEntity<byte[]> getAllStudentReport(@PathVariable String teacherId) {
        List<Attendance> attendanceList = reportsService.getAllStudentReport(teacherId);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();
        Paragraph para = new Paragraph("Attendance Report - " + teacherId);
        document.add(para);
        PdfPTable table = new PdfPTable(5);
        table.addCell("Enrollment Number");
        table.addCell("Student Name");
        table.addCell("Course Name");
        table.addCell("Date");
        table.addCell("Status");

        // for (Attendance att : attendanceList) {
        // table.addCell(att.getStudent().getEnrollmentNumber());
        // table.addCell(att.getStudent().getName());
        // table.addCell(att.getCourse().getCourseName());
        // table.addCell(att.getTimeStamp().toString());
        // table.addCell(att.getStatus());
        // }
        System.out.println("Attendance Size: " + attendanceList.size());
        for (Attendance att : attendanceList) {
            String enrollment = att.getStudent() != null
                    ? att.getStudent().getEnrollmentNumber()
                    : "N/A";
            String name = att.getStudent() != null
                    ? att.getStudent().getName()
                    : "N/A";
            String course = att.getCourse() != null
                    ? att.getCourse().getCourseName()
                    : "N/A";
            String date = att.getTimeStamp() != null
                    ? att.getTimeStamp().toString()
                    : "N/A";
            String status = att.getStatus() != null
                    ? att.getStatus().toString()
                    : "N/A";

            table.addCell(enrollment);
            table.addCell(name);
            table.addCell(course);
            table.addCell(date);
            table.addCell(status);
        }

        document.add(table);
        document.close();

        // return ResponseEntity.ok(out.toByteArray());
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=attendance-report.pdf")
                .header("Content-Type", "application/pdf")
                .body(out.toByteArray());
    }

    @GetMapping("/course/{courseId}/percentage")
    public ResponseEntity<Map<String, Double>> getCoursePercentage(@PathVariable int courseId) {
        return ResponseEntity.ok(reportsService.getCourseAttendancePercentage(courseId));
    }

    @GetMapping("/teacher/{teacherId}/courses/percentage")
    public ResponseEntity<Map<String, Double>> getAllCoursesPercentage(@PathVariable String teacherId) {
        return ResponseEntity.ok(reportsService.getAllCoursesPercentageByTeacher(teacherId));
    }

}
