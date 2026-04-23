package com.elearning.service;

import com.elearning.dto.LessonDTO;
import com.elearning.exception.ResourceNotFoundException;
import com.elearning.exception.UnauthorizedException;
import com.elearning.model.entity.Course;
import com.elearning.model.entity.Lesson;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.LessonRepository;
import com.elearning.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Course course = courseRepository.findById(lessonDTO.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", lessonDTO.getCourseId()));

        verifyInstructor(course.getInstructor().getId());

        Lesson lesson = modelMapper.map(lessonDTO, Lesson.class);
        lesson.setCourse(course);
        lesson.setIsPublished(false);

        Lesson savedLesson = lessonRepository.save(lesson);
        return modelMapper.map(savedLesson, LessonDTO.class);
    }

    public LessonDTO getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));
        return modelMapper.map(lesson, LessonDTO.class);
    }

    public List<LessonDTO> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByLessonNumberAsc(courseId).stream()
            .map(lesson -> modelMapper.map(lesson, LessonDTO.class))
            .collect(Collectors.toList());
    }

    public LessonDTO updateLesson(Long id, LessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));

        verifyInstructor(lesson.getCourse().getInstructor().getId());

        if (lessonDTO.getTitle() != null) lesson.setTitle(lessonDTO.getTitle());
        if (lessonDTO.getContent() != null) lesson.setContent(lessonDTO.getContent());
        if (lessonDTO.getVideoUrl() != null) lesson.setVideoUrl(lessonDTO.getVideoUrl());
        if (lessonDTO.getDurationMinutes() != null) lesson.setDurationMinutes(lessonDTO.getDurationMinutes());
        if (lessonDTO.getLessonNumber() != null) lesson.setLessonNumber(lessonDTO.getLessonNumber());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return modelMapper.map(updatedLesson, LessonDTO.class);
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));

        verifyInstructor(lesson.getCourse().getInstructor().getId());
        lessonRepository.delete(lesson);
    }

    public LessonDTO publishLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));

        verifyInstructor(lesson.getCourse().getInstructor().getId());
        lesson.setIsPublished(true);
        Lesson publishedLesson = lessonRepository.save(lesson);
        return modelMapper.map(publishedLesson, LessonDTO.class);
    }

    private void verifyInstructor(Long instructorId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userPrincipal.getId().equals(instructorId) && !userPrincipal.getAuthorities().toString().contains("ADMIN")) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }
    }
}
