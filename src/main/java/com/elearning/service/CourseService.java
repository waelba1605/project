package com.elearning.service;

import com.elearning.dto.CourseDTO;
import com.elearning.model.entity.Course;
import com.elearning.model.entity.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO, Long instructorId) {
        User instructor = userRepository.findById(instructorId)
            .orElseThrow(() -> new RuntimeException("Instructor not found"));

        if (courseRepository.findByCourseCode(courseDTO.getCourseCode()).isPresent()) {
            throw new RuntimeException("Course code already exists");
        }

        Course course = modelMapper.map(courseDTO, Course.class);
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

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<CourseDTO> getPublishedCourses() {
        return courseRepository.findByIsPublishedTrue().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
        userRepository.findById(instructorId)
            .orElseThrow(() -> new RuntimeException("Instructor not found"));

        return courseRepository.findByInstructorId(instructorId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        if (courseDTO.getTitle() != null) course.setTitle(courseDTO.getTitle());
        if (courseDTO.getDescription() != null) course.setDescription(courseDTO.getDescription());
        if (courseDTO.getCategory() != null) course.setCategory(courseDTO.getCategory());
        if (courseDTO.getLevel() != null) course.setLevel(courseDTO.getLevel());
        if (courseDTO.getThumbnailUrl() != null) course.setThumbnailUrl(courseDTO.getThumbnailUrl());
        if (courseDTO.getDurationHours() != null) course.setDurationHours(courseDTO.getDurationHours());
        if (courseDTO.getPrice() != null) course.setPrice(courseDTO.getPrice());

        Course updatedCourse = courseRepository.save(course);
        return mapToDTO(updatedCourse);
    }

    @Transactional
    public void publishCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        if (course.getLessons().isEmpty()) {
            throw new RuntimeException("Course must have at least one lesson to be published");
        }

        course.setIsPublished(true);
        courseRepository.save(course);
    }

    @Transactional
    public void unpublishCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setIsPublished(false);
        courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        courseRepository.delete(course);
    }

    private CourseDTO mapToDTO(Course course) {
        CourseDTO dto = modelMapper.map(course, CourseDTO.class);
        dto.setInstructorName(course.getInstructor().getFirstName() + " " + course.getInstructor().getLastName());
        dto.setLessonCount(course.getLessons().size());
        dto.setEnrollmentCount(course.getEnrollments().size());
        return dto;
    }
}
