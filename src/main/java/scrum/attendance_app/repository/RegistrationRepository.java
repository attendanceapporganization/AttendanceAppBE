package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scrum.attendance_app.data.entities.Registration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegistrationRepository extends JpaRepository<Registration, UUID> {

    @Override
    List<Registration> findAll();

    Optional<Registration> findByStudentIdAndCourseId(UUID studentId, UUID courseId);

    Optional<Registration> findByStudentId(UUID studentId);
}
