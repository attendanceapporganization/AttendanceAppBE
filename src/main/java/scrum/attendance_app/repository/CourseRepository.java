package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import scrum.attendance_app.data.entities.Course;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    // To retrieve the first course with the given name
    Course findByName(String name);

    // To retrieve a list of all course that have the same name
    @Query("SELECT c FROM Course c WHERE LOWER(c.name) = LOWER(:name)")
    List<Course> findByCourseNameLowercase(@Param("name") String name);
}
