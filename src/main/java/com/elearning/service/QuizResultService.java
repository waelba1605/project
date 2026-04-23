package com.elearning.service;

import com.elearning.dto.QuizResultDTO;
import com.elearning.exception.ResourceNotFoundException;
import com.elearning.model.entity.Quiz;
import com.elearning.model.entity.QuizResult;
import com.elearning.model.entity.User;
import com.elearning.repository.QuizRepository;
import com.elearning.repository.QuizResultRepository;
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
public class QuizResultService {

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public QuizResultDTO submitQuizResult(QuizResultDTO resultDTO) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User student = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        Quiz quiz = quizRepository.findById(resultDTO.getQuizId())
            .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", resultDTO.getQuizId()));

        // Calculate attempt number
        List<QuizResult> previousAttempts = quizResultRepository.findByStudentIdAndQuizIdOrderByCreatedAtDesc(
            student.getId(), resultDTO.getQuizId());
        int attemptNumber = previousAttempts.isEmpty() ? 1 : previousAttempts.get(0).getAttemptNumber() + 1;

        // Calculate total points
        int totalPoints = quiz.getQuestions().stream()
            .mapToInt(q -> q.getPoints() != null ? q.getPoints() : 1)
            .sum();

        // Calculate percentage
        double percentageScore = (resultDTO.getScore() * 100.0) / totalPoints;

        // Check if passed
        boolean isPassed = percentageScore >= quiz.getPassingScore();

        QuizResult quizResult = QuizResult.builder()
            .student(student)
            .quiz(quiz)
            .score(resultDTO.getScore())
            .totalPoints(totalPoints)
            .percentageScore(percentageScore)
            .isPassed(isPassed)
            .attemptNumber(attemptNumber)
            .completedAt(LocalDateTime.now())
            .build();

        QuizResult saved = quizResultRepository.save(quizResult);
        return mapToQuizResultDTO(saved);
    }

    public QuizResultDTO getResultById(Long id) {
        QuizResult result = quizResultRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("QuizResult", "id", id));
        return mapToQuizResultDTO(result);
    }

    public List<QuizResultDTO> getStudentResults() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return quizResultRepository.findByStudentIdOrderByCreatedAtDesc(userPrincipal.getId()).stream()
            .map(this::mapToQuizResultDTO)
            .collect(Collectors.toList());
    }

    public List<QuizResultDTO> getQuizResults(Long quizId) {
        return quizResultRepository.findByQuizIdOrderByCreatedAtDesc(quizId).stream()
            .map(this::mapToQuizResultDTO)
            .collect(Collectors.toList());
    }

    public QuizResultDTO getLatestResult(Long quizId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuizResult result = quizResultRepository.findTopByStudentIdAndQuizIdOrderByCreatedAtDesc(
            userPrincipal.getId(), quizId);
        
        if (result == null) {
            throw new ResourceNotFoundException("QuizResult", "quizId", quizId);
        }
        
        return mapToQuizResultDTO(result);
    }

    private QuizResultDTO mapToQuizResultDTO(QuizResult result) {
        QuizResultDTO dto = modelMapper.map(result, QuizResultDTO.class);
        dto.setStudentId(result.getStudent().getId());
        dto.setStudentName(result.getStudent().getFirstName() + " " + result.getStudent().getLastName());
        dto.setQuizId(result.getQuiz().getId());
        dto.setQuizTitle(result.getQuiz().getTitle());
        return dto;
    }
}
