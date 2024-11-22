package scrum.attendance_app.Service;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import scrum.attendance_app.data.dto.ProfessorDTO;
import scrum.attendance_app.data.dto.StudentDTO;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Registry;
import scrum.attendance_app.data.entities.Student;
import scrum.attendance_app.error_handling.exceptions.ExistingEmailException;
import scrum.attendance_app.repository.ProfessorRepository;
import scrum.attendance_app.repository.StudentRepository;

import java.util.Optional;


@Service
public class RegistrationService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ProfessorRepository professorRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<Student> getStudentByEmail(String email){
        return studentRepository.findByEmail(email);
    }

    public String registerStudent(StudentDTO studentDTO) {
        if (studentRepository.findByEmail(studentDTO.getEmail()).isPresent())
            throw new ExistingEmailException(studentDTO.getEmail());

        try{
            Registry registry = Registry.builder()
                    .firstname(studentDTO.getFirstname())
                    .lastname(studentDTO.getLastname())
                    .dateOfBirth(studentDTO.getDateOfBirth())
                    .build();
            Student student = Student.builder()
                    .id(registry.getId())
                    .email(studentDTO.getEmail())
                    .password(passwordEncoder.encode(studentDTO.getPassword()))
                    .registrationNumber(studentDTO.getRegistrationNumber())
                    .registry(registry).build();
            studentRepository.save(student);
            return "registered";
        } catch (PersistenceException e) {
            e.printStackTrace();
            return "registration failed due to persistence error: " + e.getMessage();
        } catch (Exception e) {
            //  eccezioni generiche
            e.printStackTrace();
            return "registration failed due to unexpected error: " + e.getMessage();
        }
    }
    public String registerProfessor(ProfessorDTO professorDTO) {
        try {
            if (professorRepository.findByEmail(professorDTO.getEmail()).isPresent())
                return "existing email";
            Registry registry = Registry.builder()
                    .firstname(professorDTO.getFirstname())
                    .lastname(professorDTO.getLastname())
                    .dateOfBirth(professorDTO.getDateOfBirth())
                    .build();
            Professor professor = Professor.builder()
                    .email(professorDTO.getEmail())
                    .password(passwordEncoder.encode(professorDTO.getPassword()))
                    .registry(registry)
                    .build();
            professorRepository.save(professor);
            return "registered";
        } catch (PersistenceException e) {
            e.printStackTrace();
            return "registration failed due to persistence error: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "registration failed due to unexpected error: " + e.getMessage();
        }
    }
}
