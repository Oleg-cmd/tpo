package com.example.stubs;

import com.example.interfaces.ITrigPart1;
import java.util.HashMap;
import java.util.Map;

public class TrigPart1Stub implements ITrigPart1 {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public TrigPart1Stub() {
    // x = -PI/4: sec = sqrt(2), csc = -sqrt(2), cot = -1
    // ((sqrt(2))^2 - (-sqrt(2))) / (-1) = (2 + sqrt(2)) / -1 = -2 - sqrt(2)
    values.put(-Math.PI / 4, -2.0 - Math.sqrt(2)); // approx -3.4142
    // x = -PI/6: sec=2/sqrt(3), csc=-2, cot=-sqrt(3)
    // ((2/sqrt(3))^2 - (-2)) / (-sqrt(3)) = (4/3 + 2) / (-sqrt(3)) = (10/3)/(-sqrt(3)) = -10 / (3*sqrt(3))
    values.put(-Math.PI / 6, -10.0 / (3 * Math.sqrt(3))); // approx -1.9245
    // Точки разрыва базовых функций
    values.put(0.0, Double.NaN);
    values.put(-Math.PI / 2, Double.NaN);
    values.put(-Math.PI, Double.NaN);
  }

  @Override
  public double calculate(double x, double precision) {
    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    return Double.NaN;
  }
}
