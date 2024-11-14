package scrum.attendance_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.data.entities.DigitCode;
import scrum.attendance_app.data.entities.Lesson;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.DigitCodeRepository;
import scrum.attendance_app.repository.LessonRepository;
import scrum.attendance_app.repository.StudentRepository;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class DigitCodeController {

    @Autowired
    private DigitCodeGenerator digitCodeGenerator;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DigitCodeRepository digitCodeRepository;

//    @GetMapping("newValue")
//    public String generateNewDigitCode(){
//        return digitCodeGenerator.generateCode().formattedValue();
//    }

    @GetMapping("/newValue")
    public String generateNewDigitCode(@RequestParam UUID IDCourse) {
        // Generate and save a new DigitCode
        DigitCode digitCode = DigitCode.createDigitCode();
        digitCodeRepository.save(digitCode);

        Lesson lesson = Lesson.builder()
                .time_interval("10:00-12:00")
                .date(new Date())
                .digitCode(digitCode)
                .course(courseRepository.findById(IDCourse).orElse(null)) // Link to the specified Course
                .build();


        lessonRepository.save(lesson);

        return "New Lesson created with DigitCode: " + digitCode.formattedValue();
    }

}
