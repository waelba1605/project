package com.elearning.service;

import com.elearning.dto.EnrollmentDTO;
import com.elearning.model.entity.Course;
import com.elearning.model.entity.Enrollment;
import com.elearning.model.entity.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public EnrollmentDTO enrollStudent(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
            .student(student)
            .course(course)
            .enrollmentStatus("ACTIVE")
            .progressPercentage(0)
            .certificateEarned(false)
            .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapToDTO(savedEnrollment);
    }

    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        return mapToDTO(enrollment);
    }

    public List<EnrollmentDTO> getStudentEnrollments(Long studentId) {
        userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        return enrollmentRepository.findByStudentId(studentId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getCourseEnrollments(Long courseId) {
        courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        return enrollmentRepository.findByCourseId(courseId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentDTO updateEnrollmentProgress(Long enrollmentId, Integer progressPercentage) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setProgressPercentage(Math.min(progressPercentage, 100));

        if (progressPercentage >= 100) {
            enrollment.setEnrollmentStatus("COMPLETED");
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return mapToDTO(updatedEnrollment);
    }

    @Transactional
    public void unenrollStudent(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    @Transactional
    public void issueCertificate(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (enrollment.getProgressPercentage() < 100) {
            throw new RuntimeException("Student must complete the course to receive a certificate");
        }

        enrollment.setCertificateEarned(true);
        enrollmentRepository.save(enrollment);
    }

    private EnrollmentDTO mapToDTO(Enrollment enrollment) {
        EnrollmentDTO dto = modelMapper.map(enrollment, EnrollmentDTO.class);
        dto.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        dto.setStudentEmail(enrollment.getStudent().getEmail());
        dto.setCourseName(enrollment.getCourse().getTitle());
        return dto;
    }
}
