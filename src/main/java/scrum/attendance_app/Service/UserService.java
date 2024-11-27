package scrum.attendance_app.Service;

import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Registration;
import scrum.attendance_app.data.entities.Student;
import scrum.attendance_app.repository.CourseRepository;

import scrum.attendance_app.repository.ProfessorRepository;
import scrum.attendance_app.repository.RegistrationRepository;
import scrum.attendance_app.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;

    @Autowired
    public UserService(CourseRepository courseRepository, StudentRepository studentRepository, RegistrationRepository registrationRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
    }

    // Sign up the course with student registration number and course code
    public String signUpCourse(String registrationNumber, String courseCode) {
        // Try to get the student with a certain registration number and course with a certain course code
        Student student = studentRepository.findByRegistrationNumber(registrationNumber);
        Course course = courseRepository.findCourseByCode(courseCode);

        // If the course code is not associated with a course there aren't course
        if(course == null){
            return "Course not found";
        }

        // If already exists a registration with this student id and course id
        if(registrationRepository.findByStudentIdAndCourseId(student.getId(), course.getId()).isPresent()) {
            return "Already registered";
        }

        // Try to create a registration
        try {
            Registration registration = Registration.builder()
                        .registrationDate(LocalDate.now())
                        .student(student)
                        .course(course).build();

            registrationRepository.save(registration);
            return "Registered";
        }
        catch (PersistenceException e) {
            return "Unable to sign up the course: " + e.getMessage();
        }

    }

    public List<Course> retrieveCourses(String user) {

    }

}
