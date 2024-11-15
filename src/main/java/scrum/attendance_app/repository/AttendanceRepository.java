package scrum.attendance_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import scrum.attendance_app.data.entities.Attendance;

import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

//    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
//            "FROM Attendance a " +
//            "JOIN a.registration r " +
//            "WHERE r.student.id = :studentId AND a.digitCode.numeric_value = :codeId")
//    boolean existsByStudentAndLectureCode(@Param("studentId") UUID studentId, @Param("codeId") UUID codeId);
}
