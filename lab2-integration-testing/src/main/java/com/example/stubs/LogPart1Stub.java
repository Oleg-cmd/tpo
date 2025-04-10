package com.example.stubs;

import com.example.interfaces.ILogPart1;
import java.util.HashMap;
import java.util.Map;

public class LogPart1Stub implements ILogPart1 {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public LogPart1Stub() {
    // x = 1: log2=0, log5=0. (0*0)^18 = 0
    values.put(1.0, 0.0);
    // x = 2: log2=1, log5=log5(2). (1 * log5(2))^18 = (log5(2))^18
    double log5_2 = 0.43067655807339306;
    values.put(2.0, Math.pow(log5_2, 18)); // very small number approx 1.86e-7
    // x = 5: log2=log2(5), log5=1. (log2(5) * 1)^18 = (log2(5))^18
    double log2_5 = 2.321928094887362;
    values.put(5.0, Math.pow(log2_5, 18)); // large number approx 4.36e6
    // x = 10: log2=log2(10), log5=log5(10). (log2(10)*log5(10))^18
    double log2_10 = 3.321928094887362;
    double log5_10 = 1.430676558073393;
    values.put(10.0, Math.pow(log2_10 * log5_10, 18)); // approx 4.75^18 -> large
    // Граничные
    values.put(0.0, Double.NaN);
    values.put(-1.0, Double.NaN);
  }

  @Override
  public double calculate(double x, double precision) {
    if (x <= 0) return Double.NaN;
    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    return Double.NaN;
  }
}
