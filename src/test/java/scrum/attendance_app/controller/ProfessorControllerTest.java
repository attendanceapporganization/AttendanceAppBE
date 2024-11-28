package scrum.attendance_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import scrum.attendance_app.Service.ProfessorService;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.security.TokenStore;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfessorControllerTest {

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private ProfessorController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCourse() {
        CourseDTO courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@email.it")
                .build();

        String expectedMessage = "Created";

        when(professorService.createCourse(courseDTO)).thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.createCourse(courseDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Course created successfully", result.getBody());
        verify(professorService, times(1)).createCourse(courseDTO);
    }

    @Test
    void testCreateCourseConflict() {
        CourseDTO courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@email.it")
                .build();

        String expectedMessage = "Course already exists";

        when(professorService.createCourse(courseDTO)).thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.createCourse(courseDTO);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Existing course", result.getBody());
        verify(professorService, times(1)).createCourse(courseDTO);
    }

    @Test
    void testCreateCourseError() {
        CourseDTO courseDTO = CourseDTO.builder()
                .id(UUID.randomUUID())
                .name("Course1")
                .description("Description1")
                .professorOwner("email@email.it")
                .build();

        String expectedMessage = "Unable to create course";

        when(professorService.createCourse(courseDTO)).thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.createCourse(courseDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Unable to create the course", result.getBody());
        verify(professorService, times(1)).createCourse(courseDTO);
    }

    @Test
    void testDeleteCourse() {
        String name = "Course1";
        String professorEmail = "email@email.it";

        String expectedMessage = "Deleted";

        when(professorService.deleteCourse(name, professorEmail)).thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.deleteCourse(name, professorEmail);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Course deleted successfully", result.getBody());

        verify(professorService, times(1)).deleteCourse(name, professorEmail);
    }

    @Test
    void testDeleteCourseNotFound() {
        String name = "Course1";
        String professorEmail = "email@email.it";

        String expectedMessage = "Course not found";

        when(professorService.deleteCourse(name, professorEmail)).thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.deleteCourse(name, professorEmail);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Non-existing course", result.getBody());

        verify(professorService, times(1)).deleteCourse(name, professorEmail);
    }

    @Test
    void testDeleteCourseError() {
        String name = "Course1";
        String professorEmail = "email@email.it";

        String expectedMessage = "Unable to delete the course";

        when(professorService.deleteCourse(name, professorEmail)).thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.deleteCourse(name, professorEmail);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Unable to delete the course", result.getBody());

        verify(professorService, times(1)).deleteCourse(name, professorEmail);
    }

    @Test
    void testEditCourse() {
        String expectedMessage = "Edited";

        String name = "Original Course";
        String professorEmail = "original@email.com";
        String newName = "Updated Name";
        String newDescription = "New Description";

        when(professorService.editCourse(name, professorEmail, newName, newDescription))
                .thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.editCourse(name, professorEmail, newName, newDescription);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Course updated successfully", result.getBody());

        verify(professorService, times(1)).editCourse(name, professorEmail, newName, newDescription);
    }

    @Test
    void testEditCourseNotFound() {
        String expectedMessage = "Course not found";

        String name = "Original Course";
        String professorEmail = "original@email.com";
        String newName = "Updated Name";
        String newDescription = "New Description";

        when(professorService.editCourse(name, professorEmail, newName, newDescription))
                .thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.editCourse(name, professorEmail, newName, newDescription);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Non-existing course", result.getBody());

        verify(professorService, times(1)).editCourse(name, professorEmail, newName, newDescription);
    }

    @Test
    void testEditCourseAlreadyExists() {
        String expectedMessage = "Course already exists";

        String name = "Original Course";
        String professorEmail = "original@email.com";
        String newName = "Updated Name";
        String newDescription = "New Description";

        when(professorService.editCourse(name, professorEmail, newName, newDescription))
                .thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.editCourse(name, professorEmail, newName, newDescription);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Existing course", result.getBody());

        verify(professorService, times(1)).editCourse(name, professorEmail, newName, newDescription);
    }

    @Test
    void testEditCourseError() {
        String expectedMessage = "Unable to edit the course";

        String name = "Original Course";
        String professorEmail = "original@email.com";
        String newName = "Updated Name";
        String newDescription = "New Description";

        when(professorService.editCourse(name, professorEmail, newName, newDescription))
                .thenReturn(expectedMessage);

        ResponseEntity<String> result = controller.editCourse(name, professorEmail, newName, newDescription);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Unable to edit the course", result.getBody());

        verify(professorService, times(1)).editCourse(name, professorEmail, newName, newDescription);
    }


    @Test
    void testGetList() throws Exception {
        // Dati di test
        String token = "Bearer someValidToken";
        String userEmail = "professor@email.com";
        CourseDTO course1 = new CourseDTO();
        CourseDTO course2 = new CourseDTO();
        CourseDTO course3 = new CourseDTO();

        List<CourseDTO> mockCourses = Arrays.asList(course1, course2, course3);

        try (MockedStatic<TokenStore> mockedTokenStore = mockStatic(TokenStore.class)) {
            TokenStore tokenStoreMock = mock(TokenStore.class);


            mockedTokenStore.when(TokenStore::getInstance).thenReturn(tokenStoreMock);
            when(tokenStoreMock.verifyToken(token.replace("Bearer ", ""))).thenReturn(true);
            when(tokenStoreMock.getUser(token.replace("Bearer ", ""))).thenReturn(userEmail);
            when(professorService.getListCourse(userEmail)).thenReturn(mockCourses);


            ResponseEntity<List> result = controller.getListCourses(token);


            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(mockCourses, result.getBody());


            verify(tokenStoreMock, times(1)).verifyToken(token.replace("Bearer ", ""));
            verify(tokenStoreMock, times(1)).getUser(token.replace("Bearer ", ""));
            verify(professorService, times(1)).getListCourse(userEmail);
        }
    }


}