package com.example.impl;

import com.example.interfaces.ICot;
import com.example.interfaces.ICsc;
import com.example.interfaces.ISec;
import com.example.interfaces.ITrigPart1;

public class TrigPart1 implements ITrigPart1 {

  private final ISec sec;
  private final ICsc csc;
  private final ICot cot;

  public TrigPart1(ISec sec, ICsc csc, ICot cot) {
    if (sec == null || csc == null || cot == null) {
      throw new IllegalArgumentException(
        "Sec, Csc, and Cot functions cannot be null"
      );
    }
    this.sec = sec;
    this.csc = csc;
    this.cot = cot;
  }

  @Override
  public double calculate(double x, double precision) {
    double secVal = sec.calculate(x, precision);
    double cscVal = csc.calculate(x, precision);
    double cotVal = cot.calculate(x, precision);

    if (Double.isNaN(secVal) || Double.isNaN(cscVal) || Double.isNaN(cotVal)) {
      return Double.NaN;
    }

    // Проверка деления на ноль (cot(x) = 0 при x = pi/2 + k*pi)
    if (Math.abs(cotVal) < precision) {
      return Double.NaN;
    }

    double numerator = secVal * secVal - cscVal;
    if (Double.isNaN(numerator)) { // Проверка на случай (-inf)^2 или похожих неопределенностей
      return Double.NaN;
    }

    return numerator / cotVal;
  }
}
