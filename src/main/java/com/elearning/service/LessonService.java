package com.elearning.service;

import com.elearning.dto.LessonDTO;
import com.elearning.dto.LessonRequest;
import com.elearning.model.entity.Lesson;
import com.elearning.model.entity.Course;
import com.elearning.repository.LessonRepository;
import com.elearning.repository.CourseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public LessonDTO createLesson(LessonRequest lessonRequest) {
        Course course = courseRepository.findById(lessonRequest.getCourseId())
            .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = new Lesson();
        lesson.setTitle(lessonRequest.getTitle());
        lesson.setContent(lessonRequest.getContent());
        lesson.setLessonNumber(lessonRequest.getLessonNumber());
        lesson.setVideoUrl(lessonRequest.getVideoUrl());
        lesson.setDurationMinutes(lessonRequest.getDurationMinutes());
        lesson.setCourse(course);
        lesson.setIsPublished(false);

        Lesson savedLesson = lessonRepository.save(lesson);
        return mapToDTO(savedLesson);
    }

    public LessonDTO getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return mapToDTO(lesson);
    }

    public List<LessonDTO> getLessonsByCourse(Long courseId) {
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByLessonNumberAsc(courseId);
        return lessons.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public LessonDTO updateLesson(Long id, LessonRequest lessonRequest) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setTitle(lessonRequest.getTitle());
        lesson.setContent(lessonRequest.getContent());
        lesson.setLessonNumber(lessonRequest.getLessonNumber());
        lesson.setVideoUrl(lessonRequest.getVideoUrl());
        lesson.setDurationMinutes(lessonRequest.getDurationMinutes());
        lesson.setUpdatedAt(LocalDateTime.now());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return mapToDTO(updatedLesson);
    }

    public LessonDTO publishLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setIsPublished(true);
        lesson.setUpdatedAt(LocalDateTime.now());

        Lesson publishedLesson = lessonRepository.save(lesson);
        return mapToDTO(publishedLesson);
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    private LessonDTO mapToDTO(Lesson lesson) {
        LessonDTO dto = modelMapper.map(lesson, LessonDTO.class);
        dto.setCourseId(lesson.getCourse().getId());
        dto.setCourseName(lesson.getCourse().getTitle());
        return dto;
    }
}
