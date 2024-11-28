package scrum.attendance_app.Service;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import scrum.attendance_app.data.entities.*;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.RegistrationRepository;
import scrum.attendance_app.repository.StudentRepository;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private UserService userService;

    private Course course;
    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Registry studentRegistry = Registry.builder()
                .id(UUID.fromString("240e0164-a49b-4250-a393-7108c757b251"))
                .firstname("name_student1")
                .lastname("surname_student1")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        student = Student.builder()
                .id(UUID.fromString("7ba12d04-d996-4556-83b0-668083c853f4"))
                .email("student1@student.it")
                .password("password1")
                .registry(studentRegistry)
                .build();

        Registry professorRegistry = Registry.builder()
                .id(UUID.fromString("24713058-d7e5-4ce8-be55-4f67cbf09b44"))
                .firstname("name_prof1")
                .lastname("surname_prof1")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        Professor professor = Professor.builder()
                .id(UUID.fromString("2ff9292e-9c6c-44c4-949b-8e8a78ea5779"))
                .email("professor1@professor.it")
                .password("password1")
                .registry(professorRegistry)
                .build();

        course = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

    }

    @Test
    void signUpCourse_SuccessfulRegistration() {
        String registrationNumber = "123456";
        String courseCode = "C001";

        when(studentRepository.findByRegistrationNumber(registrationNumber)).thenReturn(student);
        when(courseRepository.findCourseByCode(courseCode)).thenReturn(course);
        when(registrationRepository.findByStudentIdAndCourseId(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

        String result = userService.signUpCourse(registrationNumber, courseCode);

        assertEquals("Registered", result);
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    void signUpCourse_CourseNotFound() {
        String registrationNumber = "123456";
        String courseCode = "C001";

        when(studentRepository.findByRegistrationNumber(registrationNumber)).thenReturn(student);
        when(courseRepository.findCourseByCode(courseCode)).thenReturn(null);

        String result = userService.signUpCourse(registrationNumber, courseCode);

        assertEquals("Course not found", result);
    }

    @Test
    void signUpCourse_AlreadyRegistered() {
        String registrationNumber = "123456";
        String courseCode = "C001";

        Registration existingRegistration = new Registration();
        when(studentRepository.findByRegistrationNumber(registrationNumber)).thenReturn(student);
        when(courseRepository.findCourseByCode(courseCode)).thenReturn(course);
        when(registrationRepository.findByStudentIdAndCourseId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(existingRegistration));

        String result = userService.signUpCourse(registrationNumber, courseCode);

        assertEquals("Already registered", result);
    }

    @Test
    void signUpCourse_PersistenceException() {
        String registrationNumber = "123456";
        String courseCode = "C001";

        when(studentRepository.findByRegistrationNumber(registrationNumber)).thenReturn(student);
        when(courseRepository.findCourseByCode(courseCode)).thenReturn(course);
        when(registrationRepository.save(any(Registration.class))).thenThrow(new PersistenceException());

        String result = userService.signUpCourse(registrationNumber, courseCode);

        assertTrue(result.contains("Unable to sign up the course: "));
    }

    @Test
    void retrieveCourses_SuccessfulRetrieval() {
        UUID studentId = UUID.randomUUID();

        List<Registration> registrations = new ArrayList<>();
        registrations.add(new Registration());

        when(registrationRepository.findByStudentId(studentId)).thenReturn(registrations);

        List<Course> result = userService.retrieveCourses(studentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(registrationRepository).findByStudentId(studentId);
    }

    @Test
    void retrieveCourses_NoRegistrationsFound() {
        UUID studentId = UUID.randomUUID();

        when(registrationRepository.findByStudentId(studentId)).thenReturn(new ArrayList<>());

        List<Course> result = userService.retrieveCourses(studentId);

        assertTrue(result.isEmpty());
        verify(registrationRepository).findByStudentId(studentId);
    }

    @Test
    void retrieveCourses_DatabaseError() {
        UUID studentId = UUID.randomUUID();

        when(registrationRepository.findByStudentId(studentId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> userService.retrieveCourses(studentId));
        verify(registrationRepository).findByStudentId(studentId);
    }

}