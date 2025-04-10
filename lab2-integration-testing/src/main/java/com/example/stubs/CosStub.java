package com.example.stubs;

import com.example.interfaces.ICos;
import java.util.HashMap;
import java.util.Map;

public class CosStub implements ICos {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public CosStub() {
    values.put(0.0, 1.0);
    values.put(Math.PI / 4, Math.sqrt(2) / 2.0);
    values.put(Math.PI / 3, 0.5);
    values.put(Math.PI / 2, 0.0);
    values.put(Math.PI, -1.0);
    values.put((3 * Math.PI) / 2, 0.0);
    values.put(2 * Math.PI, 1.0);
    // Отрицательные (cos - четная функция)
    values.put(-Math.PI / 4, Math.sqrt(2) / 2.0);
    values.put(-Math.PI / 2, 0.0);
    values.put(-Math.PI, -1.0);
    values.put((-3 * Math.PI) / 2, 0.0);
    values.put(-2 * Math.PI, 1.0);
    // Значения для проверки Sec, Cot (где cos=0)
    values.put(Math.nextDown(Math.PI / 2), 0.0);
    values.put(Math.nextUp(Math.PI / 2), 0.0);
    values.put(Math.nextDown(-Math.PI / 2), 0.0);
    values.put(Math.nextUp(-Math.PI / 2), 0.0);
  }

  @Override
  public double calculate(double x, double precision) {
    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    // System.out.println("Warning: Stub value not found for Cos(" + x + ")");
    return Double.NaN;
  }
}
