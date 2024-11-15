package scrum.attendance_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.Service.LectureCodeService;
import scrum.attendance_app.Service.ProfessorService;
import scrum.attendance_app.data.entities.DigitCode;
import scrum.attendance_app.data.entities.Lesson;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.DigitCodeRepository;
import scrum.attendance_app.repository.LessonRepository;

import java.util.Date;
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

    @PostMapping("/LectureCode")
    public ResponseEntity<String> generateNewDigitCode(@RequestParam UUID studentId, String code) {

        boolean checkCode = lectureCodeService.registerAttendance(studentId, code);

        if(checkCode){
            return new ResponseEntity<>("you are present at this lesson", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Code does not exist", HttpStatus.CONFLICT);
        }
    }

}
