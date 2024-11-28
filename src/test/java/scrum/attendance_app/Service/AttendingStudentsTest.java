package scrum.attendance_app.Service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import scrum.attendance_app.data.dto.StudentDTO;
import scrum.attendance_app.data.entities.*;
import scrum.attendance_app.repository.AttendanceRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class AttendingStudentsTest {
    @MockBean
    private AttendanceRepository attendanceRepository;
    @Autowired
    private ProfessorService professorService;

    @Test
    void whenOneStudentAttending_thenExpectHisFirstname(){
        Course course1 = Course.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .build();

        Registry registry = Registry.builder()
                .id(UUID.randomUUID())
                .firstname("name")
                .lastname("surname")
                .build();

        Lesson lesson = Lesson.builder()
                .id(UUID.randomUUID())
                .build();

        Student student = Student.builder()
                .email("student@mail.it")
                .registry(registry)
                .build();

        Registration registration = Registration.builder()
                .student(student)
                .course(course1)
                .build();

        Attendance attendance1 = Attendance.builder()
                .registration(registration)
                .lesson(lesson)
                .build();

        when(attendanceRepository.findAllByLessonId(any())).thenReturn(Collections.singletonList(attendance1));

        List<StudentDTO> studentsAttending = professorService.getStudentsAttending(UUID.randomUUID());

        assertEquals("name",studentsAttending.get(0).getFirstname());
    }
}
