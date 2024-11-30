package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Lesson;

import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    Optional<Lesson> findByCourseIdAndEndDateNull(UUID courseID);

    @Query("select lesson FROM Lesson lesson WHERE lesson.startDate = (select MAX(l2.startDate) from Lesson l2 where l2.course=:course)")
    Optional<Lesson> findLessonWithMaxStartDateByCourse(@Param("course") Course course);


}
