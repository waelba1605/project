package com.elearning.repository;

import com.elearning.model.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<QuizResult> findByQuizIdOrderByCreatedAtDesc(Long quizId);
    QuizResult findTopByStudentIdAndQuizIdOrderByCreatedAtDesc(Long studentId, Long quizId);
}
