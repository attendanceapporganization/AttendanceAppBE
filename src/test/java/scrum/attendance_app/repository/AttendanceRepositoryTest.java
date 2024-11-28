package scrum.attendance_app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scrum.attendance_app.data.entities.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class AttendanceRepositoryTest {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private LessonRepository lessonRepository;



    @Test
    void test1(){
        Course course1 = Course.builder()
                .name("Course1")
                .code("01234567")
                .description("Description1")
                .build();

        Registry registry = Registry.builder()
                .firstname("name")
                .lastname("surname")
                .build();

        Lesson lesson = Lesson.builder()
                .startDate(new Date())
                .course(course1)
                .build();

        Student student = Student.builder()
                .email("student@mail.it")
                .password("password")
                .registrationNumber("registrationNumber")
                .registry(registry)
                .build();

        Registration registration = Registration.builder()
                .student(student)
                .course(course1)
                .registrationDate(LocalDate.now())
                .build();


        studentRepository.save(student);
        courseRepository.save(course1);
        Lesson persistedLesson = lessonRepository.save(lesson);
        Registration persistedRegistration = registrationRepository.save(registration);
        attendanceRepository.save(Attendance.builder()
                .lesson(persistedLesson)
                .registration(persistedRegistration)
                .build());


        List<Attendance> attendancesByLessonId = attendanceRepository.findAllByLessonId(persistedLesson.getId());
        assertThat(attendancesByLessonId.get(0).getRegistration().getStudent().getRegistry().getFirstname()).isEqualTo("name");
    }
}
