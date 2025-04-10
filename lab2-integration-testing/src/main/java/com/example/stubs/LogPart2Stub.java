package com.example.stubs;

import com.example.interfaces.ILogPart2;
import java.util.HashMap;
import java.util.Map;

public class LogPart2Stub implements ILogPart2 {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public LogPart2Stub() {
    // x = 1: log10=0, log2=0, log5=0. (0 / 0) + 0 -> NaN
    values.put(1.0, Double.NaN);
    // x = 2: log10(2)/log2(2) + log5(2) = log10(2)/1 + log5(2)
    double log10_2 = 0.3010299956639812;
    double log5_2 = 0.43067655807339306;
    values.put(2.0, log10_2 + log5_2); // approx 0.7317
    // x = 5: log10(5)/log2(5) + log5(5) = log10(5)/log2(5) + 1
    double log10_5 = 0.6989700043360188;
    double log2_5 = 2.321928094887362;
    values.put(5.0, log10_5 / log2_5 + 1.0); // approx 0.3010 + 1 = 1.3010
    // x = 10: log10(10)/log2(10) + log5(10) = 1/log2(10) + log5(10)
    double log2_10 = 3.321928094887362;
    double log5_10 = 1.430676558073393;
    values.put(10.0, 1.0 / log2_10 + log5_10); // approx 0.3010 + 1.4307 = 1.7317
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
