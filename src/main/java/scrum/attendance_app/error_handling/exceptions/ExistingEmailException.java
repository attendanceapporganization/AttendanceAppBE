package scrum.attendance_app.error_handling.exceptions;

public class ExistingEmailException extends RuntimeException {
  public ExistingEmailException(String emailAddress) {
    super("Email " + emailAddress + " already  registered!");
  }
}
