package com.example.stubs;

import com.example.interfaces.ISin;
import java.util.HashMap;
import java.util.Map;

public class SinStub implements ISin {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6; // Допуск для сравнения double

  public SinStub() {
    // Заполняем табличными значениями
    values.put(0.0, 0.0);
    values.put(Math.PI / 6, 0.5);
    values.put(Math.PI / 4, Math.sqrt(2) / 2.0);
    values.put(Math.PI / 2, 1.0);
    values.put(Math.PI, 0.0);
    values.put((3 * Math.PI) / 2, -1.0);
    values.put(2 * Math.PI, 0.0);
    // Отрицательные значения
    values.put(-Math.PI / 6, -0.5);
    values.put(-Math.PI / 4, -Math.sqrt(2) / 2.0);
    values.put(-Math.PI / 2, -1.0);
    values.put(-Math.PI, 0.0);
    values.put((-3 * Math.PI) / 2, 1.0);
    values.put(-2 * Math.PI, 0.0);
    // Значения для проверки Cot, Csc (где sin=0)
    values.put(Math.nextDown(0.0), 0.0); // Явно добавляем точки, где sin=0
    values.put(Math.nextUp(0.0), 0.0);
    values.put(Math.nextDown(Math.PI), 0.0);
    values.put(Math.nextUp(Math.PI), 0.0);
    values.put(Math.nextDown(-Math.PI), 0.0);
    values.put(Math.nextUp(-Math.PI), 0.0);
  }

  @Override
  public double calculate(double x, double precision) {
    // Ищем значение в карте с учетом допуска
    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    // Если точное значение не найдено, можно вернуть NaN или какое-то значение по умолчанию
    // System.out.println("Warning: Stub value not found for Sin(" + x + ")");
    return Double.NaN; // Или return 0.0; или throw new UnsupportedOperationException();
  }
}
