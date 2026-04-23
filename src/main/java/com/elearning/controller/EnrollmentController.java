package com.elearning.controller;

import com.elearning.dto.EnrollmentDTO;
import com.elearning.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentDTO> enrollCourse(@RequestParam Long courseId) {
        EnrollmentDTO enrollment = enrollmentService.enrollStudent(courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @GetMapping("/my-courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentDTO>> getMyEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.getStudentEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<EnrollmentDTO>> getCourseEnrollments(@PathVariable Long courseId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getCourseEnrollments(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
        EnrollmentDTO enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(enrollment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> unenrollCourse(@PathVariable Long id) {
        enrollmentService.unenrollStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentDTO> updateEnrollmentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        EnrollmentDTO enrollment = enrollmentService.updateEnrollmentStatus(id, status);
        return ResponseEntity.ok(enrollment);
    }
}
