package com.elearning.repository;

import com.elearning.model.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByStudentIdAndQuizId(Long studentId, Long quizId);
    Optional<QuizResult> findTopByStudentIdAndQuizIdOrderByCreatedAtDesc(Long studentId, Long quizId);
}
