package com.elearning.service;

import com.elearning.dto.CourseDTO;
import com.elearning.dto.CourseRequest;
import com.elearning.model.entity.Course;
import com.elearning.model.entity.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.UserRepository;
import com.elearning.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }

    public CourseDTO createCourse(CourseRequest courseRequest) {
        UserPrincipal userPrincipal = getCurrentUser();
        User instructor = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (courseRepository.findByCourseCode(courseRequest.getCourseCode()).isPresent()) {
            throw new RuntimeException("Course code already exists");
        }

        Course course = new Course();
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setCourseCode(courseRequest.getCourseCode());
        course.setCategory(courseRequest.getCategory());
        course.setLevel(courseRequest.getLevel());
        course.setThumbnailUrl(courseRequest.getThumbnailUrl());
        course.setDurationHours(courseRequest.getDurationHours());
        course.setPrice(courseRequest.getPrice());
        course.setInstructor(instructor);
        course.setIsPublished(false);

        Course savedCourse = courseRepository.save(course);
        return mapToDTO(savedCourse);
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapToDTO(course);
    }

    public Page<CourseDTO> getAllCourses(Pageable pageable, String category) {
        Page<Course> courses;
        if (category != null && !category.isEmpty()) {
            courses = courseRepository.findAll(pageable).map(course -> course.getCategory().equals(category) ? course : null)
                .map(course -> course != null ? course : new Course());
            // Alternative implementation
            List<Course> categoryList = courseRepository.findByCategory(category);
            courses = courseRepository.findAll(pageable).map(course -> 
                categoryList.contains(course) ? course : null
            ).filter(course -> course != null && course.getId() != null);
        } else {
            courses = courseRepository.findAll(pageable);
        }
        return courses.map(this::mapToDTO);
    }

    public Page<CourseDTO> getPublishedCourses(Pageable pageable) {
        List<Course> publishedCourses = courseRepository.findByIsPublishedTrue();
        return courseRepository.findAll(pageable)
            .map(course -> publishedCourses.contains(course) ? course : null)
            .filter(course -> course != null && course.getId() != null)
            .map(this::mapToDTO);
    }

    public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
        List<Course> courses = courseRepository.findByInstructorId(instructorId);
        return courses.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public CourseDTO updateCourse(Long id, CourseRequest courseRequest) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setCategory(courseRequest.getCategory());
        course.setLevel(courseRequest.getLevel());
        course.setThumbnailUrl(courseRequest.getThumbnailUrl());
        course.setDurationHours(courseRequest.getDurationHours());
        course.setPrice(courseRequest.getPrice());
        course.setUpdatedAt(LocalDateTime.now());

        Course updatedCourse = courseRepository.save(course);
        return mapToDTO(updatedCourse);
    }

    public CourseDTO publishCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setIsPublished(true);
        course.setUpdatedAt(LocalDateTime.now());

        Course publishedCourse = courseRepository.save(course);
        return mapToDTO(publishedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        courseRepository.delete(course);
    }

    private CourseDTO mapToDTO(Course course) {
        CourseDTO dto = modelMapper.map(course, CourseDTO.class);
        dto.setInstructorId(course.getInstructor().getId());
        dto.setInstructorName(course.getInstructor().getFirstName() + " " + course.getInstructor().getLastName());
        return dto;
    }
}
