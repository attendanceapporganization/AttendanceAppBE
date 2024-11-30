package scrum.attendance_app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Lesson;

import java.time.Instant;
import java.time.Period;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Test
    void whenGettingLastLessonForCourse_thenReturnTheMaxDateOne(){
        Date currentDate = Date.from(Instant.now());
        Course course = Course.builder()
                .name("OOP")
                .description("Oriented Object Programming")
                .code("stubCode")
                .build();
        Course course2 = Course.builder()
                .name("DB")
                .description("Databases")
                .code("fakeCode")
                .build();
        Lesson lessonForAnotherCourse = Lesson.builder()
                .course(course2)
                .startDate(currentDate)
                .build();
        Lesson lastLesson = Lesson.builder()
                .course(course)
                .startDate(Date.from(currentDate.toInstant().minus(Period.ofDays(1))))
                .build();
        Lesson previousLesson = Lesson.builder()
                .course(course)
                .startDate(Date.from(currentDate.toInstant().minus(Period.ofDays(2))))
                .build();
        courseRepository.save(course);
        courseRepository.save(course2);
        Lesson persistedLastLesson = lessonRepository.save(lastLesson);
        lessonRepository.save(previousLesson);
        lessonRepository.save(lessonForAnotherCourse);
        Optional<Lesson> retrievedLastLesson = lessonRepository.findLessonWithMaxStartDateByCourse(course);
        assertEquals(persistedLastLesson.getId(), retrievedLastLesson.orElseThrow().getId());
    }
}
