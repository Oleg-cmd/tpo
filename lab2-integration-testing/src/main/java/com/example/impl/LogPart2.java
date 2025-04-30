package com.example.impl;

import com.example.interfaces.ILog;
import com.example.interfaces.ILogPart2;

public class LogPart2 implements ILogPart2 {

  private final ILog log10;
  private final ILog log2;
  private final ILog log5;

  public LogPart2(ILog log10, ILog log2, ILog log5) {
    if (log10 == null || log2 == null || log5 == null) {
      throw new IllegalArgumentException(
        "Log10, Log2, and Log5 functions cannot be null"
      );
    }
    this.log10 = log10;
    this.log2 = log2;
    this.log5 = log5;
  }

  @Override
  public double calculate(double x, double precision) {
    // Проверка на x = 1 (или близко к 1) в первую очередь
    if (Math.abs(x - 1.0) < precision) { // Если x практически равен 1
      // При x=1 имеем log10(1)/log2(1) + log5(1) = 0/0 + 0 -> Неопределенность
      return Double.NaN;
    } else {}

    // Теперь вычисляем логарифмы
    double log10Val = log10.calculate(x, precision);
    double log2Val = log2.calculate(x, precision);
    double log5Val = log5.calculate(x, precision);

    // Проверяем на NaN после вычислений
    if (
      Double.isNaN(log10Val) || Double.isNaN(log2Val) || Double.isNaN(log5Val)
    ) {
      return Double.NaN;
    }

    // Проверка знаменателя log2Val на близость к нулю (уже после проверки x=1)
    if (Math.abs(log2Val) < precision) {
      System.err.printf(
        "Warning: Denominator log2(x) is close to zero (~%.2e) for x = %.5f (x != 1)%n",
        log2Val,
        x
      );
      return Double.NaN;
    }

    // Выполняем вычисления
    double divisionResult = log10Val / log2Val;

    if (Double.isNaN(divisionResult)) { // Например, inf / inf
      return Double.NaN;
    }

    double sum = divisionResult + log5Val;

    if (Double.isNaN(sum)) { // Например, inf + (-inf)
      return Double.NaN;
    }

    return sum;
  }
}
