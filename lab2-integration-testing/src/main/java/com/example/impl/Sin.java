package com.example.impl;

import com.example.interfaces.ISin;

public class Sin implements ISin {

  @Override
  public double calculate(double x, double precision) {
    if (
      Double.isNaN(x) ||
      Double.isInfinite(x) ||
      precision <= 0 ||
      Double.isNaN(precision) ||
      Double.isInfinite(precision)
    ) {
      return Double.NaN;
    }

    // Нормализация x к [-pi, pi] для лучшей сходимости
    double reducedX = x % (2 * Math.PI);
    if (reducedX > Math.PI) {
      reducedX -= 2 * Math.PI;
    } else if (reducedX < -Math.PI) {
      reducedX += 2 * Math.PI;
    }

    // **ИСПРАВЛЕНИЕ:** Проверка на близость к PI или -PI после нормализации
    final double piThreshold = 1E-12; // Малый допуск для сравнения с PI
    if (Math.abs(Math.abs(reducedX) - Math.PI) < piThreshold) {
      return 0.0;
    }

    // Если reducedX сам по себе очень близок к 0, вернуть 0 до начала вычислений
    if (Math.abs(reducedX) < precision) {
      return 0.0;
    }

    // Вычисление ряда Тейлора
    double sum = 0.0;
    double term = reducedX; // Первый член ряда x
    long factorial = 1;
    double power = reducedX;
    int n = 1; // Показатель степени и факториала (нечетные)

    int iterations = 0;
    final int MAX_ITERATIONS = 1000;

    while (Math.abs(term) > precision && iterations < MAX_ITERATIONS) {
      sum += term;
      n += 2;
      power *= (-1) * reducedX * reducedX; // Следующий член степени x^(n) * (-1)^k

      long nextFactorialPart1;
      try {
        nextFactorialPart1 = Math.multiplyExact(factorial, (n - 1));
      } catch (ArithmeticException e) {
        System.err.println(
          "Warning: Factorial overflow during multiplyExact for Sin(" + x + ")"
        );
        return Double.NaN; // Переполнение факториала
      }

      long nextFactorialFull;
      try {
        nextFactorialFull = Math.multiplyExact(nextFactorialPart1, n);
      } catch (ArithmeticException e) {
        System.err.println(
          "Warning: Factorial overflow during multiplyExact for Sin(" + x + ")"
        );
        return Double.NaN; // Переполнение факториала
      }
      factorial = nextFactorialFull;

      // Проверка на случай, если факториал стал 0 (не должно быть при multiplyExact)
      if (factorial <= 0) {
        System.err.println(
          "Warning: Factorial is non-positive unexpectedly for Sin(" + x + ")"
        );
        return Double.NaN;
      }

      term = power / factorial;
      iterations++;
    }

    if (iterations == MAX_ITERATIONS && Math.abs(term) > precision) {
      System.err.println(
        "Warning: Sin calculation might be inaccurate due to max iterations reached for x=" +
        x
      );
    }

    // Обработка результатов, близких к нулю (добавляем последний вычисленный член)
    sum += term; // Добавляем последний член, даже если он уже меньше precision
    if (Math.abs(sum) < precision) {
      return 0.0;
    }

    return sum;
  }
}
