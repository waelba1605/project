package com.elearning.service;

import com.elearning.dto.QuizResultDTO;
import com.elearning.model.entity.Quiz;
import com.elearning.model.entity.QuizResult;
import com.elearning.model.entity.User;
import com.elearning.repository.QuizRepository;
import com.elearning.repository.QuizResultRepository;
import com.elearning.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizResultService {

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public QuizResultDTO submitQuizResult(QuizResultDTO quizResultDTO, Long studentId) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Quiz quiz = quizRepository.findById(quizResultDTO.getQuizId())
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        int attemptNumber = (int) quizResultRepository
            .findByStudentIdAndQuizId(studentId, quizResultDTO.getQuizId()).size() + 1;

        double percentageScore = (double) quizResultDTO.getScore() / quizResultDTO.getTotalPoints() * 100;

        QuizResult result = QuizResult.builder()
            .student(student)
            .quiz(quiz)
            .score(quizResultDTO.getScore())
            .totalPoints(quizResultDTO.getTotalPoints())
            .percentageScore(percentageScore)
            .isPassed(percentageScore >= quiz.getPassingScore())
            .attemptNumber(attemptNumber)
            .completedAt(LocalDateTime.now())
            .build();

        QuizResult savedResult = quizResultRepository.save(result);
        return mapToDTO(savedResult);
    }

    public QuizResultDTO getQuizResultById(Long id) {
        QuizResult result = quizResultRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz result not found"));
        return mapToDTO(result);
    }

    public List<QuizResultDTO> getStudentQuizResults(Long studentId) {
        userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        return quizResultRepository.findAll().stream()
            .filter(qr -> qr.getStudent().getId().equals(studentId))
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<QuizResultDTO> getQuizResults(Long quizId) {
        quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return quizResultRepository.findAll().stream()
            .filter(qr -> qr.getQuiz().getId().equals(quizId))
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public QuizResultDTO getLatestQuizResult(Long studentId, Long quizId) {
        QuizResult result = quizResultRepository.findTopByStudentIdAndQuizIdOrderByCreatedAtDesc(studentId, quizId)
            .orElseThrow(() -> new RuntimeException("No quiz result found"));
        return mapToDTO(result);
    }

    public List<QuizResultDTO> getAllQuizResults() {
        return quizResultRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private QuizResultDTO mapToDTO(QuizResult result) {
        QuizResultDTO dto = modelMapper.map(result, QuizResultDTO.class);
        dto.setStudentName(result.getStudent().getFirstName() + " " + result.getStudent().getLastName());
        dto.setStudentEmail(result.getStudent().getEmail());
        dto.setQuizName(result.getQuiz().getTitle());
        return dto;
    }
}
