package com.example.impl;

import com.example.interfaces.ILogComposite;
import com.example.interfaces.ILogPart1;
import com.example.interfaces.ILogPart2;

public class LogComposite implements ILogComposite {

  private final ILogPart1 part1;
  private final ILogPart2 part2;

  public LogComposite(ILogPart1 part1, ILogPart2 part2) {
    if (part1 == null || part2 == null) {
      throw new IllegalArgumentException(
        "LogPart1 and LogPart2 cannot be null"
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
      // Если одна часть не определена, вся сумма не определена
      // Но можно рассмотреть случай, когда одна часть NaN, а другая +/- Infinity?
      // Стандартное поведение сложения с NaN - это NaN. Оставляем так.
      return Double.NaN;
    }

    double sum = val1 + val2;
    if (Double.isNaN(sum)) { // Например, inf + (-inf)
      return Double.NaN;
    }

    return sum;
  }
}
