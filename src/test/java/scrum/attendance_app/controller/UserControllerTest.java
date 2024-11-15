package scrum.attendance_app.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.Service.LectureCodeService;
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
public class UserControllerTest {

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

    @MockBean
    private LectureCodeService lectureCodeService;

    //    @Test
//    void generatedCodeIs4ciphersLong() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/newValue")).andExpect(status().isOk());
//    }
    @Test
    void lectureCode() throws Exception {
        UUID studentId = UUID.randomUUID(); // Genera un ID dello studente casuale
        String code = "1234";

        Mockito.when(lectureCodeService.registerAttendance(studentId, code)).thenReturn(true);


        // Esegui la richiesta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/LectureCode")
                        .param("studentId", studentId.toString())
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("you are present at this lesson")); // Verifica che il corpo della risposta contenga il messaggio giusto
    }
}
