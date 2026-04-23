package com.elearning.controller;

import com.elearning.dto.QuizDTO;
import com.elearning.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<QuizDTO> createQuiz(@Valid @RequestBody QuizDTO quizDTO) {
        QuizDTO createdQuiz = quizService.createQuiz(quizDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long id) {
        QuizDTO quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<QuizDTO>> getQuizzesByLesson(@PathVariable Long lessonId) {
        List<QuizDTO> quizzes = quizService.getQuizzesByLesson(lessonId);
        return ResponseEntity.ok(quizzes);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<QuizDTO> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody QuizDTO quizDTO) {
        QuizDTO updatedQuiz = quizService.updateQuiz(id, quizDTO);
        return ResponseEntity.ok(updatedQuiz);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<QuizDTO> publishQuiz(@PathVariable Long id) {
        QuizDTO quiz = quizService.publishQuiz(id);
        return ResponseEntity.ok(quiz);
    }
}
