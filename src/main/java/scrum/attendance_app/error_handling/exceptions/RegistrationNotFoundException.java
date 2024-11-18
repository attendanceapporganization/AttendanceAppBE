package scrum.attendance_app.error_handling.exceptions;

public class RegistrationNotFoundException extends RuntimeException {
  public RegistrationNotFoundException() {
    super("Registration not found");
  }
}
