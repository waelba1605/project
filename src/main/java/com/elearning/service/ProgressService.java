package com.elearning.service;

import com.elearning.dto.LessonProgressDTO;
import com.elearning.model.entity.LessonProgress;
import com.elearning.model.entity.Lesson;
import com.elearning.model.entity.User;
import com.elearning.repository.LessonProgressRepository;
import com.elearning.repository.LessonRepository;
import com.elearning.repository.UserRepository;
import com.elearning.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    @Autowired
    private LessonProgressRepository progressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }

    public LessonProgressDTO markLessonAsCompleted(Long lessonId) {
        UserPrincipal userPrincipal = getCurrentUser();
        User student = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        LessonProgress progress = progressRepository.findByStudentIdAndLessonId(student.getId(), lessonId)
            .orElse(new LessonProgress());

        if (progress.getId() == null) {
            progress.setStudent(student);
            progress.setLesson(lesson);
        }

        progress.setIsCompleted(true);
        progress.setProgressPercentage(100);
        progress.setCompletedAt(LocalDateTime.now());

        LessonProgress savedProgress = progressRepository.save(progress);
        return mapToDTO(savedProgress);
    }

    public LessonProgressDTO updateProgress(Long lessonId, Integer progressPercentage, Integer watchedDuration) {
        UserPrincipal userPrincipal = getCurrentUser();
        User student = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        LessonProgress progress = progressRepository.findByStudentIdAndLessonId(student.getId(), lessonId)
            .orElse(new LessonProgress());

        if (progress.getId() == null) {
            progress.setStudent(student);
            progress.setLesson(lesson);
        }

        progress.setProgressPercentage(progressPercentage);
        if (watchedDuration != null) {
            progress.setWatchedDurationMinutes(watchedDuration);
        }

        if (progressPercentage == 100) {
            progress.setIsCompleted(true);
            progress.setCompletedAt(LocalDateTime.now());
        }

        LessonProgress savedProgress = progressRepository.save(progress);
        return mapToDTO(savedProgress);
    }

    public LessonProgressDTO getLessonProgress(Long lessonId) {
        UserPrincipal userPrincipal = getCurrentUser();
        LessonProgress progress = progressRepository.findByStudentIdAndLessonId(userPrincipal.getId(), lessonId)
            .orElseThrow(() -> new RuntimeException("No progress found for this lesson"));
        return mapToDTO(progress);
    }

    public Map<String, Object> getCourseProgress(Long courseId) {
        UserPrincipal userPrincipal = getCurrentUser();
        
        // Get all lessons in the course
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByLessonNumberAsc(courseId);
        
        if (lessons.isEmpty()) {
            throw new RuntimeException("No lessons found in this course");
        }

        // Calculate progress
        int completedLessons = 0;
        int totalDurationWatched = 0;

        for (Lesson lesson : lessons) {
            var progress = progressRepository.findByStudentIdAndLessonId(userPrincipal.getId(), lesson.getId());
            if (progress.isPresent() && progress.get().getIsCompleted()) {
                completedLessons++;
                totalDurationWatched += progress.get().getWatchedDurationMinutes();
            }
        }

        double courseProgress = (completedLessons * 100.0) / lessons.size();

        Map<String, Object> response = new HashMap<>();
        response.put("courseId", courseId);
        response.put("totalLessons", lessons.size());
        response.put("completedLessons", completedLessons);
        response.put("progressPercentage", courseProgress);
        response.put("totalDurationWatched", totalDurationWatched);

        return response;
    }

    private LessonProgressDTO mapToDTO(LessonProgress progress) {
        LessonProgressDTO dto = modelMapper.map(progress, LessonProgressDTO.class);
        dto.setStudentId(progress.getStudent().getId());
        dto.setStudentName(progress.getStudent().getFirstName() + " " + progress.getStudent().getLastName());
        dto.setLessonId(progress.getLesson().getId());
        dto.setLessonTitle(progress.getLesson().getTitle());
        return dto;
    }
}
