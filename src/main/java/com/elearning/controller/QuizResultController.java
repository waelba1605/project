package com.elearning.controller;

import com.elearning.dto.QuizResultDTO;
import com.elearning.service.QuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz-results")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizResultController {

    @Autowired
    private QuizResultService quizResultService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QuizResultDTO> submitQuiz(@RequestBody QuizResultDTO resultDTO) {
        QuizResultDTO result = quizResultService.submitQuizResult(resultDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/my-results")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<QuizResultDTO>> getMyResults() {
        List<QuizResultDTO> results = quizResultService.getStudentResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<QuizResultDTO>> getQuizResults(@PathVariable Long quizId) {
        List<QuizResultDTO> results = quizResultService.getQuizResults(quizId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResultDTO> getResultById(@PathVariable Long id) {
        QuizResultDTO result = quizResultService.getResultById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/latest/{quizId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QuizResultDTO> getLatestResult(@PathVariable Long quizId) {
        QuizResultDTO result = quizResultService.getLatestResult(quizId);
        return ResponseEntity.ok(result);
    }
}
