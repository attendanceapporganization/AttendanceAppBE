package scrum.attendance_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import scrum.attendance_app.Service.ProfessorService;
import scrum.attendance_app.data.dto.CourseDTO;

@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

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

}
