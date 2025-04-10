package com.example.stubs;

import com.example.interfaces.ILogComposite;
import java.util.HashMap;
import java.util.Map;

public class LogCompositeStub implements ILogComposite {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public LogCompositeStub() {
    // x = 1: part1=0, part2=NaN. -> NaN
    values.put(1.0, Double.NaN);
    // x = 2: part1=(log5(2))^18, part2=log10(2)+log5(2)
    double log5_2 = 0.43067655807339306;
    double log10_2 = 0.3010299956639812;
    double part1_2 = Math.pow(log5_2, 18);
    double part2_2 = log10_2 + log5_2;
    values.put(2.0, part1_2 + part2_2); // approx 0 + 0.7317 = 0.7317
    // x = 5: part1=(log2(5))^18, part2=log10(5)/log2(5) + 1
    double log2_5 = 2.321928094887362;
    double log10_5 = 0.6989700043360188;
    double part1_5 = Math.pow(log2_5, 18);
    double part2_5 = log10_5 / log2_5 + 1.0;
    values.put(5.0, part1_5 + part2_5); // approx 4.36e6 + 1.3010
    // x = 10: part1=(log2(10)*log5(10))^18, part2=1/log2(10) + log5(10)
    double log2_10 = 3.321928094887362;
    double log5_10 = 1.430676558073393;
    double part1_10 = Math.pow(log2_10 * log5_10, 18);
    double part2_10 = 1.0 / log2_10 + log5_10;
    values.put(10.0, part1_10 + part2_10); // Large number + ~1.7317
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
