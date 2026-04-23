package com.elearning.service;

import com.elearning.dto.QuestionDTO;
import com.elearning.model.entity.Question;
import com.elearning.model.entity.Quiz;
import com.elearning.repository.QuestionRepository;
import com.elearning.repository.QuizRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Quiz quiz = quizRepository.findById(questionDTO.getQuizId())
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Question question = modelMapper.map(questionDTO, Question.class);
        question.setQuiz(quiz);

        if (question.getQuestionNumber() == null) {
            int maxQuestionNumber = quiz.getQuestions().stream()
                .mapToInt(q -> q.getQuestionNumber() != null ? q.getQuestionNumber() : 0)
                .max()
                .orElse(0);
            question.setQuestionNumber(maxQuestionNumber + 1);
        }

        Question savedQuestion = questionRepository.save(question);
        return mapToDTO(savedQuestion);
    }

    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question not found"));
        return mapToDTO(question);
    }

    public List<QuestionDTO> getQuestionsByQuiz(Long quizId) {
        quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return questionRepository.findByQuizId(quizId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question not found"));

        if (questionDTO.getQuestionText() != null) question.setQuestionText(questionDTO.getQuestionText());
        if (questionDTO.getQuestionType() != null) question.setQuestionType(questionDTO.getQuestionType());
        if (questionDTO.getPoints() != null) question.setPoints(questionDTO.getPoints());

        Question updatedQuestion = questionRepository.save(question);
        return mapToDTO(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question not found"));
        questionRepository.delete(question);
    }

    private QuestionDTO mapToDTO(Question question) {
        QuestionDTO dto = modelMapper.map(question, QuestionDTO.class);
        return dto;
    }
}
