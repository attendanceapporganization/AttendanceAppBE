package scrum.attendance_app.Service;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scrum.attendance_app.data.dto.CourseDTO;
import scrum.attendance_app.data.dto.LessonDTO;
import scrum.attendance_app.data.dto.StudentDTO;
import scrum.attendance_app.data.entities.*;
import scrum.attendance_app.error_handling.exceptions.CourseNotFoundException;
import scrum.attendance_app.error_handling.exceptions.LessonNotFoundException;
import scrum.attendance_app.mapper.LessonMapper;
import scrum.attendance_app.mapper.StudentMapper;
import scrum.attendance_app.repository.AttendanceRepository;
import scrum.attendance_app.repository.CourseRepository;
import scrum.attendance_app.repository.LessonRepository;
import scrum.attendance_app.repository.ProfessorRepository;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class ProfessorService {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final StudentMapper studentMapper;
    private final LessonMapper lessonMapper;

    // This method find the first course that have as name String name and as professor String professorEmail
    private Course alreadyHasThisCourse(String name, String professorEmail){
        if(!courseRepository.findByCourseNameLowercase(name).isEmpty()) {
            List<Course> coursesWithSameName = courseRepository.findByCourseNameLowercase(name);
            for (Course course : coursesWithSameName) {
                if (course.getProfessor().getEmail().equals(professorEmail)) { return course; }
            }
        }

        return null;
    }

    // Convert the given CourseDTO in Course
    private Course converteToCourse(CourseDTO courseDTO){
        String email = courseDTO.getProfessorOwner();
        Professor professor = (professorRepository.findByEmail(email)).get();

        return Course.builder()
                .name(courseDTO.getName())
                .description(courseDTO.getDescription())
                .professor(professor)
                .build();
    }

    private void assignCourseCode(Course course){
        course.generateCourseCode();
        while (course.isDefinitiveCode()){
            if(!courseRepository.existsByCourseCode(course.showCourseCode())){
                course.lockCourseCode();
            }
        }
    }

    // Create the course with given courseDTO
    public String createCourse(CourseDTO courseDTO) {
        if (professorRepository.findByEmail(courseDTO.getProfessorOwner()).isEmpty()) {
            return "Professor does not exist";
        }
        if (alreadyHasThisCourse(courseDTO.getName(), courseDTO.getProfessorOwner()) != null) {
            return "Course already exists";
        }
        try {
            Course course = converteToCourse(courseDTO);
            assignCourseCode(course);
            courseRepository.save(course);
            return "Created";
        }
        catch (PersistenceException e) {
            return "Course creation failed due to persistence error: " + e.getMessage();
        }
    }

    // Delete the course with given course name and professor email
    public String deleteCourse(String name, String professorEmail) {
        if (professorRepository.findByEmail(professorEmail).isEmpty()) {
            return "Professor does not exist";
        }
        if(alreadyHasThisCourse(name, professorEmail) == null){
            return "Course not found";
        }
        try {
            courseRepository.delete(Objects.requireNonNull(alreadyHasThisCourse(name, professorEmail)));
            return "Deleted";
        }
        catch (PersistenceException e) {
            return "Course deletion failed due to persistence error: " + e.getMessage();
        }
    }

    public String editCourse(String name, String professorEmail, String newName, String newDescription) {
        Course course = alreadyHasThisCourse(name, professorEmail);

        if(course == null){
            return "Course not found";
        }

        try {
            if (!newName.equals(course.getName())) {
                if(alreadyHasThisCourse(newName, professorEmail) == null){
                    course.setName(newName);
                }
                else{
                    return "Course already exists";
                }
            }
            if (!newDescription.equals(course.getDescription())) {
                course.setDescription(newDescription);
            }

            courseRepository.save(course);

            return "Edited";
        }
        catch (PersistenceException e) {
            return "Course edit failed due to persistence error: " + e.getMessage();
        }
    }

    public Course retrieveCourse(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
    }

    public LessonDTO createLesson(Lesson lesson) {
        return lessonMapper.toDto(lessonRepository.save(lesson));
    }

    public Lesson retrieveLesson(UUID lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(LessonNotFoundException::new);
    }

    public List<CourseDTO> getListCourse(String user) {
        Professor professor = professorRepository.findByEmail(user)
                .orElseThrow(() -> new IllegalArgumentException("Professore non trovato"));

        List<Course> corsi = courseRepository.findByProfessorId(professor);

        List<CourseDTO> courseDTOList = new ArrayList<>();
        for (Course course : corsi) {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setId(course.getId());
            courseDTO.setName(course.getName());
            courseDTO.setDescription(course.getDescription());
            courseDTO.setCode(course.getCode());
            courseDTOList.add(courseDTO);
        }

        return courseDTOList;
    }


    public List<StudentDTO> getStudentsAttending(UUID lessonId) {
        return attendanceRepository.findAllByLessonId(lessonId).stream().map(attendance ->
             studentMapper.toDTO(attendance.getRegistration().getStudent())).toList();
    }

    public void terminateLessonOfCourse(Course course) {
        Lesson lastLesson = lessonRepository.findLessonWithMaxStartDateByCourse(course).orElseThrow(LessonNotFoundException::new);
        lastLesson.setEndDate(Date.from(Instant.now()));
        lessonRepository.save(lastLesson);
    }


    public List<LessonDTO> getListLesson(String user, UUID courseId) {
        Professor professor = professorRepository.findByEmail(user)
                .orElseThrow(() -> new IllegalArgumentException("Professore non trovato"));



        List<Lesson> lezioni = lessonRepository.findByCourseId(courseId);

        List<LessonDTO> lessonDTOList = new ArrayList<>();
        for (Lesson lesson : lezioni) {
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO.setId(lesson.getId());
            lessonDTO.setStartDate(lesson.getStartDate());
            lessonDTO.setEndDate(lesson.getEndDate());
            lessonDTOList.add(lessonDTO);
        }

        return lessonDTOList;
    }
}
