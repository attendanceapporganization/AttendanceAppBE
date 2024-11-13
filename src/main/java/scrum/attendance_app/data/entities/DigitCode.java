package scrum.attendance_app.data.entities;

import jakarta.persistence.*;

import java.util.Random;
import java.util.UUID;

//create table digit_code_t (numeric_value integer, digit_code_id uuid not null, primary key (digit_code_id))
@Entity
@Table(name = "digitCode_t")
public class DigitCode {

    final static String stringFormat = "%1$04d";
    public final static Integer minValue = 0;
    public final static Integer maxValue = 9999;
    final static Random rand = new Random(System.currentTimeMillis());

    @Column(name = "numericValue")
    final Integer numericValue;

    @Id
    @Column(name = "digitCodeId")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID digitCodeId;

    private DigitCode(Integer actualValue){
        this.numericValue=actualValue;
    }

    public static DigitCode createDigitCode(){
        return new DigitCode(rand.nextInt(minValue, maxValue));
    }

    public String formattedValue(){
        return String.format(stringFormat, numericValue);
    }

    public boolean match(String givenCode){
        return Integer.parseInt(givenCode) == numericValue;
    }
}
