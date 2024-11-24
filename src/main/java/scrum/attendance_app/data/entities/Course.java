package scrum.attendance_app.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.Validate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "course_t")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 250, nullable = false)
    private String description;

    @Column(name = "code", length = 8, nullable = false, unique = true)
    private String code;

    @Transient
    private boolean isDefinitiveCode = false;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "professor_ID")
    private Professor professor;

    public Course(CourseBuilder builder) {
        Validate.notNull(builder.name);
        Validate.isTrue(builder.name.matches("[a-zA-Z]+"));
        Validate.isTrue(builder.name.length() == 14);

        Validate.notNull(builder.description);
        Validate.isTrue(builder.description.length() == 100);

        Validate.notNull(builder.professor);
        Validate.isTrue(builder.professor.getEmail().matches("[a-zA-Z]+@[a-z]/.it"));

        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.professor = builder.professor;
        this.code = builder.code;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", professorOwner=" + /*professorOwner*/ +
                '}';
    }

    public void lockCourseCode(){
        if(!this.isDefinitiveCode){
            this.isDefinitiveCode = true;
        }
    }

    public void generateCourseCode(){
        if (!this.isDefinitiveCode){
            StringBuilder sb = new StringBuilder();
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#@+*^?!_-";

            for (int i = 0; i < 8; i++) {
                int index = (int) (Math.random() * characters.length());
                sb.append(characters.charAt(index));
            }

            this.code = sb.toString();
        }
    }

    public String showCourseCode(){
        return new String(this.code);
    }

}
