package scrum.attendance_app.error_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import scrum.attendance_app.error_handling.exceptions.ExistingCourseNameException;
import scrum.attendance_app.error_handling.exceptions.ExistingEmailException;

@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(ExistingEmailException.class)
    public ResponseEntity<String> existingEmail(){
        return new ResponseEntity<>("Existing email", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExistingCourseNameException.class)
    public ResponseEntity<String> existingCourse(){
        return new ResponseEntity<>("Existing course", HttpStatus.CONFLICT);
    }
}
