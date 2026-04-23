package com.elearning.service;

import com.elearning.dto.CourseDTO;
import com.elearning.exception.DuplicateResourceException;
import com.elearning.exception.ResourceNotFoundException;
import com.elearning.exception.UnauthorizedException;
import com.elearning.model.entity.Course;
import com.elearning.model.entity.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.UserRepository;
import com.elearning.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    public CourseDTO createCourse(CourseDTO courseDTO) {
        if (courseRepository.findByCourseCode(courseDTO.getCourseCode()).isPresent()) {
            throw new DuplicateResourceException("Course", "courseCode", courseDTO.getCourseCode());
        }

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User instructor = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        Course course = modelMapper.map(courseDTO, Course.class);
        course.setInstructor(instructor);
        course.setIsPublished(false);

        Course savedCourse = courseRepository.save(course);
        return mapToCourseDTO(savedCourse);
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return mapToCourseDTO(course);
    }

    public List<CourseDTO> getAllPublishedCourses() {
        return courseRepository.findByIsPublishedTrue().stream()
            .map(this::mapToCourseDTO)
            .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category).stream()
            .filter(Course::getIsPublished)
            .map(this::mapToCourseDTO)
            .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
            .map(this::mapToCourseDTO)
            .collect(Collectors.toList());
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        verifyInstructor(course.getInstructor().getId());

        if (courseDTO.getTitle() != null) course.setTitle(courseDTO.getTitle());
        if (courseDTO.getDescription() != null) course.setDescription(courseDTO.getDescription());
        if (courseDTO.getCategory() != null) course.setCategory(courseDTO.getCategory());
        if (courseDTO.getLevel() != null) course.setLevel(courseDTO.getLevel());
        if (courseDTO.getDurationHours() != null) course.setDurationHours(courseDTO.getDurationHours());
        if (courseDTO.getPrice() != null) course.setPrice(courseDTO.getPrice());
        if (courseDTO.getThumbnailUrl() != null) course.setThumbnailUrl(courseDTO.getThumbnailUrl());

        Course updatedCourse = courseRepository.save(course);
        return mapToCourseDTO(updatedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        verifyInstructor(course.getInstructor().getId());
        courseRepository.delete(course);
    }

    public CourseDTO publishCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        verifyInstructor(course.getInstructor().getId());

        course.setIsPublished(true);
        Course publishedCourse = courseRepository.save(course);
        return mapToCourseDTO(publishedCourse);
    }

    public CourseDTO unpublishCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        verifyInstructor(course.getInstructor().getId());

        course.setIsPublished(false);
        Course unpublishedCourse = courseRepository.save(course);
        return mapToCourseDTO(unpublishedCourse);
    }

    private void verifyInstructor(Long instructorId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userPrincipal.getId().equals(instructorId) && !userPrincipal.getAuthorities().toString().contains("ADMIN")) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }
    }

    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO dto = modelMapper.map(course, CourseDTO.class);
        dto.setInstructorId(course.getInstructor().getId());
        dto.setInstructorName(course.getInstructor().getFirstName() + " " + course.getInstructor().getLastName());
        dto.setLessonCount(course.getLessons().size());
        dto.setEnrollmentCount(course.getEnrollments().size());
        return dto;
    }
}
