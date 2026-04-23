package com.elearning.service;

import com.elearning.dto.EnrollmentDTO;
import com.elearning.exception.DuplicateResourceException;
import com.elearning.exception.ResourceNotFoundException;
import com.elearning.exception.UnauthorizedException;
import com.elearning.model.entity.Course;
import com.elearning.model.entity.Enrollment;
import com.elearning.model.entity.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.UserRepository;
import com.elearning.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    public EnrollmentDTO enrollStudent(Long courseId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        User student = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        if (enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId).isPresent()) {
            throw new DuplicateResourceException("Student", "enrollment", "already exists");
        }

        Enrollment enrollment = Enrollment.builder()
            .student(student)
            .course(course)
            .enrollmentStatus("ACTIVE")
            .progressPercentage(0)
            .certificateEarned(false)
            .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Send enrollment email
        emailService.sendCourseEnrollmentEmail(
            student.getEmail(),
            student.getFirstName(),
            course.getTitle()
        );

        return mapToEnrollmentDTO(savedEnrollment);
    }

    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        return mapToEnrollmentDTO(enrollment);
    }

    public List<EnrollmentDTO> getStudentEnrollments() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return enrollmentRepository.findByStudentId(userPrincipal.getId()).stream()
            .map(this::mapToEnrollmentDTO)
            .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
            .map(this::mapToEnrollmentDTO)
            .collect(Collectors.toList());
    }

    public void unenrollStudent(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!enrollment.getStudent().getId().equals(userPrincipal.getId())) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }

        enrollmentRepository.delete(enrollment);
    }

    public EnrollmentDTO updateEnrollmentStatus(Long enrollmentId, String status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!enrollment.getStudent().getId().equals(userPrincipal.getId())) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }

        enrollment.setEnrollmentStatus(status);
        
        if ("COMPLETED".equals(status)) {
            enrollment.setCompletedAt(LocalDateTime.now());
            enrollment.setCertificateEarned(true);
            
            emailService.sendCertificateEmail(
                enrollment.getStudent().getEmail(),
                enrollment.getStudent().getFirstName(),
                enrollment.getCourse().getTitle()
            );
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return mapToEnrollmentDTO(updatedEnrollment);
    }

    private EnrollmentDTO mapToEnrollmentDTO(Enrollment enrollment) {
        EnrollmentDTO dto = modelMapper.map(enrollment, EnrollmentDTO.class);
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        dto.setStudentEmail(enrollment.getStudent().getEmail());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseName(enrollment.getCourse().getTitle());
        return dto;
    }
}
