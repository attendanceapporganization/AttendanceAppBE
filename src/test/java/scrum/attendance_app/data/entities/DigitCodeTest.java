package scrum.attendance_app.data.entities;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class DigitCodeTest {
    
    @Test
    void testRage(){
        DigitCode digitCode = DigitCode.createDigitCode();
        assertThat(digitCode.numericValue).isGreaterThanOrEqualTo(DigitCode.minValue).isLessThanOrEqualTo(DigitCode.maxValue);
    }

    @Test
    void testMatch(){
        DigitCode digitCode = DigitCode.createDigitCode();
        assertThat(digitCode.match(digitCode.formattedValue())).isTrue();
    }

    @Test
    void testMatchFalse(){
        Integer negativeNumber = -1;
        DigitCode digitCode = DigitCode.createDigitCode();
        assertThat(digitCode.match(negativeNumber.toString())).isFalse();
    }
}
