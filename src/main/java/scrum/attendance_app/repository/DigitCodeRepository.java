package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scrum.attendance_app.data.entities.DigitCode;

import java.util.Optional;
import java.util.UUID;

public interface DigitCodeRepository extends JpaRepository<DigitCode, UUID> {

    Optional<DigitCode> findByNumericValue(Integer numericValue);

}
