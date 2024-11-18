package scrum.attendance_app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import scrum.attendance_app.data.entities.Registration;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class RegistrationRepositoryTest {

    @Autowired
    RegistrationRepository registrationRepository;

    @Test
    @Sql(scripts = {"file:src/test/resources/repository-test-scripts/registration.sql"})
    public void testFindRegistration(){
        List<Registration> all = registrationRepository.findAll();
        Optional<Registration> byStudentIdAndCourseId = registrationRepository.findByStudentIdAndCourseId(
                UUID.fromString("6d5385a5-c21e-49c1-b1c0-293d042a2dfe"),
                UUID.fromString("766988cd-b7e8-459e-8d94-8d74f6042691"));
        assertThat(byStudentIdAndCourseId.get().id.toString()).isEqualTo("4bb6d5a8-3516-45c9-8f9c-5b06257a30dc");
    }
}
