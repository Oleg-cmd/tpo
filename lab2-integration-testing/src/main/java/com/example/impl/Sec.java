package com.example.impl;

import com.example.interfaces.ICos;
import com.example.interfaces.ISec;

public class Sec implements ISec {

  private final ICos cos;

  public Sec(ICos cos) {
    if (cos == null) {
      throw new IllegalArgumentException("Cos function cannot be null");
    }
    this.cos = cos;
  }

  @Override
  public double calculate(double x, double precision) {
    double cosValue = cos.calculate(x, precision);

    if (Double.isNaN(cosValue)) {
      return Double.NaN;
    }

    // Проверка на деление на ноль с учетом точности
    // Если |cos(x)| очень мало, считаем это нулем в пределах точности
    if (Math.abs(cosValue) < precision) { // Используем precision как порог для нуля
      // Более строгая проверка: сравнить с машинным эпсилон или малой константой?
      // Для данной задачи, использование заданной точности precision может быть оправдано.
      // Альтернатива: if (Math.abs(cosValue) < 1E-12) return Double.NaN;
      return Double.NaN; // Деление на ноль (cos(x) = 0 при x = pi/2 + k*pi)
    }

    return 1.0 / cosValue;
  }
}
