package com.elearning.controller;

import com.elearning.dto.QuizResultDTO;
import com.elearning.dto.QuizResultRequest;
import com.elearning.service.QuizResultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quiz-results")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizResultController {

    @Autowired
    private QuizResultService quizResultService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitQuiz(@Valid @RequestBody QuizResultRequest quizResultRequest) {
        try {
            QuizResultDTO resultDTO = quizResultService.submitQuiz(quizResultRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResultById(@PathVariable Long id) {
        try {
            QuizResultDTO resultDTO = quizResultService.getResultById(id);
            return ResponseEntity.ok(resultDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/quiz/{quizId}/my-results")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getMyQuizResults(@PathVariable Long quizId) {
        try {
            List<QuizResultDTO> results = quizResultService.getMyQuizResults(quizId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/quiz/{quizId}/latest")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getLatestQuizResult(@PathVariable Long quizId) {
        try {
            QuizResultDTO resultDTO = quizResultService.getLatestQuizResult(quizId);
            return ResponseEntity.ok(resultDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
