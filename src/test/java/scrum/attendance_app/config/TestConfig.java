package scrum.attendance_app.config;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import scrum.attendance_app.mapper.LessonMapper;
import scrum.attendance_app.mapper.StudentMapper;
import scrum.attendance_app.security.UserDetailsServiceImpl;

@TestConfiguration
@ComponentScan(basePackageClasses = {StudentMapper.class, LessonMapper.class})
public class TestConfig {

    @Bean
    UserDetailsServiceImpl userDetailsService(){
        return new UserDetailsServiceImpl();
    }
}
