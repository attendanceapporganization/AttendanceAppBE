package scrum.attendance_app.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Registry;
import scrum.attendance_app.data.entities.Student;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


@DataJpaTest
public class ProfessorRepositoryTest {

    @Autowired
    ProfessorRepository professorRepository;

    private Professor professor;
    private Registry registry;

    @BeforeEach
    public void setupData(){
        registry = Registry.builder()
                .firstname("name")
                .lastname("surname")
                .dateOfBirth(LocalDate.of(1999,12,1))
                .id(UUID.randomUUID())
                .build();
        professor = Professor.builder()
                .email("email@mail.it")
                .password("password")
                .registry(registry)
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    public void saveProfessorTest(){
        Professor savedProfessor = professorRepository.save(professor);
        assertThat(savedProfessor, notNullValue());
        assertThat(savedProfessor.getId(), notNullValue());
    }
}
