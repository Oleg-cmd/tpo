package com.example.impl;

import com.example.interfaces.ICos;
import com.example.interfaces.ISin;

public class Cos implements ICos {

  private final ISin sin;

  public Cos(ISin sin) {
    if (sin == null) {
      throw new IllegalArgumentException("Sin function cannot be null");
    }
    this.sin = sin;
  }

  @Override
  public double calculate(double x, double precision) {
    // Используем формулу cos(x) = sin(pi/2 - x)
    // Убедимся, что pi/2 - x не приводит к проблемам точности на границах
    double arg = Math.PI / 2.0 - x;
    return sin.calculate(arg, precision);
  }
}
