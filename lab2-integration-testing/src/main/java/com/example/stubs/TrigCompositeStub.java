package com.example.stubs;

import com.example.interfaces.ITrigComposite;
import java.util.HashMap;
import java.util.Map;

public class TrigCompositeStub implements ITrigComposite {

  private final Map<Double, Double> values = new HashMap<>();
  private final double tolerance = 1E-6;

  public TrigCompositeStub() {
    // x = -PI/4: part1 = -2-sqrt(2), part2 = 8
    // (part1 * part2)^2 = ((-2-sqrt(2))*8)^2 = (-16 - 8*sqrt(2))^2
    // = (-(16+8*sqrt(2)))^2 = (16+8*sqrt(2))^2 = 64*(2+sqrt(2))^2 = 64*(4+4sqrt(2)+2) = 64*(6+4sqrt(2))
    // = 128*(3+2sqrt(2))
    values.put(-Math.PI / 4, 128.0 * (3.0 + 2.0 * Math.sqrt(2))); // approx 744.98
    // x = -PI/6: part1 = -10/(3*sqrt(3)), part2 = (8*(2+sqrt(3)))/3
    // (part1*part2)^2 = ( (-10/(3*sqrt(3))) * (8*(2+sqrt(3)))/3 )^2
    // = ( -80*(2+sqrt(3)) / (9*sqrt(3)) )^2
    double part1_pi6 = -10.0 / (3 * Math.sqrt(3));
    double part2_pi6 = (8.0 * (2.0 + Math.sqrt(3))) / 3.0;
    double product_pi6 = part1_pi6 * part2_pi6;
    values.put(-Math.PI / 6, product_pi6 * product_pi6); // approx 368.14
    // Точки разрыва
    values.put(0.0, Double.NaN);
    values.put(-Math.PI / 2, Double.NaN);
    values.put(-Math.PI, Double.NaN);
  }

  @Override
  public double calculate(double x, double precision) {
    for (Map.Entry<Double, Double> entry : values.entrySet()) {
      if (Math.abs(entry.getKey() - x) < tolerance) {
        return entry.getValue();
      }
    }
    return Double.NaN;
  }
}
