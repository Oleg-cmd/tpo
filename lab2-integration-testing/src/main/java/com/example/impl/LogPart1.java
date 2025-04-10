package com.example.impl;

import com.example.interfaces.ILog;
import com.example.interfaces.ILogPart1;

public class LogPart1 implements ILogPart1 {

  private final ILog log2;
  private final ILog log5;

  public LogPart1(ILog log2, ILog log5) {
    // Можно добавить проверку, что база у log2 действительно 2, а у log5 - 5, если интерфейс ILog это позволяет.
    // Или полагаться на корректное создание объектов LogN.
    if (log2 == null || log5 == null) {
      throw new IllegalArgumentException(
        "Log2 and Log5 functions cannot be null"
      );
    }
    this.log2 = log2;
    this.log5 = log5;
  }

  @Override
  public double calculate(double x, double precision) {
    double log2Val = log2.calculate(x, precision);
    double log5Val = log5.calculate(x, precision);

    if (Double.isNaN(log2Val) || Double.isNaN(log5Val)) {
      return Double.NaN;
    }

    double base = log2Val * log5Val;
    if (Double.isNaN(base)) { // Например, 0 * inf
      return Double.NaN;
    }

    // Вычисляем base ^ 18. Math.pow может быть неточным или медленным.
    // Проще сделать через умножение.
    // Так как степень четная (18), результат всегда >= 0 (или NaN)
    double result = base;
    for (int i = 1; i < 18; i++) {
      result *= base;
      if (Double.isInfinite(result) && !Double.isInfinite(base)) {
        // Если результат стал бесконечным при конечном основании,
        // возможно переполнение. Зависит от требований, можно вернуть NaN или Inf.
        // Оставим как есть, Math.pow() тоже вернул бы Inf.
      }
      if (Double.isNaN(result)) return Double.NaN; // Если промежуточное вычисление дало NaN
    }

    // Альтернатива через Math.pow:
    // double result = Math.pow(base, 18);
    // if (Double.isNaN(result)) return Double.NaN;

    return result;
  }
}
