package com.example.impl;

import com.example.interfaces.ICos;
import com.example.interfaces.ICot;
import com.example.interfaces.ISin;

public class Cot implements ICot {

  private final ICos cos;
  private final ISin sin;

  public Cot(ICos cos, ISin sin) {
    if (cos == null || sin == null) {
      throw new IllegalArgumentException(
        "Cos and Sin functions cannot be null"
      );
    }
    this.cos = cos;
    this.sin = sin;
  }

  @Override
  public double calculate(double x, double precision) {
    double sinValue = sin.calculate(x, precision);
    double cosValue = cos.calculate(x, precision);

    if (Double.isNaN(sinValue) || Double.isNaN(cosValue)) {
      return Double.NaN;
    }

    // Проверка на деление на ноль с учетом точности
    if (Math.abs(sinValue) < precision) { // sin(x) = 0 при x = k*pi
      return Double.NaN;
    }

    return cosValue / sinValue;
  }
}
