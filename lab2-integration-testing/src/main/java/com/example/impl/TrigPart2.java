package com.example.impl;

import com.example.interfaces.ICsc;
import com.example.interfaces.ISec;
import com.example.interfaces.ITrigPart2;

public class TrigPart2 implements ITrigPart2 {

  private final ISec sec;
  private final ICsc csc;

  public TrigPart2(ISec sec, ICsc csc) {
    if (sec == null || csc == null) {
      throw new IllegalArgumentException(
        "Sec and Csc functions cannot be null"
      );
    }
    this.sec = sec;
    this.csc = csc;
  }

  @Override
  public double calculate(double x, double precision) {
    double secVal = sec.calculate(x, precision);
    double cscVal = csc.calculate(x, precision);

    if (Double.isNaN(secVal) || Double.isNaN(cscVal)) {
      return Double.NaN;
    }

    double base = secVal - cscVal;
    if (Double.isNaN(base)) { // Например, inf - inf
      return Double.NaN;
    }

    // Возводим в квадрат - результат всегда неотрицательный (или NaN)
    return base * base;
  }
}
