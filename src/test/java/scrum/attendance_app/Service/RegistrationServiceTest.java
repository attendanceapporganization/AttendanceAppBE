package scrum.attendance_app.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import scrum.attendance_app.data.dto.ProfessorDTO;
import scrum.attendance_app.data.dto.StudentDTO;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Registry;
import scrum.attendance_app.data.entities.Student;
import scrum.attendance_app.error_handling.exceptions.ExistingEmailException;
import scrum.attendance_app.repository.ProfessorRepository;
import scrum.attendance_app.repository.StudentRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class RegistrationServiceTest
{
    @Autowired
    private RegistrationService registrationService;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private ProfessorRepository professorRepository;

    StudentDTO studentDTO;
    Student student;
    Registry studentRegistry;
    ProfessorDTO professorDTO;
    Professor professor;
    Registry professorRegistry;

    @BeforeEach
    public void setup(){
        studentDTO = StudentDTO.builder()
                .firstname("john")
                .lastname("doe")
                .password("password")
                .email("email@mail.it")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();
        studentRegistry = Registry.builder()
                .id(UUID.fromString("240e0164-a49b-4250-a393-7108c757b251"))
                .firstname("john")
                .lastname("doe")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();
        student = Student.builder()
                .id(UUID.fromString("7ba12d04-d996-4556-83b0-668083c853f4"))
                .email("johndoe@yahoo.it")
                .password("password")
                .registry(studentRegistry)
                .build();

        professorDTO = ProfessorDTO.builder()
                .firstname("john")
                .lastname("doe")
                .password("password")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();
        professorRegistry = Registry.builder()
                .id(UUID.fromString("24713058-d7e5-4ce8-be55-4f67cbf09b44"))
                .firstname("john")
                .lastname("doe")
                .dateOfBirth(LocalDate.of(2001,1,1))
                .build();
        professor = Professor.builder()
                .id(UUID.fromString("2ff9292e-9c6c-44c4-949b-8e8a78ea5779"))
                .email("johndoe@yahoo.it")
                .password("password")
                .registry(studentRegistry)
                .build();
    }

    @Test
    void whenSavingStudent_thenReturnRegistered(){
        given(studentRepository.save(student)).willReturn(student);
        String returnMessage = registrationService.registerStudent(studentDTO);
        assertThat(returnMessage).isEqualTo("registered");
    }

    @Test
    void whenSavingProfessor_thenReturnRegistered(){
        given(professorRepository.save(professor)).willReturn(professor);
        String returnMessage = registrationService.registerProfessor(professorDTO);
        assertThat(returnMessage).isEqualTo("registered");
    }

    @Test
    void whenSaveStudentWithExistingEmail_thenExpectExistingEmailException(){
        given(studentRepository.findByEmail(anyString())).willReturn(Optional.of(student));
        Throwable thrown = catchThrowable(() -> {
            registrationService.registerStudent(studentDTO);
        });
        assertThat(thrown).isInstanceOf(ExistingEmailException.class);
    }
}