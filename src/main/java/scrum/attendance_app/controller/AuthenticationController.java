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
        String registrationStatus = registrationService.registerStudent(studentDTO);
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
    public void authenticate(@RequestParam("username") String email, @RequestParam("password") String password, HttpServletResponse response) throws JOSEException, JOSEException {
        try {
            System.out.println("Inizio autenticazione per l'utente: " + email);

            // Autenticazione dell'utente
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            // Determina il ruolo dell'utente in base all'email usando UserDetailsService
            String role = null;

            // Carica i dettagli dell'utente
            System.out.println("Carico i dettagli dell'utente da UserDetailsService per: " + email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Verifica il ruolo in base al tipo di utente
            System.out.println("Dettagli dell'utente: " + userDetails.getUsername());
            userDetails.getAuthorities().forEach(authority -> System.out.println("Autorità trovata: " + authority.getAuthority()));

            if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STUDENT"))) {
                role = "studente";
                System.out.println("Ruolo dell'utente: studente");
            } else if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_PROFESSOR"))) {
                role = "professore";
                System.out.println("Ruolo dell'utente: professore");
            } else {
                throw new RuntimeException("Ruolo utente non riconosciuto.");
            }

            // Creazione del token JWT con il ruolo dell'utente
            try {
                System.out.println("Creazione del token JWT per il ruolo: " + role);
                String token = TokenStore.getInstance().createToken(Map.of("email", email, "role", role));
                System.out.println(token);

                // Aggiungi il token JWT nell'header della risposta
                response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                System.out.println("Token JWT generato con successo.");

            } catch (Exception e) {
                throw new RuntimeException("Errore nella creazione del token: " + e.getMessage());
            }

        } catch (UsernameNotFoundException e) {
            System.out.println("Errore: utente non trovato con email: " + email);
            throw new RuntimeException("Utente non trovato: " + email);
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Errore di autenticazione per l'utente: " + email + " - Dettaglio errore: " + e.getMessage());
            throw new RuntimeException("Credenziali errate per l'utente: " + email + " - Errore: " + e.getMessage());
        }
    }

    @PostMapping(path = "/authenticate2")
    @ResponseStatus(HttpStatus.OK)
    public void authenticate2(@RequestParam("username") String email, @RequestParam("password") String password, HttpServletResponse response) throws JOSEException, JOSEException {
        Optional<Student> studentByEmail = registrationService.getStudentByEmail(email);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        System.out.println("ciao");
    }


}
