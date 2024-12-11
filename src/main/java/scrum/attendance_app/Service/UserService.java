package scrum.attendance_app.Service;

import com.nimbusds.jose.JOSEException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Registration;
import scrum.attendance_app.data.entities.Student;
import scrum.attendance_app.repository.CourseRepository;

import scrum.attendance_app.repository.RegistrationRepository;
import scrum.attendance_app.repository.StudentRepository;
import scrum.attendance_app.security.TokenStore;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationService registrationService;

    @Autowired
    public UserService(CourseRepository courseRepository, StudentRepository studentRepository, RegistrationRepository registrationRepository, RegistrationService registrationService) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
        this.registrationService = registrationService;
    }

    // Sign up the course with student registration number and course code
    public String signUpCourse(String user, String courseCode) {
        // Try to get the student with a certain registration number and course with a certain course code
        Student student = null;
        if(studentRepository.findByEmail(user).isPresent()) {
            student = studentRepository.findByEmail(user).get();
        }

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

    public List<CourseDTO> retrieveCourses(String user) {
        Student student = null;
        if(studentRepository.findByEmail(user).isPresent()) {
            student = studentRepository.findByEmail(user).get();
        }

        List<Registration> registrations = registrationRepository.findByStudentId(student.getId());
        List<Course> courseList = new ArrayList<>();
        for (Registration registration : registrations) {
            Course course = registration.getCourse();
            courseList.add(course);
        }

        List<CourseDTO> courseDTOList = new ArrayList<>();
        for (Course course : courseList) {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setId(course.getId());
            courseDTO.setName(course.getName());
            courseDTO.setDescription(course.getDescription());
            courseDTO.setCode(course.getCode());
            courseDTOList.add(courseDTO);
        }

        return courseDTOList;
    }

    public static String readToken(String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.replace("Bearer ", "");
        TokenStore.getInstance().verifyToken(token);
        String user = TokenStore.getInstance().getUser(token);
        return user;
    }
}
