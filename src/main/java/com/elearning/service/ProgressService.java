package com.elearning.service;

import com.elearning.dto.ProgressDTO;
import com.elearning.exception.ResourceNotFoundException;
import com.elearning.exception.UnauthorizedException;
import com.elearning.model.entity.Enrollment;
import com.elearning.model.entity.Lesson;
import com.elearning.model.entity.LessonProgress;
import com.elearning.model.entity.User;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.LessonProgressRepository;
import com.elearning.repository.LessonRepository;
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
public class ProgressService {

    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProgressDTO updateLessonProgress(ProgressDTO progressDTO) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User student = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        Lesson lesson = lessonRepository.findById(progressDTO.getLessonId())
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", progressDTO.getLessonId()));

        LessonProgress progress = lessonProgressRepository.findByStudentIdAndLessonId(student.getId(), lesson.getId())
            .orElse(LessonProgress.builder()
                .student(student)
                .lesson(lesson)
                .progressPercentage(0)
                .watchedDurationMinutes(0)
                .isCompleted(false)
                .build());

        if (progressDTO.getWatchedDurationMinutes() != null) {
            progress.setWatchedDurationMinutes(progressDTO.getWatchedDurationMinutes());
        }

        if (progressDTO.getProgressPercentage() != null) {
            progress.setProgressPercentage(progressDTO.getProgressPercentage());
        }

        if (progressDTO.getProgressPercentage() >= 100) {
            progress.setIsCompleted(true);
            progress.setCompletedAt(LocalDateTime.now());
        }

        LessonProgress saved = lessonProgressRepository.save(progress);
        return mapToProgressDTO(saved);
    }

    public ProgressDTO getLessonProgress(Long lessonId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", lessonId));

        LessonProgress progress = lessonProgressRepository.findByStudentIdAndLessonId(userPrincipal.getId(), lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Progress", "lesson", lessonId));

        return mapToProgressDTO(progress);
    }

    public List<ProgressDTO> getCourseProgress(Long courseId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(userPrincipal.getId(), courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "course", courseId));

        return enrollment.getCourse().getLessons().stream()
            .map(lesson -> lessonProgressRepository.findByStudentIdAndLessonId(userPrincipal.getId(), lesson.getId())
                .map(this::mapToProgressDTO)
                .orElse(null))
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
    }

    public Integer calculateEnrollmentProgress(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!enrollment.getStudent().getId().equals(userPrincipal.getId())) {
            throw new UnauthorizedException("You are not authorized to view this progress");
        }

        int totalLessons = enrollment.getCourse().getLessons().size();
        if (totalLessons == 0) return 0;

        long completedLessons = enrollment.getCourse().getLessons().stream()
            .filter(lesson -> lessonProgressRepository.findByStudentIdAndLessonId(enrollment.getStudent().getId(), lesson.getId())
                .map(LessonProgress::getIsCompleted)
                .orElse(false))
            .count();

        int progress = (int) ((completedLessons * 100) / totalLessons);
        enrollment.setProgressPercentage(progress);
        enrollmentRepository.save(enrollment);

        return progress;
    }

    private ProgressDTO mapToProgressDTO(LessonProgress progress) {
        ProgressDTO dto = modelMapper.map(progress, ProgressDTO.class);
        dto.setStudentId(progress.getStudent().getId());
        dto.setLessonTitle(progress.getLesson().getTitle());
        dto.setTotalDurationMinutes(progress.getLesson().getDurationMinutes());
        return dto;
    }
}
