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
    // --- ОТЛАДОЧНЫЙ ВЫВОД ---
    System.out.printf(
      "[DEBUG LogPart2] calculate(x=%.5f, precision=%.2e)%n",
      x,
      precision
    );
    // --- КОНЕЦ ОТЛАДКИ ---

    // Проверка на x = 1 (или близко к 1) в первую очередь
    if (Math.abs(x - 1.0) < precision) { // Если x практически равен 1
      // --- ОТЛАДОЧНЫЙ ВЫВОД ---
      System.out.printf(
        "[DEBUG LogPart2] Condition (Math.abs(%.5f - 1.0) < %.2e) is TRUE. Returning NaN.%n",
        x,
        precision
      );
      // --- КОНЕЦ ОТЛАДКИ ---
      // При x=1 имеем log10(1)/log2(1) + log5(1) = 0/0 + 0 -> Неопределенность
      return Double.NaN;
    } else {
      // --- ОТЛАДОЧНЫЙ ВЫВОД ---
      System.out.printf(
        "[DEBUG LogPart2] Condition (Math.abs(%.5f - 1.0) < %.2e) is FALSE.%n",
        x,
        precision
      );
      // --- КОНЕЦ ОТЛАДКИ ---
    }

    // Теперь вычисляем логарифмы
    double log10Val = log10.calculate(x, precision);
    double log2Val = log2.calculate(x, precision);
    double log5Val = log5.calculate(x, precision);
    // --- ОТЛАДОЧНЫЙ ВЫВОД ---
    System.out.printf(
      "[DEBUG LogPart2] Calculated logs: log10=%.5f, log2=%.5f, log5=%.5f%n",
      log10Val,
      log2Val,
      log5Val
    );
    // --- КОНЕЦ ОТЛАДКИ ---

    // Проверяем на NaN после вычислений
    if (
      Double.isNaN(log10Val) || Double.isNaN(log2Val) || Double.isNaN(log5Val)
    ) {
      // --- ОТЛАДОЧНЫЙ ВЫВОД ---
      System.out.println(
        "[DEBUG LogPart2] One of the logs is NaN. Returning NaN."
      );
      // --- КОНЕЦ ОТЛАДКИ ---
      return Double.NaN;
    }

    // Проверка знаменателя log2Val на близость к нулю (уже после проверки x=1)
    if (Math.abs(log2Val) < precision) {
      // --- ОТЛАДОЧНЫЙ ВЫВОД ---
      System.out.printf(
        "[DEBUG LogPart2] Denominator log2Val (~%.2e) is close to zero. Returning NaN.%n",
        log2Val
      );
      // --- КОНЕЦ ОТЛАДКИ ---
      System.err.printf(
        "Warning: Denominator log2(x) is close to zero (~%.2e) for x = %.5f (x != 1)%n",
        log2Val,
        x
      );
      return Double.NaN;
    }

    // Выполняем вычисления
    double divisionResult = log10Val / log2Val;
    // --- ОТЛАДОЧНЫЙ ВЫВОД ---
    System.out.printf(
      "[DEBUG LogPart2] divisionResult = %.5f / %.5f = %.5f%n",
      log10Val,
      log2Val,
      divisionResult
    );
    // --- КОНЕЦ ОТЛАДКИ ---

    if (Double.isNaN(divisionResult)) { // Например, inf / inf
      // --- ОТЛАДОЧНЫЙ ВЫВОД ---
      System.out.println(
        "[DEBUG LogPart2] divisionResult is NaN. Returning NaN."
      );
      // --- КОНЕЦ ОТЛАДКИ ---
      return Double.NaN;
    }

    double sum = divisionResult + log5Val;
    // --- ОТЛАДОЧНЫЙ ВВыВОД ---
    System.out.printf(
      "[DEBUG LogPart2] sum = %.5f + %.5f = %.5f. Returning sum.%n",
      divisionResult,
      log5Val,
      sum
    );
    // --- КОНЕЦ ОТЛАДКИ ---
    if (Double.isNaN(sum)) { // Например, inf + (-inf)
      // --- ОТЛАДОЧНЫЙ ВЫВОД ---
      System.out.println("[DEBUG LogPart2] sum is NaN. Returning NaN.");
      // --- КОНЕЦ ОТЛАДКИ ---
      return Double.NaN;
    }

    return sum;
  }
}
