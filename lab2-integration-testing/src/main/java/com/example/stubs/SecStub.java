package com.example.stubs;

import com.example.interfaces.ISec;
import java.util.HashMap;
import java.util.Map;

public class SecStub implements ISec {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public SecStub() {
    values.put(0.0, 1.0);
    values.put(Math.PI / 4, Math.sqrt(2));
    values.put(Math.PI / 3, 2.0);
    values.put(Math.PI / 2, Double.NaN); // Деление на 0
    values.put(Math.PI, -1.0);
    values.put((3 * Math.PI) / 2, Double.NaN); // Деление на 0
    values.put(2 * Math.PI, 1.0);
    // Отрицательные
    values.put(-Math.PI / 4, Math.sqrt(2));
    values.put(-Math.PI / 2, Double.NaN);
    values.put(-Math.PI, -1.0);
    values.put((-3 * Math.PI) / 2, Double.NaN);
    values.put(-2 * Math.PI, 1.0);
  }

  @Override
  public double calculate(double x, double precision) {
    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    // System.out.println("Warning: Stub value not found for Sec(" + x + ")");
    return Double.NaN;
  }
}
