package scrum.attendance_app.error_handling.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException() {
        super("course not found!");
    }
}
