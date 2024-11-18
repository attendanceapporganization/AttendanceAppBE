package scrum.attendance_app.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import scrum.attendance_app.Service.DigitCodeGenerator;
import scrum.attendance_app.config.SecurityConfig;
import scrum.attendance_app.data.entities.Course;
import scrum.attendance_app.repository.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DigitCodeController.class)
@Import({DigitCodeGenerator.class})
@ComponentScan(basePackageClasses = {SecurityConfig.class})
public class DigitCodeControllerTest {


}
