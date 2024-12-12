package scrum.attendance_app.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import scrum.attendance_app.Service.LectureCodeService;
import scrum.attendance_app.Service.ProfessorService;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.dto.StudentDTO;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Student;
import scrum.attendance_app.mapper.StudentMapper;
import scrum.attendance_app.security.TokenStore;

import java.awt.*;
import scrum.attendance_app.data.dto.LessonDTO;
import scrum.attendance_app.data.entities.DigitCode;
import scrum.attendance_app.data.entities.Lesson;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class ProfessorController {

    private final ProfessorService professorService;
    private final StudentMapper studentMapper;

    // Method to create course with give courseDTO
    // Possible httpStatus OK,CONFLICT and INTERNAL_SERVER_ERROR
    @PostMapping(path = "/createCourse")
    public ResponseEntity<String> createCourse(@RequestBody CourseDTO courseDTO) {
        String createCourseStatus = professorService.createCourse(courseDTO);

        if(createCourseStatus.equals("Created")) {
            return new ResponseEntity<>("Course created successfully", HttpStatus.OK);
        }
        else if(createCourseStatus.equals("Course already exists")) {
            return new ResponseEntity<>("Existing course", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Unable to create the course", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Controller function to delete the course with given name and professor email
    // Possible httpStatus OK,NOT_FOUND and INTERNAL_SERVER_ERROR
    @PostMapping(path = "/deleteCourse")
    public ResponseEntity<String> deleteCourse(@RequestParam("name") String name, @RequestParam("professorEmail") String professorEmail) {
        String deleteCourseStatus = professorService.deleteCourse(name, professorEmail);

        if(deleteCourseStatus.equals("Deleted")) {
            return new ResponseEntity<>("Course deleted successfully", HttpStatus.OK);
        }

        else if(deleteCourseStatus.equals("Course not found")) {
            return new ResponseEntity<>("Non-existing course", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Unable to delete the course", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/editCourse")
    public ResponseEntity<String> editCourse(@RequestParam("name") String name, @RequestParam("professorEmail") String professorEmail, @RequestParam("newName") String newName, @RequestParam("newDescription") String newDescription) {
        String editCourseStatus = professorService.editCourse(name, professorEmail, newName, newDescription);

        if(editCourseStatus.equals("Edited")) {
            return new ResponseEntity<>("Course updated successfully", HttpStatus.OK);
        }
        else if(editCourseStatus.equals("Course not found")) {
            return new ResponseEntity<>("Non-existing course", HttpStatus.NOT_FOUND);
        }
        else if(editCourseStatus.equals("Course already exists")) {
            return new ResponseEntity<>("Existing course", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Unable to edit the course", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("startLesson")
    public ResponseEntity<LessonDTO> createAndStartLesson(@RequestParam UUID courseId){
        DigitCode digitCode = DigitCode.createDigitCode();
        Lesson lesson = Lesson.builder()
                .startDate(Date.from(Instant.now()))
                .endDate(null)
                .digitCode(digitCode)
                .course(professorService.retrieveCourse(courseId)) // Link to the specified Course
                .build();
        //Lesson lesson1 = professorService.createLesson(lesson);
        //return "Lesson started: code" + digitCode.formattedValue() + " id: " + professorService.createLesson(lesson);
        return new ResponseEntity<>(professorService.createLesson(lesson), HttpStatus.OK);
    }

    @GetMapping("terminateLesson")
    @Transactional
    public String terminateLesson(@RequestParam UUID courseId){
        Course course = professorService.retrieveCourse(courseId);
        professorService.terminateLessonOfCourse(course);
        return "Lesson terminated.";
    }


    @GetMapping(path = "/ListCourses")
    public ResponseEntity<java.util.List> getListCourses(
            @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        String token = authorizationHeader.replace("Bearer ", "");
        TokenStore.getInstance().verifyToken(token);
        String user = TokenStore.getInstance().getUser(token);
        java.util.List corsi = professorService.getListCourse(user);


        return new ResponseEntity<java.util.List>(corsi, HttpStatus.OK);
    }

    @GetMapping(path="students-attending")
    public ResponseEntity<List<StudentDTO>> getStudentsAttending(@RequestParam UUID lessonId){
        return new ResponseEntity<>(professorService.getStudentsAttending(lessonId), HttpStatus.OK);
    }

    @PutMapping("confirm")
    public String changeStudentsAttending(@RequestParam UUID lessonId, @RequestBody List<StudentDTO> confirmedStudents){
        professorService.changeStudentsAttending(lessonId, confirmedStudents.stream().map(studentMapper::toEntity).toList());
        return "ok";
    }
}
