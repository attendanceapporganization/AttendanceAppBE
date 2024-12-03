package scrum.attendance_app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scrum.attendance_app.data.entities.*;
import scrum.attendance_app.error_handling.exceptions.*;
import scrum.attendance_app.repository.*;

import java.util.Optional;

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

    @Autowired //to delete
    private CourseRepository courseRepository; //to delete

    @Transactional
    public void registerAttendance(String email, String code, String courseName) throws CourseNotFoundException, NoOngoingLectureException, WrongAttendanceCodeException {
        Optional<Course> courseToTakeAttendanceFor = courseRepository.findByName(courseName);
        if (courseToTakeAttendanceFor.isEmpty())
            throw new CourseNotFoundException();

        //UUID courseId = courseToTakeAttendanceFor.orElseThrow().getId(); //to delete, course id should be passed to this method
        //Optional<Lesson> onGoingLecture = lessonRepository.findByCourseIdAndEndDateNull(courseId); //this query should be used instead: every lesson must have an end date set
        Optional<Lesson> onGoingLecture = lessonRepository.findLessonWithMaxStartDateByCourse(courseToTakeAttendanceFor.get());
        if (onGoingLecture.isEmpty() || onGoingLecture.get().getEndDate() != null)
            throw new NoOngoingLectureException();

        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isEmpty())
            throw new StudentNotFoundException();

        Optional<Registration> registration = registrationRepository.findByStudentIdAndCourseId(studentOpt.get().getId(), courseToTakeAttendanceFor.get().getId());
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
            else {
                throw new WrongAttendanceCodeException();
            }
    }
}
