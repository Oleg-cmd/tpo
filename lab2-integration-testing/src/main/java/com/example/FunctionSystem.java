package com.example;

import com.example.interfaces.ILogComposite;
import com.example.interfaces.ITrigComposite;

public class FunctionSystem {

  private final ITrigComposite trigPart; // Для x <= 0
  private final ILogComposite logPart; // Для x > 0

  public FunctionSystem(ITrigComposite trigPart, ILogComposite logPart) {
    if (trigPart == null || logPart == null) {
      throw new IllegalArgumentException(
        "Trigonometric and Logarithmic parts cannot be null"
      );
    }
    this.trigPart = trigPart;
    this.logPart = logPart;
  }

  /**
   * Вычисляет значение системы функций в зависимости от x.
   *
   * @param x         Входное значение аргумента.
   * @param precision Требуемая точность для базовых функций (sin, ln).
   * @return Вычисленное значение системы или Double.NaN, если вычисление невозможно
   *         (например, из-за неопределенности в одной из веток или некорректной точности).
   */
  public double compute(double x, double precision) {
    if (Double.isNaN(x) || Double.isInfinite(x)) {
      return Double.NaN; // Система не определена для NaN или бесконечности
    }
    if (
      precision <= 0 || Double.isNaN(precision) || Double.isInfinite(precision)
    ) {
      System.err.println("Error: Invalid precision value: " + precision);
      return Double.NaN; // Некорректная точность
    }

    if (x <= 0) {
      // Вычисляем тригонометрическую часть
      return trigPart.calculate(x, precision);
    } else { // x > 0
      // Вычисляем логарифмическую часть
      return logPart.calculate(x, precision);
    }
  }
}
