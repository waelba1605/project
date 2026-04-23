package com.elearning.controller;

import com.elearning.dto.QuizResultDTO;
import com.elearning.service.QuizResultService;
import com.elearning.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitQuizResult(@RequestBody QuizResultDTO quizResultDTO, Authentication authentication) {
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            QuizResultDTO result = quizResultService.submitQuizResult(quizResultDTO, principal.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizResult(@PathVariable Long id) {
        try {
            QuizResultDTO result = quizResultService.getQuizResultById(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<QuizResultDTO>> getAllQuizResults() {
        List<QuizResultDTO> results = quizResultService.getAllQuizResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getStudentQuizResults(@PathVariable Long studentId) {
        try {
            List<QuizResultDTO> results = quizResultService.getStudentQuizResults(studentId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getQuizResults(@PathVariable Long quizId) {
        try {
            List<QuizResultDTO> results = quizResultService.getQuizResults(quizId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/student/{studentId}/quiz/{quizId}/latest")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getLatestQuizResult(@PathVariable Long studentId, @PathVariable Long quizId) {
        try {
            QuizResultDTO result = quizResultService.getLatestQuizResult(studentId, quizId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
