package com.example.impl; // Или com.example

import static org.junit.Assert.*;

import com.example.interfaces.ILn;
import org.junit.Before;
import org.junit.Test;

public class LnTest {

  private ILn ln;
  // Для Ln может потребоваться бОльшая точность вычислений для достижения нужной точности сравнения
  private final double CALCULATION_PRECISION = 1E-12;
  private final double COMPARISON_DELTA = 1E-7;

  @Before
  public void setUp() {
    ln = new Ln();
  }

  // --- Тесты на известные значения ---

  @Test
  public void testLnOne() {
    assertEquals(
      0.0,
      ln.calculate(1.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testLnE() {
    assertEquals(
      1.0,
      ln.calculate(Math.E, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testLnExp2() {
    assertEquals(
      2.0,
      ln.calculate(Math.exp(2), CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testLnExpMinus1() {
    assertEquals(
      -1.0,
      ln.calculate(Math.exp(-1), CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testLnValueBetween0And1() {
    // ln(0.5) = -ln(2)
    assertEquals(
      -Math.log(2),
      ln.calculate(0.5, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testLnValueGreaterThan1() {
    // ln(10)
    assertEquals(
      Math.log(10),
      ln.calculate(10.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  // --- Тесты на граничные и невалидные входы ---

  @Test
  public void testLnZero() {
    // ln(0) не определен (математически -> -Infinity)
    // Наша реализация должна возвращать NaN по условию x <= 0
    assertTrue(Double.isNaN(ln.calculate(0.0, CALCULATION_PRECISION)));
  }

  @Test
  public void testLnVeryCloseToZero() {
    // Проверяем предел справа от нуля, ожидаем большое отрицательное число
    // Точное значение не так важно, как знак и порядок величины.
    // Math.log(Double.MIN_VALUE) ~ -708
    assertTrue(
      "Expected a negative result for Ln near zero",
      ln.calculate(Double.MIN_VALUE, CALCULATION_PRECISION) < 0.0
    );
    // Или можно проверить на Double.NEGATIVE_INFINITY, если реализация Ln должна его возвращать
    // assertEquals(Double.NEGATIVE_INFINITY, ln.calculate(Math.nextUp(0.0), CALCULATION_PRECISION), COMPARISON_DELTA);
  }

  @Test
  public void testLnNegativeInput() {
    assertTrue(Double.isNaN(ln.calculate(-1.0, CALCULATION_PRECISION)));
    assertTrue(Double.isNaN(ln.calculate(-Math.E, CALCULATION_PRECISION)));
    assertTrue(Double.isNaN(ln.calculate(-100.0, CALCULATION_PRECISION)));
  }

  @Test
  public void testLnNaNInput() {
    assertTrue(Double.isNaN(ln.calculate(Double.NaN, CALCULATION_PRECISION)));
  }

  @Test
  public void testLnPositiveInfinityInput() {
    // ln(inf) = inf
    assertEquals(
      Double.POSITIVE_INFINITY,
      ln.calculate(Double.POSITIVE_INFINITY, CALCULATION_PRECISION),
      0.0
    );
  }

  @Test
  public void testLnNegativeInfinityInput() {
    // ln(-inf) не определен
    assertTrue(
      Double.isNaN(
        ln.calculate(Double.NEGATIVE_INFINITY, CALCULATION_PRECISION)
      )
    );
  }

  @Test
  public void testLnZeroPrecision() {
    assertTrue(Double.isNaN(ln.calculate(1.0, 0.0)));
  }

  @Test
  public void testLnNegativePrecision() {
    assertTrue(Double.isNaN(ln.calculate(1.0, -0.1)));
  }

  @Test
  public void testLnNaNPrecision() {
    assertTrue(Double.isNaN(ln.calculate(1.0, Double.NaN)));
  }
}
