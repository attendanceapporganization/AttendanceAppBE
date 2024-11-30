package scrum.attendance_app.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.Service.LectureCodeService;
import scrum.attendance_app.Service.ProfessorService;
import scrum.attendance_app.config.SecurityConfig;
import scrum.attendance_app.config.TestConfig;
import scrum.attendance_app.data.entities.*;
import scrum.attendance_app.error_handling.exceptions.WrongAttendanceCodeException;
import scrum.attendance_app.mapper.LessonMapper;
import scrum.attendance_app.mapper.StudentMapper;
import scrum.attendance_app.repository.*;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfessorController.class)
@Import({ProfessorService.class, DigitCodeGenerator.class})
@ComponentScan(basePackageClasses = {TestConfig.class})
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
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
    private ProfessorService professorService;
    @Autowired
    private LessonMapper mapper;

    private static String code;
    private static DigitCode digitCode;
    private static Lesson lessonInstance;
    private static Student studentInstance;
    private static Registration registrationInstance;

    @Test
    void createLessonTest() throws Exception {
        Course fakeCourse = Course.builder()
                .id(UUID.randomUUID())
                .code("1234")
                .name("Operative systems")
                .build();
        when(courseRepository.findById(any())).thenReturn(Optional.ofNullable(fakeCourse));
        when(lessonRepository.save(any())).thenReturn(Lesson.builder()
                .digitCode(DigitCode.createDigitCode())
                .course(fakeCourse)
                .startDate(Date.from(Instant.now()))
                .build());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/startLesson")
                .param("courseId",UUID.randomUUID().toString())).andExpect(status().isOk());
    }

    /*
    @BeforeAll
    public static void setup(){
        code = "1234";
        digitCode=mock(DigitCode.class);
        lessonInstance = Lesson.builder()
            .id(UUID.randomUUID())
            .digitCode(digitCode)
            .build();
        studentInstance = Student.builder()
            .id(UUID.randomUUID())
            .email("email@address.com")
            .build();
        registrationInstance = Registration.builder()
            .id(UUID.randomUUID())
            .build();
    }

    @Test
    void whenTakeAttendanceWithRightCode_ThenOk() throws Exception {
        Mockito.when(digitCode.formattedValue()).thenReturn(code);
        Mockito.when(lessonRepository.findByCourseIdAndEndDateNull(any())).thenReturn(Optional.ofNullable(lessonInstance));
        Mockito.when(studentRepository.findById(any())).thenReturn(Optional.ofNullable(studentInstance));
        Mockito.when(registrationRepository.findByStudentIdAndCourseId(any(), any())).thenReturn(Optional.ofNullable(registrationInstance));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/student/takeAttendance")
                        .param("studentId", UUID.randomUUID().toString())
                        .param("code", code)
                        .param("courseId", UUID.randomUUID().toString())
                        )
                .andExpect(status().isOk())
                .andExpect(content().string("you are present at this lesson")); // Verifica che il corpo della risposta contenga il messaggio giusto
    }
    @Test
    void whenTakeAttendanceWithWrongCode_ThenRaiseWrongAttendanceCodeException() {
        Mockito.when(digitCode.formattedValue()).thenReturn("wrong-code");
        Mockito.when(lessonRepository.findByCourseIdAndEndDateNull(any())).thenReturn(Optional.ofNullable(lessonInstance));
        Mockito.when(studentRepository.findById(any())).thenReturn(Optional.ofNullable(studentInstance));
        Mockito.when(registrationRepository.findByStudentIdAndCourseId(any(), any())).thenReturn(Optional.ofNullable(registrationInstance));

        Throwable thrown = catchThrowable(() -> {
            lectureCodeService.registerAttendance(UUID.randomUUID(),code,UUID.randomUUID());
        });
        assertThat(thrown).isInstanceOf(WrongAttendanceCodeException.class);
    }

     */

}
