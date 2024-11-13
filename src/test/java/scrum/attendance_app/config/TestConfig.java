package scrum.attendance_app.config;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import scrum.attendance_app.security.UserDetailsServiceImpl;

@TestConfiguration
public class TestConfig {

    @Bean
    UserDetailsServiceImpl userDetailsService(){
        return new UserDetailsServiceImpl();
    }
}
