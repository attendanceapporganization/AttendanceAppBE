package scrum.attendance_app.Service;

import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.error_handling.exceptions.ExistingCourseNameException;
import scrum.attendance_app.error_handling.exceptions.ExistingEmailException;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.ProfessorRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ProfessorRepository professorRepository;

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
    public String createCourse(CourseDTO courseDTO, String user) {

        Professor professor = professorRepository.findByEmail(user).get();
        courseDTO.setProfessorOwner(professor.getEmail());
        courseDTO.setIdProfessor(professor.getId());
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


    public List getListCourse(String user) {

        Professor professor = professorRepository.findByEmail(user).get();


        var corsi = courseRepository.findByProfessorId(professor);

        return corsi;

    }
}
