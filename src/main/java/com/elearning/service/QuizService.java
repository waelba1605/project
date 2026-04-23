package com.elearning.service;

import com.elearning.dto.QuizDTO;
import com.elearning.exception.ResourceNotFoundException;
import com.elearning.exception.UnauthorizedException;
import com.elearning.model.entity.Lesson;
import com.elearning.model.entity.Quiz;
import com.elearning.repository.LessonRepository;
import com.elearning.repository.QuizRepository;
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
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModelMapper modelMapper;

    public QuizDTO createQuiz(QuizDTO quizDTO) {
        Lesson lesson = lessonRepository.findById(quizDTO.getLessonId())
            .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", quizDTO.getLessonId()));

        verifyInstructor(lesson.getCourse().getInstructor().getId());

        Quiz quiz = modelMapper.map(quizDTO, Quiz.class);
        quiz.setLesson(lesson);
        quiz.setIsPublished(false);

        Quiz savedQuiz = quizRepository.save(quiz);
        return modelMapper.map(savedQuiz, QuizDTO.class);
    }

    public QuizDTO getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        return modelMapper.map(quiz, QuizDTO.class);
    }

    public List<QuizDTO> getQuizzesByLesson(Long lessonId) {
        return quizRepository.findByLessonId(lessonId).stream()
            .map(quiz -> modelMapper.map(quiz, QuizDTO.class))
            .collect(Collectors.toList());
    }

    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        verifyInstructor(quiz.getLesson().getCourse().getInstructor().getId());

        if (quizDTO.getTitle() != null) quiz.setTitle(quizDTO.getTitle());
        if (quizDTO.getDescription() != null) quiz.setDescription(quizDTO.getDescription());
        if (quizDTO.getPassingScore() != null) quiz.setPassingScore(quizDTO.getPassingScore());
        if (quizDTO.getTimeLimitMinutes() != null) quiz.setTimeLimitMinutes(quizDTO.getTimeLimitMinutes());

        Quiz updatedQuiz = quizRepository.save(quiz);
        return modelMapper.map(updatedQuiz, QuizDTO.class);
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        verifyInstructor(quiz.getLesson().getCourse().getInstructor().getId());
        quizRepository.delete(quiz);
    }

    public QuizDTO publishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        verifyInstructor(quiz.getLesson().getCourse().getInstructor().getId());
        quiz.setIsPublished(true);
        Quiz publishedQuiz = quizRepository.save(quiz);
        return modelMapper.map(publishedQuiz, QuizDTO.class);
    }

    private void verifyInstructor(Long instructorId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userPrincipal.getId().equals(instructorId) && !userPrincipal.getAuthorities().toString().contains("ADMIN")) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }
    }
}
