package com.studyplatform.repository;

import com.studyplatform.model.Enrollment;
import com.studyplatform.model.Lesson;
import com.studyplatform.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByEnrollment(Enrollment enrollment);
    Optional<Progress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
    boolean existsByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
    
    @Query("SELECT COUNT(p) FROM Progress p WHERE p.enrollment = :enrollment")
    long countByEnrollment(Enrollment enrollment);
    
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course = :#{#enrollment.course}")
    long countLessonsByCourse(Enrollment enrollment);
}