package com.elearning.service;

import com.elearning.dto.QuizDTO;
import com.elearning.model.entity.Lesson;
import com.elearning.model.entity.Quiz;
import com.elearning.repository.LessonRepository;
import com.elearning.repository.QuizRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        Lesson lesson = lessonRepository.findById(quizDTO.getLessonId())
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Quiz quiz = modelMapper.map(quizDTO, Quiz.class);
        quiz.setLesson(lesson);
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
        lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

        return quizRepository.findByLessonId(lessonId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        if (quizDTO.getTitle() != null) quiz.setTitle(quizDTO.getTitle());
        if (quizDTO.getDescription() != null) quiz.setDescription(quizDTO.getDescription());
        if (quizDTO.getPassingScore() != null) quiz.setPassingScore(quizDTO.getPassingScore());
        if (quizDTO.getTimeLimitMinutes() != null) quiz.setTimeLimitMinutes(quizDTO.getTimeLimitMinutes());
        if (quizDTO.getShowCorrectAnswers() != null) quiz.setShowCorrectAnswers(quizDTO.getShowCorrectAnswers());

        Quiz updatedQuiz = quizRepository.save(quiz);
        return mapToDTO(updatedQuiz);
    }

    @Transactional
    public void publishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        if (quiz.getQuestions().isEmpty()) {
            throw new RuntimeException("Quiz must have at least one question to be published");
        }

        quiz.setIsPublished(true);
        quizRepository.save(quiz);
    }

    @Transactional
    public void unpublishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.setIsPublished(false);
        quizRepository.save(quiz);
    }

    @Transactional
    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    private QuizDTO mapToDTO(Quiz quiz) {
        QuizDTO dto = modelMapper.map(quiz, QuizDTO.class);
        dto.setLessonName(quiz.getLesson().getTitle());
        dto.setQuestionCount(quiz.getQuestions().size());
        return dto;
    }
}
