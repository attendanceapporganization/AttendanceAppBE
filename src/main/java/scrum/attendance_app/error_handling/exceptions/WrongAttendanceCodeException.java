package scrum.attendance_app.error_handling.exceptions;

public class WrongAttendanceCodeException extends RuntimeException {
    public WrongAttendanceCodeException() {
        super("Provided code is incorrect!");
    }
}
