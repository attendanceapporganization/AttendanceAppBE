package scrum.attendance_app.error_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import scrum.attendance_app.error_handling.exceptions.*;

@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(ExistingEmailException.class)
    public ResponseEntity<String> existingEmailExceptionHandler(){
        return new ResponseEntity<>("Existing email", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExistingCourseNameException.class)
    public ResponseEntity<String> existingCourseExceptionHandler(){
        return new ResponseEntity<>("Existing course", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoOngoingLectureException.class)
    public ResponseEntity<String> noOngoingLectureExceptionHandler(){
        return new ResponseEntity<>("No lecture is going on for this course currently", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RegistrationNotFoundException.class)
    public ResponseEntity<String> registrationNotFoundExceptionHandler(){
        return new ResponseEntity<>("Student not registered to this course", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> studentNotFoundExceptionHandler(){
        return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<String> lessonNotFoundExceptionHandler(){
        return new ResponseEntity<>("Lesson not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongAttendanceCodeException.class)
    public ResponseEntity<String> wrongAttendanceCodeExceptionHandler(){
        return new ResponseEntity<>("Provided digit code is not valid for this lesson", HttpStatus.NOT_ACCEPTABLE);
    }
}
