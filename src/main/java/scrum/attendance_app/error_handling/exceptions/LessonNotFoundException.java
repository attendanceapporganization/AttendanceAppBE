package scrum.attendance_app.error_handling.exceptions;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException() {
        super("lesson not found!");
    }
}