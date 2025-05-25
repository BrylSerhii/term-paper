package com.studyplatform.repository;

import com.studyplatform.model.Assignment;
import com.studyplatform.model.User;
import com.studyplatform.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByLesson(Lesson lesson);
    List<Assignment> findByCreatedBy(User user);
    List<Assignment> findByLessonAndCreatedBy(Lesson lesson, User user);
    List<Assignment> findByDueDateBefore(LocalDateTime date);
    List<Assignment> findByDueDateAfter(LocalDateTime date);
}