package scrum.attendance_app.error_handling.exceptions;

public class StudentNotFoundException extends RuntimeException {
  public StudentNotFoundException() {
    super("Student not found");
  }
}
