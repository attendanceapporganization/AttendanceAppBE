package scrum.attendance_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import scrum.attendance_app.Service.LectureCodeService;
import scrum.attendance_app.Service.UserService;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.error_handling.exceptions.NoOngoingLectureException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private LectureCodeService lectureCodeService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
    @Test
    void signUpCourse_SuccessfulRegistration() {
        String registrationNumber = "12345";
        String courseCode = "C001";

        when(userService.signUpCourse(registrationNumber, courseCode)).thenReturn("Registered");

        ResponseEntity<String> response = userController.signUpCourse(registrationNumber, courseCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sign up to course successfully", response.getBody());
    }
     */

    /*
    @Test
    void signUpCourse_AlreadyRegistered() {
        String registrationNumber = "12345";
        String courseCode = "C001";

        when(userService.signUpCourse(registrationNumber, courseCode)).thenReturn("Already registered");

        ResponseEntity<String> response = userController.signUpCourse(registrationNumber, courseCode);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Already registered to course", response.getBody());
    }
    */

    /*
    @Test
    void signUpCourse_CourseNotFound() {
        // Arrange
        String registrationNumber = "12345";
        String courseCode = "C001";

        when(userService.signUpCourse(registrationNumber, courseCode)).thenReturn("Course not found");


        ResponseEntity<String> response = userController.signUpCourse(registrationNumber, courseCode);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Non-existing course", response.getBody());
    }
     */

    /*
    @Test
    void retrieveCourse_Successful() {
        List<Course> courseList = new ArrayList<>();
        when(userService.retrieveCourses(UUID.randomUUID())).thenReturn(courseList);

        try{
            ResponseEntity<List> response = userController.retrieveCourses(UUID.randomUUID());
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
        catch (NoOngoingLectureException e) {
            throw new RuntimeException(e);
        }
    }
     */

}
