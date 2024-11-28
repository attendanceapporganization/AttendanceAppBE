package scrum.attendance_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import scrum.attendance_app.Service.ProfessorService;
import scrum.attendance_app.Service.RegistrationService;
import scrum.attendance_app.config.SecurityConfig;
import scrum.attendance_app.data.dto.ProfessorDTO;
import scrum.attendance_app.data.dto.StudentDTO;
import scrum.attendance_app.mapper.StudentMapper;
import scrum.attendance_app.repository.*;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;


@WebMvcTest
@Import(StudentRepository.class)
@ComponentScan(basePackageClasses = {RegistrationService.class, SecurityConfig.class, StudentMapper.class})
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private ProfessorService professorService;
    @MockBean
    private ProfessorRepository professorRepository;
    @MockBean
    private DigitCodeRepository digitCodeRepository;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private AttendanceRepository attendanceRepository;
    @MockBean
    private RegistrationRepository registrationRepository;
    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void whenRegisterStudent_ThenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registerStudent").contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(StudentDTO.builder()
                        .id(UUID.randomUUID())
                        .email("email@mail.it")
                        .password("password")
                        .registrationNumber("matricola")
                        .build()))).andExpect(status().isOk());

    }
    @Test
    public void whenRegisterProfessor_ThenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registerProfessor").contentType(MediaType.APPLICATION_JSON).
                content(mapper.writeValueAsString(ProfessorDTO.builder()
                        .id(UUID.randomUUID())
                        .email("email@mail.it")
                        .password("password")
                        .build()))).andExpect(status().isOk());

    }

}