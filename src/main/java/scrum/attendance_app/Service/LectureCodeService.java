package scrum.attendance_app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scrum.attendance_app.data.entities.*;
import scrum.attendance_app.error_handling.exceptions.*;
import scrum.attendance_app.repository.*;

import java.util.Optional;
import java.util.UUID;

@Service
public class LectureCodeService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    public void registerAttendance(UUID studentId, String code, UUID courseId) throws DataNotFoundException, NoOngoingLectureException, WrongAttendanceCodeException {
        Optional<Lesson> onGoingLecture = lessonRepository.findByCourseIdAndEndDateNull(courseId);
        if (onGoingLecture.isEmpty())
            throw new NoOngoingLectureException();

        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty())
            throw new StudentNotFoundException();

        Optional<Registration> registration = registrationRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (registration.isEmpty())
            throw new RegistrationNotFoundException();



        DigitCode lectureCode = onGoingLecture.get().getDigitCode();
        if (lectureCode != null)
            if (lectureCode.formattedValue().equalsIgnoreCase(code)){
                attendanceRepository.save(Attendance.builder()
                        .registration(registration.get())
                        .lesson(onGoingLecture.get())
                        .build());
            }
            else
                throw new WrongAttendanceCodeException();
    }
}
