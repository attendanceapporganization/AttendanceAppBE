package scrum.attendance_app.error_handling.exceptions;

public class ExistingCourseNameException extends RuntimeException {
    public ExistingCourseNameException(String name) {
        super("Course " + name + " already  exist!");
    }
}
