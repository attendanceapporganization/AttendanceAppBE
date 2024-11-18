package scrum.attendance_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import scrum.attendance_app.Service.RegistrationService;
import scrum.attendance_app.data.dto.ProfessorDTO;
import scrum.attendance_app.data.dto.StudentDTO;
import scrum.attendance_app.data.entities.Student;
import scrum.attendance_app.error_handling.exceptions.InvalidRoleException;
import scrum.attendance_app.error_handling.exceptions.TokenCreationException;
import scrum.attendance_app.security.TokenStore;
import scrum.attendance_app.security.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthenticationController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    public AuthenticationController( PasswordEncoder passwordEncoder, RegistrationService registrationService, AuthenticationManager authenticationManager ) {
        this.passwordEncoder = passwordEncoder;
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping(path = "/registerStudent")
    public ResponseEntity<String> registerStudent(@RequestBody StudentDTO studentDTO) {
        registrationService.registerStudent(studentDTO);
        return new ResponseEntity<>("Registered successfully", HttpStatus.OK);
    }

    @PostMapping(path = "/registerProfessor")
        public ResponseEntity<String> registerProfessor(@RequestBody ProfessorDTO professorDTO) {

        String registrationStatus = registrationService.registerProfessor(professorDTO);

        if ("existing email".equals(registrationStatus)) {
            return new ResponseEntity<>("Existing email", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Registered successfully", HttpStatus.OK);
    }

    @PostMapping(path = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public void authenticate(@RequestParam("username") String email, @RequestParam("password") String password, HttpServletResponse response) throws JOSEException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String role = null;
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STUDENT"))) {
                role = "studente";
            } else if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_PROFESSOR"))) {
                role = "professore";
            } else {
                throw new InvalidRoleException("Ruolo utente non riconosciuto.");
            }
            try {
                String token = TokenStore.getInstance().createToken(Map.of("email", email, "role", role));
                response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            } catch (Exception e) {
                throw new TokenCreationException("Errore nella creazione del token: " + e.getMessage());
            }

        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Utente non trovato: " + email);
        } catch (RuntimeException | TokenCreationException e) {
            e.printStackTrace();
            throw new RuntimeException("Credenziali errate per l'utente: " + email + " - Errore: " + e.getMessage());
        }
    }
}
