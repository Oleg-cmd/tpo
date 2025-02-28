package lab1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SecantTest {

    private final double input;
    private final double delta;
    private final int terms;
    private final boolean isNearAsymptoteCheck;

    public SecantTest(double input, double delta, int terms, boolean isNearAsymptoteCheck) {
        this.input = input;
        this.delta = delta;
        this.terms = terms;
        this.isNearAsymptoteCheck = isNearAsymptoteCheck;
    }

    @Parameters(name = "sec({0}, terms={2}) (delta={1}, nearAsymptote={3})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Стандартные значения
                {0, 1e-6, 10, false},
                {Math.PI/4, 1e-6, 50, false},
                {-Math.PI/4, 1e-6, 50, false},
                {Math.PI/3, 1e-6, 15, false},
                
                // Близко к асимптотам (в пределах tolerance 1e-4)
                {Math.PI/2 - 9e-5, 0, 200, true},  // ~1.5707 (PASS)
                {Math.PI/2 + 9e-5, 0, 200, true},  // ~1.5709 (PASS)
                {3*Math.PI/2 - 9e-5, 0, 200, true}, // ~4.7123 (PASS)
                {3*Math.PI/2 + 9e-5, 0, 200, true}, // ~4.7125 (PASS)
                
                // Значения между асимптотами
                {1.0, 1e-6, 50, false},
                {2.0, 1e-6, 50, false},
                {4.0, 1e-6, 50, false},
                
                // Граничные случаи
                {Math.PI, 1e-6, 50, false},
                {2*Math.PI, 1e-6, 50, false},
                {-Math.PI/2 + 1e-3, 1e-6, 200, false}, // вне зоны асимптоты
                
                // Большие углы
                {1000, 1e-6, 50, false},
                {-1000, 1e-6, 50, false},
                
                // Точки чуть дальше tolerance
                {Math.PI/2 - 2e-4, 1e-6, 200, false}, // вне зоны асимптоты
                {3*Math.PI/2 + 2e-4, 1e-6, 200, false}
        });
    }

    @Test
    public void testSecant() {
        double result = Secant.sec(input, terms);
        
        if (isNearAsymptoteCheck) {
            // Проверяем выход за порог 1e6 по модулю
            assertTrue("Near asymptote: " + input + ", result: " + result,
                      Math.abs(result) > 1e6);
        } else {
            // Сравниваем с эталонным значением через Math.cos
            double expected = 1.0 / Math.cos(input);
            assertEquals("sec(" + input + ")", 
                        expected, 
                        result, 
                        delta);
        }
    }

    // Дополнительные тесты для особых случаев
    @Test
    public void testExactAsymptotes() {
        double[] asymptotes = {
            Math.PI/2, 
            3*Math.PI/2, 
            -Math.PI/2, 
            5*Math.PI/2
        };
        
        for (double x : asymptotes) {
            double result = Secant.sec(x, 200);
            assertTrue("Exact asymptote: " + x + ", result: " + result,
                      Math.abs(result) > 1e6);
        }
    }

    @Test
    public void testPeriodicity() {
        double x = 1.234;
        double sec1 = Secant.sec(x, 50);
        double sec2 = Secant.sec(x + 2*Math.PI, 50);
        assertEquals(sec1, sec2, 1e-6);
    }
}