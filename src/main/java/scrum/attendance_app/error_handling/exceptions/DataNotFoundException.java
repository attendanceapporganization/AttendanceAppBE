package scrum.attendance_app.error_handling.exceptions;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String dataType) {
        super(dataType + " not found!");
    }
}
