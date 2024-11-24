package scrum.attendance_app.Service;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Registry;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.ProfessorRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfessorServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourse_shouldReturnCreated_whenValidInput() {
        CourseDTO courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@email.it")
                .build();

        when(professorRepository.findByEmail(courseDTO.getProfessorOwner())).thenReturn(java.util.Optional.of(new Professor()));

        String result = professorService.createCourse(courseDTO);

        assertEquals("Created", result);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_shouldReturnError_whenCourseAlreadyExists() {
        CourseDTO courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@email.it")
                .build();

        Registry registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        Professor professor = Professor.builder()
                .id(UUID.randomUUID())
                .email("email@email.it")
                .password("password")
                .registry(registry)
                .build();

        Course existingCourse = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

        when(professorRepository.findByEmail(courseDTO.getProfessorOwner())).thenReturn(java.util.Optional.of(professor));
        when(courseRepository.findByCourseNameLowercase(courseDTO.getName())).thenReturn(java.util.Collections.singletonList(existingCourse));

        String result = professorService.createCourse(courseDTO);

        assertEquals("Course already exists", result);
    }

    @Test
    void createCourse_shouldReturnError_whenProfessorDoesNotExist() {
        CourseDTO courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@email.it")
                .build();

        when(professorRepository.findByEmail(courseDTO.getProfessorOwner())).thenReturn(java.util.Optional.empty());

        String result = professorService.createCourse(courseDTO);

        assertEquals("Professor does not exist", result);
    }

    @Test
    void deleteCourse_shouldReturnDeleted_whenValidInput() {
        String name = "Course1";
        String email = "email@email.it";

        Registry registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        Professor professor = Professor.builder()
                .id(UUID.randomUUID())
                .email("email@email.it")
                .password("password")
                .registry(registry)
                .build();

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

        when(professorRepository.findByEmail(email)).thenReturn(java.util.Optional.of(professor));
        when(courseRepository.findByCourseNameLowercase(name)).thenReturn(java.util.Collections.singletonList(course));

        String result = professorService.deleteCourse(name, email);

        assertEquals("Deleted", result);
        verify(courseRepository).delete(course);
    }

    @Test
    void deleteCourse_shouldReturnError_whenCourseNotFound() {
        String name = "CourseNotFound";
        String email = "email@email.it";

        when(professorRepository.findByEmail(email)).thenReturn(java.util.Optional.of(new Professor()));
        when(courseRepository.findByCourseNameLowercase(name)).thenReturn(java.util.Collections.emptyList());

        String result = professorService.deleteCourse(name, email);

        assertEquals("Course not found", result);
    }

    @Test
    void deleteCourse_shouldReturnError_whenProfessorDoesNotExist() {
        String name = "Existing Course";
        String email = "NonexistentProfessor@email.it";

        when(professorRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());

        String result = professorService.deleteCourse(name, email);

        assertEquals("Professor does not exist", result);
    }

    @Test
    void editCourse_shouldReturnEdited_whenValidInput() {
        // Arrange
        String originalName = "Course1";
        String originalEmail = "email@email.it";
        String newName = "Updated Course";
        String newDescription = "New description";

        Registry registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        Professor professor = Professor.builder()
                .id(UUID.randomUUID())
                .email("email@email.it")
                .password("password")
                .registry(registry)
                .build();

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

        when(professorRepository.findByEmail(originalEmail)).thenReturn(java.util.Optional.of(professor));
        when(courseRepository.findByCourseNameLowercase(originalName)).thenReturn(java.util.Collections.singletonList(course));

        String result = professorService.editCourse(originalName, originalEmail, newName, newDescription);

        assertEquals("Edited", result);
        verify(courseRepository).save(course);
    }

    @Test
    void editCourse_shouldReturnError_whenCourseNotFound() {
        // Arrange
        String originalName = "CourseNotFound";
        String originalEmail = "email@email.it";
        String newName = "Updated Course";
        String newDescription = "New description";

        when(professorRepository.findByEmail(originalEmail)).thenReturn(java.util.Optional.of(new Professor()));
        when(courseRepository.findByCourseNameLowercase(originalName)).thenReturn(java.util.Collections.emptyList());

        String result = professorService.editCourse(originalName, originalEmail, newName, newDescription);

        assertEquals("Course not found", result);
    }

    @Test
    void createCourse_shouldHandlePersistenceExceptions() {
        CourseDTO courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@email.it")
                .build();

        Registry registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        Professor professor = Professor.builder()
                .id(UUID.randomUUID())
                .email("email@email2.it")
                .password("password")
                .registry(registry)
                .build();

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

        when(professorRepository.findByEmail(courseDTO.getProfessorOwner())).thenReturn(Optional.of(professor));
        when(courseRepository.findByCourseNameLowercase(courseDTO.getName())).thenReturn(Collections.singletonList(course));
        doThrow(PersistenceException.class).when(courseRepository).save(any(Course.class));

        String result = professorService.createCourse(courseDTO);

        assertTrue(result.contains("Course creation failed due to persistence error: "));
    }

    @Test
    void deleteCourse_shouldHandlePersistenceExceptions() {

        String name = "Course1";
        String email = "email@email.it";

        Registry registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        Professor professor = Professor.builder()
                .id(UUID.randomUUID())
                .email("email@email.it")
                .password("password")
                .registry(registry)
                .build();

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

        when(professorRepository.findByEmail(email)).thenReturn(Optional.of(professor));
        when(courseRepository.findByCourseNameLowercase(name)).thenReturn(Collections.singletonList(course));
        doThrow(PersistenceException.class).when(courseRepository).delete(any(Course.class));

        String result = professorService.deleteCourse(name, email);
        assertTrue(result.contains("Course deletion failed due to persistence error: "));
    }

    @Test
    void editCourse_shouldHandlePersistenceExceptions() {
        String originalName = "Course1";
        String originalEmail = "email@email.it";
        String newName = "Updated Course";
        String newDescription = "New description";

        Registry registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        Professor professor = Professor.builder()
                .id(UUID.randomUUID())
                .email("email@email.it")
                .password("password")
                .registry(registry)
                .build();

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

        when(professorRepository.findByEmail(originalEmail)).thenReturn(Optional.of(professor));
        when(courseRepository.findByCourseNameLowercase(originalName)).thenReturn(Collections.singletonList(course));
        doThrow(PersistenceException.class).when(courseRepository).save(any(Course.class));

        String result = professorService.editCourse(originalName, originalEmail, newName, newDescription);
        assertTrue(result.contains("Course edit failed due to persistence error: "));
    }

    @Test
    void editCourse_shouldReturnError_whenCourseExists() {
        // Arrange
        String originalName = "Course1";
        String originalEmail = "email@email.it";
        String newName = "Course2";
        String newDescription = "New description";

        Registry registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        Professor professor = Professor.builder()
                .id(UUID.randomUUID())
                .email("email@email.it")
                .password("password")
                .registry(registry)
                .build();

        Course course1 = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

        Course course2 = Course.builder()
                .id(UUID.randomUUID())
                .name("Course2")
                .description("Description1")
                .professor(professor)
                .build();

        when(professorRepository.findByEmail(originalEmail)).thenReturn(Optional.of(professor));
        when(courseRepository.findByCourseNameLowercase(originalName)).thenReturn(Collections.singletonList(course1));
        when(courseRepository.findByCourseNameLowercase(newName)).thenReturn(Collections.singletonList(course2));

        String result = professorService.editCourse(originalName, originalEmail, newName, newDescription);

        assertEquals("Course already exists", result);
    }

}