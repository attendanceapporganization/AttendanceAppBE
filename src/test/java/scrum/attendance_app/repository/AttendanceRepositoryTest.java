package scrum.attendance_app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scrum.attendance_app.data.entities.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Test
    void test2(){
        Registry registry1 = Registry.builder()
                .firstname("name1")
                .lastname("surname1")
                .build();
        Registry registry2 = Registry.builder()
                .firstname("name2")
                .lastname("surname2")
                .build();
        Student s1 = Student.builder()
                .registrationNumber("matricola1")
                .email("s1@mail.it")
                .password("pwd")
                .registry(registry1)
                .build();
        Student s2 = Student.builder()
                .registrationNumber("matricola2")
                .email("s2@mail.it")
                .password("pwd")
                .registry(registry2)
                .build();
        Course course = Course.builder()
                .name("OOP")
                .code("12345678")
                .build();
        Course course2 = Course.builder()
                .name("RDM")
                .code("87654321")
                .build();
        Date currentDate = Date.from(Instant.now());
        Registration student1Course1 = Registration.builder()
                .registrationDate(LocalDate.now().minus(Period.ofMonths(3)))
                .student(s1)
                .course(course)
                .build();
        Registration student2Course1 = Registration.builder()
                .registrationDate(LocalDate.now().minus(Period.ofMonths(3)))
                .student(s2)
                .course(course)
                .build();
        Registration student1Course2 = Registration.builder()
                .registrationDate(LocalDate.now().minus(Period.ofMonths(3)))
                .student(s1)
                .course(course)
                .build();
        Registration student2Course2 = Registration.builder()
                .registrationDate(LocalDate.now().minus(Period.ofMonths(3)))
                .student(s2)
                .course(course)
                .build();

        Lesson lesson1 = Lesson.builder()
                .startDate(Date.from(currentDate.toInstant().minus(Period.ofDays(1))))
                .course(course)
                .build();
        Lesson lesson2 = Lesson.builder()
                .startDate(Date.from(currentDate.toInstant().minus(Period.ofDays(2))))
                .course(course)
                .build();
        Lesson lesson1C2 = Lesson.builder()
                .startDate(Date.from(currentDate.toInstant().minus(Period.ofDays(2))))
                .course(course2)
                .build();
        Attendance attendanceS1L1 = Attendance.builder()
                .lesson(lesson1)
                .registration(student1Course1)
                .build();
        Attendance attendanceS1L2 = Attendance.builder()
                .lesson(lesson2)
                .registration(student1Course1)
                .build();
        Attendance attendanceS2L1 = Attendance.builder()
                .lesson(lesson1)
                .registration(student2Course1)
                .build();

        Attendance attendanceS1L1C2 = Attendance.builder()
                .lesson(lesson1C2)
                .registration(student1Course2)
                .build();
        Attendance attendanceS2L1C2 = Attendance.builder()
                .lesson(lesson1C2)
                .registration(student2Course2)
                .build();
        studentRepository.save(s1);
        studentRepository.save(s2);
        Course savedOopCourse = courseRepository.save(course);
        Course savedRdmCourse = courseRepository.save(course2);
        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        lessonRepository.save(lesson1C2);
        registrationRepository.save(student1Course1);
        registrationRepository.save(student2Course1);
        registrationRepository.save(student1Course2);
        registrationRepository.save(student2Course2);
        attendanceRepository.save(attendanceS1L1);
        attendanceRepository.save(attendanceS2L1);
        attendanceRepository.save(attendanceS1L2);
        attendanceRepository.save(attendanceS1L1C2);
        attendanceRepository.save(attendanceS2L1C2);
        List<Attendance> oopAttendances = attendanceRepository.findAllByCourse(savedOopCourse.getId());
        List<Attendance> rdmAttendances = attendanceRepository.findAllByCourse(savedRdmCourse.getId());
        assertThat(oopAttendances).hasSize(3);
        assertThat(rdmAttendances).hasSize(2);
    }
}
