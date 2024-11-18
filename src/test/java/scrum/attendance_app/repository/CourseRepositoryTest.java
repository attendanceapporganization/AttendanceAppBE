package scrum.attendance_app.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Registry;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    private Professor professor;
    private Registry registry;
    private Course course;

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

        course = Course.builder()
                .name("Course1")
                .description("Description1")
                .id(UUID.randomUUID())
                .professor(professor)
                .build();
    }

    @Test
    public void saveCourseTest(){
        Course savedCourse = courseRepository.save(course);
        assertThat(savedCourse, notNullValue());
        assertThat(savedCourse.getId(), notNullValue());
    }

    @Test
    public void deleteCourseTest(){
        Course courseToSave = courseRepository.save(course);
        assertThat(courseToSave, notNullValue());
        assertThat(courseToSave.getId(), notNullValue());

        Course courseToDelete = courseRepository.findByName("Course1");
        courseRepository.delete(courseToDelete);

        Course courseDeleted = courseRepository.findByName("Course1");
        assertNull(courseDeleted);
    }
}
