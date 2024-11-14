package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scrum.attendance_app.data.entities.Registration;

import java.util.Optional;
import java.util.UUID;

public interface RegistrationRepository extends JpaRepository<Registration, UUID> {

    Optional<Registration> findByStudentId(UUID studentId);
}
