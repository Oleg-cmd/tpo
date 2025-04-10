package com.example.impl;

import com.example.interfaces.ICsc;
import com.example.interfaces.ISin;

public class Csc implements ICsc {

  private final ISin sin;

  public Csc(ISin sin) {
    if (sin == null) {
      throw new IllegalArgumentException("Sin function cannot be null");
    }
    this.sin = sin;
  }

  @Override
  public double calculate(double x, double precision) {
    double sinValue = sin.calculate(x, precision);

    if (Double.isNaN(sinValue)) {
      return Double.NaN;
    }

    // Проверка на деление на ноль с учетом точности
    if (Math.abs(sinValue) < precision) { // sin(x) = 0 при x = k*pi
      return Double.NaN;
    }

    return 1.0 / sinValue;
  }
}
