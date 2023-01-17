package com.user.app.entity;

import com.user.app.dto.QuestionExerciseDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @ManyToOne
    @JoinColumn(name = "question_exercise_id")
    private QuestionExercise questionExercise;

    @Column(columnDefinition = "LONGTEXT", length = 999999999)
    private String question;

    @Column(columnDefinition = "LONGTEXT", length = 999999999)
    private String answer;

    private Date createdAt;

    private Date updatedAt;

    private String timeTaken;

    private String totalQuestions;
    private String totalSolvedQuestions;
    private String totalUnsolvedQuestions;
    private double minimumMarks;
    private boolean isNegative;
    private int eachQuestionContains;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Exam exam = (Exam) o;
        return id != null && Objects.equals(id, exam.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
