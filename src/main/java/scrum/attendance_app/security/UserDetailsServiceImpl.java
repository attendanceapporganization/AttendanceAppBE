package scrum.attendance_app.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import scrum.attendance_app.data.entities.Professor;
import scrum.attendance_app.data.entities.Student;
import scrum.attendance_app.repository.ProfessorRepository;
import scrum.attendance_app.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Controlla se l'utente è uno studente
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            // Crea l'utente con il ruolo "ROLE_STUDENT"
            return new User(email, student.get().getPassword(), List.of(new SimpleGrantedAuthority("ROLE_STUDENT")));
        }

        // Controlla se l'utente è un professore
        Optional<Professor> professor = professorRepository.findByEmail(email);
        if (professor.isPresent()) {
            // Crea l'utente con il ruolo "ROLE_PROFESSOR"
            return new User(email, professor.get().getPassword(), List.of(new SimpleGrantedAuthority("ROLE_PROFESSOR")));
        }

        // Se l'utente non è né uno studente né un professore, lancia un'eccezione
        throw new UsernameNotFoundException("User not found: " + email);
    }

}