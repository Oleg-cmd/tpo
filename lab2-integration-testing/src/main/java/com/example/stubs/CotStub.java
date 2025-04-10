package com.example.stubs;

import com.example.interfaces.ICot;
import java.util.HashMap;
import java.util.Map;

public class CotStub implements ICot {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public CotStub() {
    values.put(0.0, Double.NaN); // Деление на 0
    values.put(Math.PI / 6, Math.sqrt(3));
    values.put(Math.PI / 4, 1.0);
    values.put(Math.PI / 2, 0.0);
    values.put(Math.PI, Double.NaN); // Деление на 0
    values.put((3 * Math.PI) / 2, 0.0);
    values.put(2 * Math.PI, Double.NaN); // Деление на 0
    // Отрицательные
    values.put(-Math.PI / 6, -Math.sqrt(3));
    values.put(-Math.PI / 4, -1.0);
    values.put(-Math.PI / 2, 0.0);
    values.put(-Math.PI, Double.NaN);
    values.put((-3 * Math.PI) / 2, 0.0);
    values.put(-2 * Math.PI, Double.NaN);
  }

  @Override
  public double calculate(double x, double precision) {
    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    // System.out.println("Warning: Stub value not found for Cot(" + x + ")");
    return Double.NaN;
  }
}
