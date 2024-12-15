package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import scrum.attendance_app.data.entities.Attendance;

import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findAllByLessonId(UUID lessonId);

    @Query("select attendance from Attendance attendance where attendance.lesson.course.id=:idofthecourse")
    List<Attendance> findAllByCourse(@Param("idofthecourse") UUID courseId);
}
