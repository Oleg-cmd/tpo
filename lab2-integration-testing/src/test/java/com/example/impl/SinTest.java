package com.example.impl;

import static org.junit.Assert.*;

import com.example.interfaces.ISin;
import org.junit.Before;
import org.junit.Test;

public class SinTest {

  private ISin sin;
  private final double CALCULATION_PRECISION = 1E-10; // Точность для вычисления ряда
  private final double COMPARISON_DELTA = 1E-6; // Допуск для сравнения assertEquals

  @Before
  public void setUp() {
    // Создаем экземпляр нашей реализации перед каждым тестом
    sin = new Sin();
  }

  // --- Тесты на известные значения ---

  @Test
  public void testSinZero() {
    assertEquals(
      0.0,
      sin.calculate(0.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testSinPiOver6() {
    assertEquals(
      0.5,
      sin.calculate(Math.PI / 6, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testSinPiOver4() {
    assertEquals(
      Math.sqrt(2) / 2.0,
      sin.calculate(Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testSinPiOver2() {
    assertEquals(
      1.0,
      sin.calculate(Math.PI / 2, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testSinPi() {
    assertEquals(
      0.0,
      sin.calculate(Math.PI, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testSin3PiOver2() {
    assertEquals(
      -1.0,
      sin.calculate((3 * Math.PI) / 2, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testSin2Pi() {
    assertEquals(
      0.0,
      sin.calculate(2 * Math.PI, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  // --- Тесты на отрицательные значения (проверка нечетности) ---

  @Test
  public void testSinNegativePiOver6() {
    assertEquals(
      -0.5,
      sin.calculate(-Math.PI / 6, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testSinNegativePiOver2() {
    assertEquals(
      -1.0,
      sin.calculate(-Math.PI / 2, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testSinNegativePi() {
    assertEquals(
      0.0,
      sin.calculate(-Math.PI, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  // --- Тесты на периодичность ---

  @Test
  public void testPeriodicityPositive() {
    double x = Math.PI / 5; // Произвольное значение
    assertEquals(
      sin.calculate(x, CALCULATION_PRECISION),
      sin.calculate(x + 2 * Math.PI, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      sin.calculate(x, CALCULATION_PRECISION),
      sin.calculate(x + 4 * Math.PI, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  @Test
  public void testPeriodicityNegative() {
    double x = -Math.PI / 7; // Произвольное значение
    assertEquals(
      sin.calculate(x, CALCULATION_PRECISION),
      sin.calculate(x - 2 * Math.PI, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      sin.calculate(x, CALCULATION_PRECISION),
      sin.calculate(x - 6 * Math.PI, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  // --- Тесты на граничные и невалидные входы ---

  @Test
  public void testSinNaNInput() {
    assertTrue(Double.isNaN(sin.calculate(Double.NaN, CALCULATION_PRECISION)));
  }

  @Test
  public void testSinPositiveInfinityInput() {
    assertTrue(
      Double.isNaN(
        sin.calculate(Double.POSITIVE_INFINITY, CALCULATION_PRECISION)
      )
    );
  }

  @Test
  public void testSinNegativeInfinityInput() {
    assertTrue(
      Double.isNaN(
        sin.calculate(Double.NEGATIVE_INFINITY, CALCULATION_PRECISION)
      )
    );
  }

  @Test
  public void testSinZeroPrecision() {
    assertTrue(Double.isNaN(sin.calculate(1.0, 0.0))); // Нулевая точность невалидна
  }

  @Test
  public void testSinNegativePrecision() {
    assertTrue(Double.isNaN(sin.calculate(1.0, -0.1))); // Отрицательная точность невалидна
  }

  @Test
  public void testSinNaNPrecision() {
    assertTrue(Double.isNaN(sin.calculate(1.0, Double.NaN)));
  }

  // Дополнительный тест на сходимость для большого аргумента (проверка нормализации)
  @Test
  public void testLargeArgument() {
    double largeX = 100 * Math.PI + Math.PI / 3; // Эквивалентно PI/3
    assertEquals(
      Math.sin(Math.PI / 3),
      sin.calculate(largeX, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  // Дополнительный тест на сходимость для отрицательного большого аргумента
  @Test
  public void testNegativeLargeArgument() {
    double largeX = -100 * Math.PI - Math.PI / 4; // Эквивалентно -PI/4
    assertEquals(
      Math.sin(-Math.PI / 4),
      sin.calculate(largeX, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }
}
