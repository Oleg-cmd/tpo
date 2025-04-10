package com.example.stubs;

import com.example.interfaces.ILn;
import java.util.HashMap;
import java.util.Map;

public class LnStub implements ILn {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public LnStub() {
    values.put(1.0, 0.0);
    values.put(Math.E, 1.0);
    values.put(Math.exp(2), 2.0);
    values.put(0.5, -0.6931471805599453); // Примерное значение ln(0.5)
    values.put(2.0, 0.6931471805599453); // Примерное значение ln(2)
    values.put(5.0, 1.6094379124341003); // ln(5)
    values.put(10.0, 2.302585092994046); // ln(10)
    // Граничные случаи
    values.put(0.0, Double.NaN);
    values.put(Math.nextUp(0.0), Double.NEGATIVE_INFINITY); // Предел справа от 0
    values.put(-1.0, Double.NaN); // Не определен для отрицательных
    values.put(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
  }

  @Override
  public double calculate(double x, double precision) {
    if (x < 0) return Double.NaN; // Ln не определен для x<0
    if (x == 0) return Double.NaN; // Ln(0) не определен (или -Infinity как предел)

    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    // System.out.println("Warning: Stub value not found for Ln(" + x + ")");
    return Double.NaN;
  }
}
