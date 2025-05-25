package com.studyplatform.repository;

import com.studyplatform.model.Material;
import com.studyplatform.model.User;
import com.studyplatform.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByLesson(Lesson lesson);
    List<Material> findByCreatedBy(User user);
    List<Material> findByLessonAndCreatedBy(Lesson lesson, User user);
}