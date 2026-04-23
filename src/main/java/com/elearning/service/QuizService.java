package com.elearning.service;

import com.elearning.dto.QuizDTO;
import com.elearning.dto.QuizRequest;
import com.elearning.model.entity.Quiz;
import com.elearning.model.entity.Lesson;
import com.elearning.repository.QuizRepository;
import com.elearning.repository.LessonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModelMapper modelMapper;

    public QuizDTO createQuiz(QuizRequest quizRequest) {
        Lesson lesson = lessonRepository.findById(quizRequest.getLessonId())
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Quiz quiz = new Quiz();
        quiz.setTitle(quizRequest.getTitle());
        quiz.setDescription(quizRequest.getDescription());
        quiz.setLesson(lesson);
        quiz.setPassingScore(quizRequest.getPassingScore());
        quiz.setTimeLimitMinutes(quizRequest.getTimeLimitMinutes());
        quiz.setShowCorrectAnswers(quizRequest.getShowCorrectAnswers());
        quiz.setIsPublished(false);

        Quiz savedQuiz = quizRepository.save(quiz);
        return mapToDTO(savedQuiz);
    }

    public QuizDTO getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return mapToDTO(quiz);
    }

    public List<QuizDTO> getQuizzesByLesson(Long lessonId) {
        List<Quiz> quizzes = quizRepository.findByLessonId(lessonId);
        return quizzes.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public QuizDTO updateQuiz(Long id, QuizRequest quizRequest) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setTitle(quizRequest.getTitle());
        quiz.setDescription(quizRequest.getDescription());
        quiz.setPassingScore(quizRequest.getPassingScore());
        quiz.setTimeLimitMinutes(quizRequest.getTimeLimitMinutes());
        quiz.setShowCorrectAnswers(quizRequest.getShowCorrectAnswers());
        quiz.setUpdatedAt(LocalDateTime.now());

        Quiz updatedQuiz = quizRepository.save(quiz);
        return mapToDTO(updatedQuiz);
    }

    public QuizDTO publishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setIsPublished(true);
        quiz.setUpdatedAt(LocalDateTime.now());

        Quiz publishedQuiz = quizRepository.save(quiz);
        return mapToDTO(publishedQuiz);
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    private QuizDTO mapToDTO(Quiz quiz) {
        QuizDTO dto = modelMapper.map(quiz, QuizDTO.class);
        dto.setLessonId(quiz.getLesson().getId());
        dto.setLessonTitle(quiz.getLesson().getTitle());
        return dto;
    }
}
