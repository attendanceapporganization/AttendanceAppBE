package scrum.attendance_app.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.Service.ProfessorService;
import scrum.attendance_app.config.SecurityConfig;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Lesson;
import scrum.attendance_app.repository.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfessorController.class)
@Import({DigitCodeGenerator.class, ProfessorService.class})
@ComponentScan(basePackageClasses = {SecurityConfig.class})
public class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private ProfessorRepository professorRepository;

    @MockBean
    private DigitCodeRepository digitCodeRepository;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private DigitCodeGenerator digitCodeGenerator;


    @Test
    void whenStartLesson_thenOk() throws Exception {
        UUID testCourseId = UUID.randomUUID();
        Course course = Course.builder()
                .id(UUID.randomUUID())
                .build();
        Lesson lesson = Lesson.builder()
                .id(UUID.randomUUID())
                .build();
        Mockito.when(courseRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(course));
        Mockito.when(lessonRepository.save(ArgumentMatchers.any())).thenReturn(lesson);
        // Esegui la richiesta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/startLesson")
                        .param("courseId", testCourseId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Lesson started")));
    }

    @Test
    void whenTerminateLesson_thenOk() throws Exception {
        Lesson lesson = Lesson.builder()
                .id(UUID.randomUUID())
                .build();
        Mockito.when(lessonRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(lesson));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/terminateLesson")
                        .param("lessonId", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Lesson terminated.")));
    }
}
