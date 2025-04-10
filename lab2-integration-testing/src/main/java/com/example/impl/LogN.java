package com.example.impl;

import com.example.interfaces.ILn;
import com.example.interfaces.ILog;

public class LogN implements ILog {

  private final ILn ln;
  private final double base;
  private Double lnBase = null; // Кэшируем ln(base)

  public LogN(ILn ln, double base) {
    if (ln == null) {
      throw new IllegalArgumentException("Ln function cannot be null");
    }
    if (base <= 0 || base == 1.0) {
      // Логарифм по основанию <= 0 или == 1 не определен
      throw new IllegalArgumentException(
        "Log base must be > 0 and != 1. Got: " + base
      );
      // Альтернативно, можно сделать так, чтобы calculate возвращал NaN для некорректной базы,
      // но лучше пресечь на этапе создания объекта.
    }
    this.ln = ln;
    this.base = base;
  }

  // Метод для получения ln(base) с кэшированием и ленивой инициализацией
  private double getLnBase(double precision) {
    if (this.lnBase == null) {
      this.lnBase = ln.calculate(this.base, precision);
      // Дополнительная проверка после вычисления
      if (Double.isNaN(this.lnBase) || Math.abs(this.lnBase) < precision) {
        // Если ln(base) не вычислился или слишком близок к нулю (что не должно быть для base!=1)
        // Считаем базу некорректной для данной точности
        System.err.println(
          "Error: Could not calculate ln(base) or it's too close to zero for base=" +
          base +
          " with precision=" +
          precision
        );
        this.lnBase = Double.NaN; // Помечаем как невалидное
      }
    }
    return this.lnBase;
  }

  @Override
  public double calculate(double x, double precision) {
    // Проверка аргумента x (логарифм определен для x > 0)
    if (Double.isNaN(x) || x <= 0) {
      return Double.NaN;
    }

    double lnX = ln.calculate(x, precision);
    double lnBaseValue = getLnBase(precision);

    if (Double.isNaN(lnX) || Double.isNaN(lnBaseValue)) {
      return Double.NaN;
    }

    // Проверка деления на ноль (lnBaseValue не должен быть близок к нулю, т.к. base != 1)
    // Но на всякий случай проверим, если ln.calculate вернул что-то близкое к 0
    if (Math.abs(lnBaseValue) < precision) {
      System.err.println("Warning: Denominator ln(base) is close to zero.");
      return Double.NaN;
    }

    return lnX / lnBaseValue;
  }

  // Опционально: геттер для базы, если нужен
  public double getBase() {
    return base;
  }
}
