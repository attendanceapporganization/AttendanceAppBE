package scrum.attendance_app.controller;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.Service.LectureCodeService;
import scrum.attendance_app.Service.ProfessorService;
import scrum.attendance_app.Service.UserService;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.data.entities.Student;
import scrum.attendance_app.error_handling.exceptions.NoOngoingLectureException;
import scrum.attendance_app.error_handling.exceptions.WrongAttendanceCodeException;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.DigitCodeRepository;
import scrum.attendance_app.repository.LessonRepository;
import scrum.attendance_app.security.TokenStore;

import java.util.UUID;

@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    private DigitCodeGenerator digitCodeGenerator;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DigitCodeRepository digitCodeRepository;

    @Autowired
    private LectureCodeService lectureCodeService;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/student/takeAttendance")
    public ResponseEntity<String> lectureCode(@RequestParam UUID studentId, @RequestParam String code, @RequestParam UUID courseId) throws NoOngoingLectureException, WrongAttendanceCodeException {
        lectureCodeService.registerAttendance(studentId, code, courseId);
        return new ResponseEntity<>("you are present at this lesson", HttpStatus.OK);
    }

    @PostMapping("/student/signUpCourse")
    public ResponseEntity<String> signUpCourse(@RequestParam String registrationNumber, @RequestParam String courseCode) {
        String status = userService.signUpCourse(registrationNumber, courseCode);

        if(status.equals("Course not found")){
            return new ResponseEntity<>("Non-existing course", HttpStatus.NOT_FOUND);
        }
        else if(status.equals("Already registered")){
            return new ResponseEntity<>("Already registered to course", HttpStatus.CONFLICT);
        }
        else if(status.equals("Registered")){
            return new ResponseEntity<>("Sign up to course successfully", HttpStatus.OK);

        }

        return new ResponseEntity<>("Unable to sign up the course", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/student/retrieveCourses")
    public ResponseEntity<java.util.List> retrieveCourses(@RequestParam UUID userID) throws NoOngoingLectureException, WrongAttendanceCodeException {
        java.util.List courses = userService.retrieveCourses(userID);
        return new ResponseEntity<java.util.List>(courses, HttpStatus.OK);
    }

}
