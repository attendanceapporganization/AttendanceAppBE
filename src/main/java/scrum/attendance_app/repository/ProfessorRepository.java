package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {

    Optional<Professor> findByEmail(String email);

}
