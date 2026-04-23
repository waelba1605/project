package com.elearning.service;

import com.elearning.dto.EnrollmentDTO;
import com.elearning.model.entity.Enrollment;
import com.elearning.model.entity.User;
import com.elearning.model.entity.Course;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.UserRepository;
import com.elearning.repository.CourseRepository;
import com.elearning.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }

    public EnrollmentDTO enrollStudent(Long courseId) {
        UserPrincipal userPrincipal = getCurrentUser();
        User student = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId).isPresent()) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentStatus("ACTIVE");
        enrollment.setProgressPercentage(0);
        enrollment.setCertificateEarned(false);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapToDTO(savedEnrollment);
    }

    public List<EnrollmentDTO> getStudentEnrollments() {
        UserPrincipal userPrincipal = getCurrentUser();
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(userPrincipal.getId());
        return enrollments.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getCourseEnrollments(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollments.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        return mapToDTO(enrollment);
    }

    public void unenrollCourse(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    private EnrollmentDTO mapToDTO(Enrollment enrollment) {
        EnrollmentDTO dto = modelMapper.map(enrollment, EnrollmentDTO.class);
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        dto.setStudentEmail(enrollment.getStudent().getEmail());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseName(enrollment.getCourse().getTitle());
        return dto;
    }
}
