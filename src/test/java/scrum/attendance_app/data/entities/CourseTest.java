package scrum.attendance_app.data.entities;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.NotNull;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseTest {

    @Test
    void testCourseCreation(){

        Registry registry = Registry.builder()
                .firstname("name1")
                .lastname("surname1")
                .dateOfBirth(LocalDate.now())
                .build();

        Professor professor = Professor.builder()
                .email("prof1@uni.it")
                .password("prof1")
                .registry(registry)
                .build();

        Course course = Course.builder()
                .name("course1")
                .description("description1")
                .professor(professor)
                .build();

        assertThat(course != null).isTrue();
    }
}
