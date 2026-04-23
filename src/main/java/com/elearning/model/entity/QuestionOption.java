package com.elearning.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String optionText;

    @Column(name = "option_letter")
    private String optionLetter; // A, B, C, D

    @Column(name = "is_correct")
    private Boolean isCorrect = false;

    @Column(name = "option_number")
    private Integer optionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}