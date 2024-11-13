package scrum.attendance_app.Service;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.dto.ProfessorDTO;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Registry;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.CourseRepositoryTest;
import scrum.attendance_app.repository.ProfessorRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ProfessorServiceTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ProfessorService professorService;

    @MockBean
    private CourseRepository courseRepository;

    private ProfessorDTO professorDTO;
    private Professor professor;
    private Registry registry;

    CourseDTO courseDTO;
    Course course;

    public void setup(){
        registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        professorDTO = ProfessorDTO.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .password("password")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();

        professor = Professor.builder()
                .id(UUID.randomUUID())
                .email("email@mail.it")
                .password("password")
                .registry(registry)
                .build();

        professorRepository.save(professor);

        courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@mail.it")
                .build();

        course = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();
    }

    @Test
    void whenSavingCourse_thenReturnCreated(){
        setup();

        given(courseRepository.save(course)).willReturn(course);
        String returnMessage = professorService.createCourse(courseDTO);
        assertThat(returnMessage).isEqualTo("Created");

        professorRepository.delete(professor);
        courseRepository.delete(course);
    }

    @Test
    void whenSavingCourse_thenReturnProfessorDoesNotExist() {
        courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@mail2.it")
                .build();

        course = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professor(professor)
                .build();

        given(courseRepository.save(course)).willReturn(course);
        String returnMessage = professorService.createCourse(courseDTO);
        assertThat(returnMessage).isEqualTo("Professor does not exist");
    }

}
