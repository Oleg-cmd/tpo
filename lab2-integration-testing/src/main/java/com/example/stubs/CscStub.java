package com.example.stubs;

import com.example.interfaces.ICsc;
import java.util.HashMap;
import java.util.Map;

public class CscStub implements ICsc {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public CscStub() {
    values.put(0.0, Double.NaN); // Деление на 0
    values.put(Math.PI / 6, 2.0);
    values.put(Math.PI / 4, Math.sqrt(2));
    values.put(Math.PI / 2, 1.0);
    values.put(Math.PI, Double.NaN); // Деление на 0
    values.put((3 * Math.PI) / 2, -1.0);
    values.put(2 * Math.PI, Double.NaN); // Деление на 0
    // Отрицательные
    values.put(-Math.PI / 6, -2.0);
    values.put(-Math.PI / 4, -Math.sqrt(2));
    values.put(-Math.PI / 2, -1.0);
    values.put(-Math.PI, Double.NaN);
    values.put((-3 * Math.PI) / 2, 1.0);
    values.put(-2 * Math.PI, Double.NaN);
  }

  @Override
  public double calculate(double x, double precision) {
    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    // System.out.println("Warning: Stub value not found for Csc(" + x + ")");
    return Double.NaN;
  }
}
