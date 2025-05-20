package com.studyplatform.repository;

import com.studyplatform.model.Course;
import com.studyplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCreatedBy(User user);
    List<Course> findByTitleContainingIgnoreCase(String title);
}