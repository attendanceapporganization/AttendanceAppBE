package scrum.attendance_app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scrum.attendance_app.data.entities.*;
import scrum.attendance_app.repository.*;

import java.util.Optional;
import java.util.UUID;

@Service
public class LectureCodeService {

    @Autowired
    private DigitCodeRepository codeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    public boolean registerAttendance(UUID studentId, String code) {

        UUID codeUuid;
//        try {
//            codeUuid = UUID.fromString(code);
//        } catch (IllegalArgumentException e) {
//            return false;
//        }



        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            return false;
        }
        Optional<Registration> registrationOpt = registrationRepository.findByStudent_Id(studentId);
        if (registrationOpt.isEmpty()) {
            return false;
        }
        Optional<DigitCode> lectureCodeOpt = codeRepository.findByNumericValue(Integer.valueOf(code));
        if (lectureCodeOpt.isEmpty()) {
            return false;
        }

        DigitCode lectureCode = lectureCodeOpt.get();
        Student student = studentOpt.get();
        // Controlla se è già presente un record di attendance per questo studente e codice
//        boolean alreadyPresent = attendanceRepository.existsByStudentAndLectureCode(studentId, lectureCodeOpt.get().getDigitCodeId());
//        if (alreadyPresent) {
//            return false;
//        }

        Optional<Lesson> lessonOpt = lessonRepository.findByDigitCode_DigitCodeId(lectureCode.getDigitCodeId());
        if (registrationOpt.isEmpty()) {
            return false;
        }

        Attendance attendance = Attendance.builder()
                .registration(registrationOpt.get())
                .attendanceDate(java.time.LocalDate.now())
                .lesson(lessonOpt.get())
                .build();

        attendanceRepository.save(attendance);
        return true;
    }
}
