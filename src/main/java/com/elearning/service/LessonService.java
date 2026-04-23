package com.elearning.service;

import com.elearning.dto.LessonDTO;
import com.elearning.model.entity.Course;
import com.elearning.model.entity.Lesson;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.LessonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Course course = courseRepository.findById(lessonDTO.getCourseId())
            .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = modelMapper.map(lessonDTO, Lesson.java);
        lesson.setCourse(course);
        lesson.setIsPublished(false);

        if (lesson.getLessonNumber() == null) {
            int maxLessonNumber = course.getLessons().stream()
                .mapToInt(l -> l.getLessonNumber() != null ? l.getLessonNumber() : 0)
                .max()
                .orElse(0);
            lesson.setLessonNumber(maxLessonNumber + 1);
        }

        Lesson savedLesson = lessonRepository.save(lesson);
        return mapToDTO(savedLesson);
    }

    public LessonDTO getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return mapToDTO(lesson);
    }

    public List<LessonDTO> getLessonsByCourse(Long courseId) {
        courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        return lessonRepository.findByCourseIdOrderByLessonNumberAsc(courseId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<LessonDTO> getAllLessons() {
        return lessonRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public LessonDTO updateLesson(Long id, LessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        if (lessonDTO.getTitle() != null) lesson.setTitle(lessonDTO.getTitle());
        if (lessonDTO.getContent() != null) lesson.setContent(lessonDTO.getContent());
        if (lessonDTO.getVideoUrl() != null) lesson.setVideoUrl(lessonDTO.getVideoUrl());
        if (lessonDTO.getDurationMinutes() != null) lesson.setDurationMinutes(lessonDTO.getDurationMinutes());
        if (lessonDTO.getLessonNumber() != null) lesson.setLessonNumber(lessonDTO.getLessonNumber());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return mapToDTO(updatedLesson);
    }

    @Transactional
    public void publishLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lesson.setIsPublished(true);
        lessonRepository.save(lesson);
    }

    @Transactional
    public void unpublishLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lesson.setIsPublished(false);
        lessonRepository.save(lesson);
    }

    @Transactional
    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    private LessonDTO mapToDTO(Lesson lesson) {
        LessonDTO dto = modelMapper.map(lesson, LessonDTO.class);
        dto.setCourseName(lesson.getCourse().getTitle());
        dto.setQuizCount(lesson.getQuizzes().size());
        return dto;
    }
}
