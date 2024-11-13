package scrum.attendance_app.Service;

import org.springframework.stereotype.Service;
import scrum.attendance_app.data.entities.DigitCode;

@Service
public class DigitCodeGenerator {

    public DigitCode generateCode(){
        return DigitCode.createDigitCode();
    }
}
