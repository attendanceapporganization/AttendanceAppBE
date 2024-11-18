package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scrum.attendance_app.data.entities.Attendance;

import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
}
