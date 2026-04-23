package com.elearning.service;

import com.elearning.dto.QuizResultDTO;
import com.elearning.dto.QuizResultRequest;
import com.elearning.model.entity.QuizResult;
import com.elearning.model.entity.Quiz;
import com.elearning.model.entity.User;
import com.elearning.repository.QuizResultRepository;
import com.elearning.repository.QuizRepository;
import com.elearning.repository.UserRepository;
import com.elearning.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizResultService {

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }

    public QuizResultDTO submitQuiz(QuizResultRequest quizResultRequest) {
        UserPrincipal userPrincipal = getCurrentUser();
        User student = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Quiz quiz = quizRepository.findById(quizResultRequest.getQuizId())
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Calculate percentage score
        Double percentageScore = (quizResultRequest.getScore() * 100.0) / quizResultRequest.getTotalPoints();
        Boolean isPassed = percentageScore >= quiz.getPassingScore();

        // Get attempt number
        List<QuizResult> previousAttempts = quizResultRepository.findByStudentIdAndQuizId(student.getId(), quiz.getId());
        Integer attemptNumber = previousAttempts.size() + 1;

        QuizResult result = new QuizResult();
        result.setStudent(student);
        result.setQuiz(quiz);
        result.setScore(quizResultRequest.getScore());
        result.setTotalPoints(quizResultRequest.getTotalPoints());
        result.setPercentageScore(percentageScore);
        result.setIsPassed(isPassed);
        result.setAttemptNumber(attemptNumber);
        result.setCompletedAt(LocalDateTime.now());

        QuizResult savedResult = quizResultRepository.save(result);
        return mapToDTO(savedResult);
    }

    public QuizResultDTO getResultById(Long id) {
        QuizResult result = quizResultRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz result not found"));
        return mapToDTO(result);
    }

    public List<QuizResultDTO> getMyQuizResults(Long quizId) {
        UserPrincipal userPrincipal = getCurrentUser();
        List<QuizResult> results = quizResultRepository.findByStudentIdAndQuizId(userPrincipal.getId(), quizId);
        return results.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public QuizResultDTO getLatestQuizResult(Long quizId) {
        UserPrincipal userPrincipal = getCurrentUser();
        QuizResult result = quizResultRepository.findTopByStudentIdAndQuizIdOrderByCreatedAtDesc(userPrincipal.getId(), quizId)
            .orElseThrow(() -> new RuntimeException("No quiz results found"));
        return mapToDTO(result);
    }

    private QuizResultDTO mapToDTO(QuizResult result) {
        QuizResultDTO dto = modelMapper.map(result, QuizResultDTO.class);
        dto.setStudentId(result.getStudent().getId());
        dto.setStudentName(result.getStudent().getFirstName() + " " + result.getStudent().getLastName());
        dto.setQuizId(result.getQuiz().getId());
        dto.setQuizTitle(result.getQuiz().getTitle());
        return dto;
    }
}
