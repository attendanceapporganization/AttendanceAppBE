package scrum.attendance_app.Service;

import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Lesson;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.error_handling.exceptions.DataNotFoundException;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.LessonRepository;
import scrum.attendance_app.repository.ProfessorRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProfessorService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    LessonRepository lessonRepository;

    // This method find the first course that have as name String name and as professor String professorEmail
    private Course alreadyHasThisCourse(String name, String professorEmail){
        if(!courseRepository.findByCourseNameLowercase(name).isEmpty()) {
            List<Course> coursesWithSameName = courseRepository.findByCourseNameLowercase(name);
            for (Course course : coursesWithSameName) {
                if (course.getProfessor().getEmail().equals(professorEmail)) { return course; }
            }
        }

        return null;
    }

    // Convert the given CourseDTO in Course
    private Course converteToCourse(CourseDTO courseDTO){
        String email = courseDTO.getProfessorOwner();
        Professor professor = (professorRepository.findByEmail(email)).get();

        return Course.builder()
                .name(courseDTO.getName())
                .description(courseDTO.getDescription())
                .professor(professor)
                .build();
    }

    // Create the course with given courseDTO
    public String createCourse(CourseDTO courseDTO) {
        if (alreadyHasThisCourse(courseDTO.getName(), courseDTO.getProfessorOwner()) != null) {
            return "Course already exists";
        }
        if (professorRepository.findByEmail(courseDTO.getProfessorOwner()).isEmpty()) {
            return "Professor does not exist";
        }
        try {
            courseRepository.save(converteToCourse(courseDTO));
            return "Created";
        }
        catch (PersistenceException e) {
            return "Course creation failed due to persistence error: " + e.getMessage();
        } catch (Exception e) {
            return "Course creation failed due to unexpected error: " + e.getMessage();
        }
    }

    // Delete the course with given course name and professor email
    public String deleteCourse(String name, String professorEmail) {
        if(alreadyHasThisCourse(name, professorEmail) == null){
            return "Course not found";
        }
        if (professorRepository.findByEmail(professorEmail).isEmpty()) {
            return "Professor does not exist";
        }
        try {
            courseRepository.delete(Objects.requireNonNull(alreadyHasThisCourse(name, professorEmail)));
            return "Deleted";
        }
        catch (PersistenceException e) {
            return "Operation failed due to persistence error: " + e.getMessage();
        }
    }

    public Course retrieveCourse(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(()->new DataNotFoundException("Course not found"));
    }

    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Lesson retrieveLesson(UUID lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(()->new DataNotFoundException("lesson not found"));
    }

    public void terminateLesson(Lesson lesson) {
        lesson.setEndDate(Date.from(Instant.now()));
        lessonRepository.save(lesson);
    }
}
