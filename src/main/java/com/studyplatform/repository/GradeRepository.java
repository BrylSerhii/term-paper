package com.studyplatform.repository;

import com.studyplatform.model.Assignment;
import com.studyplatform.model.Grade;
import com.studyplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByAssignment(Assignment assignment);
    List<Grade> findByStudent(User student);
    List<Grade> findByGradedBy(User teacher);
    Optional<Grade> findByAssignmentAndStudent(Assignment assignment, User student);
    List<Grade> findByStudentOrderBySubmittedAtDesc(User student);
    List<Grade> findByAssignmentAndPointsGreaterThanEqual(Assignment assignment, Integer points);
}