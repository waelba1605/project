package com.elearning.repository;

import com.elearning.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByInstructorId(Long instructorId);
    List<Course> findByIsPublishedTrue();
    List<Course> findByCategory(String category);
}
