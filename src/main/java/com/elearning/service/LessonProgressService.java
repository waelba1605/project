package com.elearning.service;

import com.elearning.dto.LessonProgressDTO;
import com.elearning.model.entity.Lesson;
import com.elearning.model.entity.LessonProgress;
import com.elearning.model.entity.User;
import com.elearning.repository.LessonProgressRepository;
import com.elearning.repository.LessonRepository;
import com.elearning.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonProgressService {

    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public LessonProgressDTO startLesson(Long studentId, Long lessonId) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        LessonProgress progress = lessonProgressRepository.findByStudentIdAndLessonId(studentId, lessonId)
            .orElseGet(() -> LessonProgress.builder()
                .student(student)
                .lesson(lesson)
                .isCompleted(false)
                .progressPercentage(0)
                .watchedDurationMinutes(0)
                .build());

        return mapToDTO(lessonProgressRepository.save(progress));
    }

    @Transactional
    public LessonProgressDTO updateLessonProgress(Long studentId, Long lessonId, Integer watchedMinutes) {
        LessonProgress progress = lessonProgressRepository.findByStudentIdAndLessonId(studentId, lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson progress not found"));

        progress.setWatchedDurationMinutes(watchedMinutes);

        Lesson lesson = progress.getLesson();
        if (lesson.getDurationMinutes() != null && lesson.getDurationMinutes() > 0) {
            int percentage = (int) ((double) watchedMinutes / lesson.getDurationMinutes() * 100);
            progress.setProgressPercentage(Math.min(percentage, 100));
        }

        return mapToDTO(lessonProgressRepository.save(progress));
    }

    @Transactional
    public LessonProgressDTO completeLesson(Long studentId, Long lessonId) {
        LessonProgress progress = lessonProgressRepository.findByStudentIdAndLessonId(studentId, lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson progress not found"));

        progress.setIsCompleted(true);
        progress.setProgressPercentage(100);
        progress.setCompletedAt(LocalDateTime.now());

        if (progress.getLesson().getDurationMinutes() != null) {
            progress.setWatchedDurationMinutes(progress.getLesson().getDurationMinutes());
        }

        return mapToDTO(lessonProgressRepository.save(progress));
    }

    public LessonProgressDTO getLessonProgress(Long studentId, Long lessonId) {
        LessonProgress progress = lessonProgressRepository.findByStudentIdAndLessonId(studentId, lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson progress not found"));
        return mapToDTO(progress);
    }

    public List<LessonProgressDTO> getStudentProgress(Long studentId) {
        userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        return lessonProgressRepository.findAll().stream()
            .filter(p -> p.getStudent().getId().equals(studentId))
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<LessonProgressDTO> getLessonProgress(Long lessonId) {
        lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        return lessonProgressRepository.findAll().stream()
            .filter(p -> p.getLesson().getId().equals(lessonId))
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<LessonProgressDTO> getAllProgress() {
        return lessonProgressRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private LessonProgressDTO mapToDTO(LessonProgress progress) {
        LessonProgressDTO dto = modelMapper.map(progress, LessonProgressDTO.class);
        dto.setStudentName(progress.getStudent().getFirstName() + " " + progress.getStudent().getLastName());
        dto.setLessonName(progress.getLesson().getTitle());
        return dto;
    }
}
