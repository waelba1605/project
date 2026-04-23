package com.elearning.controller;

import com.elearning.dto.CourseDTO;
import com.elearning.model.entity.Course;
import com.elearning.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllPublishedCourses() {
        List<CourseDTO> courses = courseService.getAllPublishedCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CourseDTO>> getCoursesByCategory(@PathVariable String category) {
        List<CourseDTO> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<CourseDTO> courses = courseService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> publishCourse(@PathVariable Long id) {
        CourseDTO course = courseService.publishCourse(id);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{id}/unpublish")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> unpublishCourse(@PathVariable Long id) {
        CourseDTO course = courseService.unpublishCourse(id);
        return ResponseEntity.ok(course);
    }
}
