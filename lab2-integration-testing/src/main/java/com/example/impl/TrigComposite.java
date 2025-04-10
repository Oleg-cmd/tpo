package com.example.impl;

import com.example.interfaces.ITrigComposite;
import com.example.interfaces.ITrigPart1;
import com.example.interfaces.ITrigPart2;

public class TrigComposite implements ITrigComposite {

  private final ITrigPart1 part1;
  private final ITrigPart2 part2;

  public TrigComposite(ITrigPart1 part1, ITrigPart2 part2) {
    if (part1 == null || part2 == null) {
      throw new IllegalArgumentException(
        "TrigPart1 and TrigPart2 cannot be null"
      );
    }
    this.part1 = part1;
    this.part2 = part2;
  }

  @Override
  public double calculate(double x, double precision) {
    double val1 = part1.calculate(x, precision);
    double val2 = part2.calculate(x, precision);

    if (Double.isNaN(val1) || Double.isNaN(val2)) {
      return Double.NaN;
    }

    double product = val1 * val2;
    if (Double.isNaN(product)) { // Например, 0 * inf
      return Double.NaN;
    }

    // Возводим в квадрат
    return product * product;
  }
}
