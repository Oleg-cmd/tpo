package com.example.stubs;

import com.example.interfaces.ILog;
import java.util.HashMap;
import java.util.Map;

// Эта заглушка имитирует LogN с КОНКРЕТНОЙ базой.
// Для каждой нужной базы (2, 5, 10) нужно будет создать свой экземпляр
// или сделать заглушку более умной, если потребуется.
// Проще создать отдельные экземпляры для тестов.
public class LogNStub implements ILog {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;
  private final double base; // Храним базу для информации

  public LogNStub(double base, Map<Double, Double> predefinedValues) {
    this.base = base;
    this.values.putAll(predefinedValues);
    // Добавляем стандартные точки для логарифмов
    values.putIfAbsent(1.0, 0.0);
    values.putIfAbsent(0.0, Double.NaN);
    values.putIfAbsent(-1.0, Double.NaN); // Любое отрицательное
  }

  // Статические фабричные методы для удобства создания заглушек для нужных баз
  public static LogNStub createLog2Stub() {
    Map<Double, Double> log2Values = new HashMap<>();
    log2Values.put(2.0, 1.0);
    log2Values.put(4.0, 2.0);
    log2Values.put(8.0, 3.0);
    log2Values.put(0.5, -1.0);
    log2Values.put(1.0, 0.0); // Важно для LogPart2
    log2Values.put(5.0, 2.321928094887362); // log2(5)
    log2Values.put(10.0, 3.321928094887362); // log2(10)
    return new LogNStub(2.0, log2Values);
  }

  public static LogNStub createLog5Stub() {
    Map<Double, Double> log5Values = new HashMap<>();
    log5Values.put(5.0, 1.0);
    log5Values.put(25.0, 2.0);
    log5Values.put(0.2, -1.0);
    log5Values.put(1.0, 0.0);
    log5Values.put(2.0, 0.43067655807339306); // log5(2)
    log5Values.put(10.0, 1.430676558073393); // log5(10)
    return new LogNStub(5.0, log5Values);
  }

  public static LogNStub createLog10Stub() {
    Map<Double, Double> log10Values = new HashMap<>();
    log10Values.put(10.0, 1.0);
    log10Values.put(100.0, 2.0);
    log10Values.put(0.1, -1.0);
    log10Values.put(1.0, 0.0);
    log10Values.put(2.0, 0.3010299956639812); // log10(2)
    log10Values.put(5.0, 0.6989700043360188); // log10(5)
    return new LogNStub(10.0, log10Values);
  }

  @Override
  public double calculate(double x, double precision) {
    if (x < 0) return Double.NaN;
    if (x == 0) return Double.NaN;

    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    // System.out.println("Warning: Stub value not found for Log" + base + "(" + x + ")");
    return Double.NaN;
  }

  public double getBase() {
    return base;
  } // Для информации
}
