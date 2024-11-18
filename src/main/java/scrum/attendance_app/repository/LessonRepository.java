package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scrum.attendance_app.data.entities.Lesson;

import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    Optional<Lesson> findByCourseIdAndEndDateNull(UUID courseID);


}
