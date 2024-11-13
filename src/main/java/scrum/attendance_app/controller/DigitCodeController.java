package scrum.attendance_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.repository.StudentRepository;

@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class DigitCodeController {

    @Autowired
    DigitCodeGenerator digitCodeGenerator;

    @GetMapping("newValue")
    public String generateNewDigitCode(){
        return digitCodeGenerator.generateCode().formattedValue();
    }
}
