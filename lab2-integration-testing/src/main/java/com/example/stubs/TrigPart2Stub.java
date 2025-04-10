package com.example.stubs;

import com.example.interfaces.ITrigPart2;
import java.util.HashMap;
import java.util.Map;

public class TrigPart2Stub implements ITrigPart2 {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public TrigPart2Stub() {
    // x = -PI/4: sec = sqrt(2), csc = -sqrt(2)
    // (sqrt(2) - (-sqrt(2)))^2 = (2*sqrt(2))^2 = 8
    values.put(-Math.PI / 4, 8.0);
    // x = -PI/6: sec=2/sqrt(3), csc=-2
    // (2/sqrt(3) - (-2))^2 = (2/sqrt(3) + 2)^2 = ( (2 + 2*sqrt(3)) / sqrt(3) )^2
    // = (4 * (1+sqrt(3))^2) / 3 = (4 * (1 + 2*sqrt(3) + 3)) / 3 = (4 * (4 + 2*sqrt(3))) / 3 = (8 * (2+sqrt(3)))/3
    values.put(-Math.PI / 6, (8.0 * (2.0 + Math.sqrt(3))) / 3.0); // approx 9.9540
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
