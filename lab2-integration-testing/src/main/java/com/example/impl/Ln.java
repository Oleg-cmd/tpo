package com.example.impl;

import com.example.interfaces.ILn;

public class Ln implements ILn {

  @Override
  public double calculate(double x, double precision) {
    if (
      Double.isNaN(x) ||
      x <= 0 ||
      precision <= 0 ||
      Double.isNaN(precision) ||
      Double.isInfinite(precision)
    ) {
      return Double.NaN; // Ln не определен для x <= 0
    }
    if (x == 1.0) {
      return 0.0; // Ln(1) = 0
    }
    if (x == Double.POSITIVE_INFINITY) {
      return Double.POSITIVE_INFINITY;
    }

    // Используем ряд для ln((1+y)/(1-y)) = 2 * (y + y^3/3 + y^5/5 + ...)
    // где y = (x-1)/(x+1)
    double y = (x - 1.0) / (x + 1.0);
    double sum = 0.0;
    double term = y; // Первый член: y
    double power = y;
    int n = 1; // Знаменатель: 1, 3, 5, ...

    // Итеративно вычисляем члены ряда
    int iterations = 0;
    final int MAX_ITERATIONS = 10000; // Для логарифма может потребоваться больше итераций

    // Сравниваем модуль следующего прибавляемого члена с точностью
    while (
      Math.abs(term / n) > precision / 2.0 && iterations < MAX_ITERATIONS
    ) { // Делим точность, т.к. итоговая сумма умножается на 2
      sum += term / n;
      n += 2;
      power *= y * y; // y^3, y^5, ...
      term = power;
      iterations++;
    }

    // Проверка на достижение лимита итераций
    if (iterations == MAX_ITERATIONS && Math.abs(term / n) > precision / 2.0) {
      System.err.println(
        "Warning: Ln calculation might be inaccurate due to max iterations reached for x=" +
        x
      );
      // return Double.NaN;
    }

    double result = 2.0 * sum;

    // Обработка результатов, близких к нулю
    if (Math.abs(result) < precision) {
      // Будьте осторожны, ln(x) может быть очень мал, но не равен 0 для x != 1
      // Лучше вернуть вычисленное значение, если оно не NaN
      // return 0.0; // Возможно, это не всегда корректно
    }

    return result;
  }
}
