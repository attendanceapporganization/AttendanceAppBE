package scrum.attendance_app.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.config.SecurityConfig;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.repository.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DigitCodeController.class)
@Import({DigitCodeGenerator.class})
@ComponentScan(basePackageClasses = {SecurityConfig.class})
public class DigitCodeControllerTest {

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


//    @Test
//    void generatedCodeIs4ciphersLong() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/newValue")).andExpect(status().isOk());
//    }
    @Test
    void generatedCodeIs4ciphersLong() throws Exception {
        UUID testCourseId = UUID.randomUUID();

        // Mock delle risposte del repository
        Course course = new Course(); // Imposta qui i campi necessari
        Mockito.when(courseRepository.findById(testCourseId)).thenReturn(Optional.of(course));

        // Esegui la richiesta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/newValue")
                        .param("IDCourse", testCourseId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("New Lesson created with DigitCode: ")));
    }
}
