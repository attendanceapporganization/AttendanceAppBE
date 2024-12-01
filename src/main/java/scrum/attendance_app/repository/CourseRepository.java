package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Professor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface CourseRepository extends JpaRepository<Course, UUID> {

    // To retrieve the first course with the given name
    Optional<Course> findByName(String name);

    Optional<Course> findById(UUID id);

    // To retrieve a list of all course that have the same name
    @Query("SELECT c FROM Course c WHERE LOWER(c.name) = LOWER(:name)")
    List<Course> findByCourseNameLowercase(@Param("name") String name);

    @Query("SELECT c FROM Course c WHERE c.code = :courseCode")
    boolean existsByCourseCode(@Param("courseCode") String courseCode);

    @Query("SELECT c FROM Course c WHERE c.code = :courseCode")
    Course findCourseByCode(@Param("courseCode") String courseCode);

    @Query("SELECT c FROM Course c WHERE c.professor = :professorId")
    List<Course> findByProfessorId(@Param("professorId") Professor professorId);

}
