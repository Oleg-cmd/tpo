package com.example;

import static org.junit.Assert.*;

import com.example.impl.*;
import com.example.interfaces.*;
import com.example.stubs.*;
import org.junit.Test;

public class FunctionSystemTest {

  // Точность для базовых функций и дельта для сравнения
  private final double CALCULATION_PRECISION = 1E-7; // Точность для Sin/Ln
  private final double COMPARISON_DELTA = 1E-5; // Допуск для assertEquals

  // --- Тест 1: Система собрана полностью из ЗАГЛУШЕК ---
  // Проверяет логику FunctionSystem и верхнеуровневые заглушки
  @Test
  public void testSystemWithAllStubs() {
    System.out.println("--- Testing FunctionSystem with ALL STUBS ---");
    ITrigComposite trigStub = new TrigCompositeStub();
    ILogComposite logStub = new LogCompositeStub();
    FunctionSystem system = new FunctionSystem(trigStub, logStub);

    // Проверяем точки, определенные в TrigCompositeStub (x <= 0)
    // x = -PI/4
    double expectedTrig1 = 128.0 * (3.0 + 2.0 * Math.sqrt(2)); // Из TrigCompositeStub
    assertEquals(
      expectedTrig1,
      system.compute(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // x = -PI/6
    double part1_pi6_stub = -10.0 / (3 * Math.sqrt(3));
    double part2_pi6_stub = (8.0 * (2.0 + Math.sqrt(3))) / 3.0;
    double expectedTrig2 = Math.pow(part1_pi6_stub * part2_pi6_stub, 2); // Из TrigCompositeStub
    assertEquals(
      expectedTrig2,
      system.compute(-Math.PI / 6, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // x = 0 (должен использовать триг. ветку, где результат NaN)
    assertTrue(Double.isNaN(system.compute(0.0, CALCULATION_PRECISION)));

    // Проверяем точки, определенные в LogCompositeStub (x > 0)
    // x = 1 (результат NaN из-за деления на ноль в LogPart2)
    assertTrue(Double.isNaN(system.compute(1.0, CALCULATION_PRECISION)));

    // x = 2
    double log5_2 = 0.43067655807339306;
    double log10_2 = 0.3010299956639812;
    double part1_2 = Math.pow(log5_2, 18);
    double part2_2 = log10_2 + log5_2;
    double expectedLog1 = part1_2 + part2_2; // Из LogCompositeStub
    assertEquals(
      expectedLog1,
      system.compute(2.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // x = 5
    double log2_5 = 2.321928094887362;
    double log10_5 = 0.6989700043360188;
    double part1_5 = Math.pow(log2_5, 18);
    double part2_5 = log10_5 / log2_5 + 1.0;
    double expectedLog2 = part1_5 + part2_5; // Из LogCompositeStub
    assertEquals(
      expectedLog2,
      system.compute(5.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // Невалидный x
    assertTrue(Double.isNaN(system.compute(Double.NaN, CALCULATION_PRECISION)));
    assertTrue(
      Double.isNaN(
        system.compute(Double.POSITIVE_INFINITY, CALCULATION_PRECISION)
      )
    );

    // Невалидная точность
    assertTrue(Double.isNaN(system.compute(2.0, 0.0)));
    assertTrue(Double.isNaN(system.compute(-1.0, -1.0)));
  }

  // --- Тест 2: Интеграция базовых Sin и Ln ---
  // Заменяем SinStub -> Sin, LnStub -> Ln. Остальное - заглушки более высокого уровня.
  // Этот тест не очень показателен без замены зависимых модулей,
  // но является первым шагом интеграции.
  @Test
  public void testSystemWithRealBaseFunctions() {
    System.out.println(
      "--- Testing FunctionSystem with REAL Sin, Ln; STUBS for others ---"
    );
    // Реальные базовые функции
    ISin sin = new Sin();
    ILn ln = new Ln();

    // Заглушки для всего остального
    ICos cosStub = new CosStub();
    ISec secStub = new SecStub();
    ICsc cscStub = new CscStub();
    ICot cotStub = new CotStub();
    ILog log2Stub = LogNStub.createLog2Stub();
    ILog log5Stub = LogNStub.createLog5Stub();
    ILog log10Stub = LogNStub.createLog10Stub();

    ITrigPart1 trigPart1Stub = new TrigPart1Stub(); // Используют Sec/Csc/Cot Stub
    ITrigPart2 trigPart2Stub = new TrigPart2Stub(); // Используют Sec/Csc Stub
    ITrigComposite trigCompositeStub = new TrigCompositeStub(); // Используют TrigPart1/TrigPart2 Stub

    ILogPart1 logPart1Stub = new LogPart1Stub(); // Используют Log2/Log5 Stub
    ILogPart2 logPart2Stub = new LogPart2Stub(); // Используют Log10/Log2/Log5 Stub
    ILogComposite logCompositeStub = new LogCompositeStub(); // Используют LogPart1/LogPart2 Stub

    // Система собирается из ЗАГЛУШЕК верхнего уровня, хотя Sin/Ln реальные,
    // они НЕ ИСПОЛЬЗУЮТСЯ на этом этапе, т.к. зависящие от них модули (Cos, LogN) - заглушки.
    FunctionSystem system = new FunctionSystem(
      trigCompositeStub,
      logCompositeStub
    );

    // Результаты должны быть такими же, как в тесте с полными заглушками
    // Повторим пару проверок:
    assertEquals(
      128.0 * (3.0 + 2.0 * Math.sqrt(2)),
      system.compute(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertTrue(Double.isNaN(system.compute(1.0, CALCULATION_PRECISION)));

    // Дополнительно можно проверить, что реальные Sin/Ln работают
    // (хотя для этого есть модульные тесты)
    assertEquals(
      0.5,
      sin.calculate(Math.PI / 6, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      1.0,
      ln.calculate(Math.E, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
  }

  // --- Тест 3: Интеграция Cos ---
  // Sin - реальный, Cos - реальный, Ln - реальный. Остальное - заглушки.
  @Test
  public void testSystemWithRealCos() {
    System.out.println(
      "--- Testing FunctionSystem with REAL Sin, Cos, Ln; STUBS for others ---"
    );
    ISin sin = new Sin();
    ICos cos = new Cos(sin); // Реальный Cos использует реальный Sin
    ILn ln = new Ln();

    // Остальные - заглушки
    ISec secStub = new SecStub();
    ICsc cscStub = new CscStub();
    ICot cotStub = new CotStub(); // Заглушка Cot еще не использует реальный Cos/Sin
    ILog log2Stub = LogNStub.createLog2Stub();
    ILog log5Stub = LogNStub.createLog5Stub();
    ILog log10Stub = LogNStub.createLog10Stub();
    ITrigPart1 trigPart1Stub = new TrigPart1Stub();
    ITrigPart2 trigPart2Stub = new TrigPart2Stub();
    ITrigComposite trigCompositeStub = new TrigCompositeStub();
    ILogPart1 logPart1Stub = new LogPart1Stub();
    ILogPart2 logPart2Stub = new LogPart2Stub();
    ILogComposite logCompositeStub = new LogCompositeStub();

    FunctionSystem system = new FunctionSystem(
      trigCompositeStub,
      logCompositeStub
    );

    // Результаты системы все еще зависят от заглушек верхнего уровня
    assertEquals(
      128.0 * (3.0 + 2.0 * Math.sqrt(2)),
      system.compute(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // Проверим, что реальный Cos работает правильно (дублирование модульного теста)
    assertEquals(
      0.0,
      cos.calculate(Math.PI / 2, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      1.0,
      cos.calculate(0.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      Math.cos(-Math.PI / 3),
      cos.calculate(-Math.PI / 3, CALCULATION_PRECISION),
      COMPARISON_DELTA
    ); // Сравним с Math.cos
  }

  // --- Тест 4: Интеграция Sec, Csc, Cot ---
  // Sin, Cos, Sec, Csc, Cot - реальные. Ln - реальный. Логарифмы - заглушки.
  @Test
  public void testSystemWithRealTrigDerivatives() {
    System.out.println(
      "--- Testing FunctionSystem with REAL Sin, Cos, Sec, Csc, Cot; STUBS for Logs & Composites ---"
    );
    ISin sin = new Sin();
    ICos cos = new Cos(sin);
    ISec sec = new Sec(cos); // Реальный Sec
    ICsc csc = new Csc(sin); // Реальный Csc
    ICot cot = new Cot(cos, sin); // Реальный Cot
    ILn ln = new Ln();

    // Остальные - заглушки
    ILog log2Stub = LogNStub.createLog2Stub();
    ILog log5Stub = LogNStub.createLog5Stub();
    ILog log10Stub = LogNStub.createLog10Stub();
    ITrigPart1 trigPart1Stub = new TrigPart1Stub(); // Все еще заглушка!
    ITrigPart2 trigPart2Stub = new TrigPart2Stub(); // Все еще заглушка!
    ITrigComposite trigCompositeStub = new TrigCompositeStub(); // Все еще заглушка!
    ILogPart1 logPart1Stub = new LogPart1Stub();
    ILogPart2 logPart2Stub = new LogPart2Stub();
    ILogComposite logCompositeStub = new LogCompositeStub();

    FunctionSystem system = new FunctionSystem(
      trigCompositeStub,
      logCompositeStub
    );

    // Результаты системы все еще зависят от заглушек верхнего уровня
    assertEquals(
      128.0 * (3.0 + 2.0 * Math.sqrt(2)),
      system.compute(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // Проверим реальные Sec, Csc, Cot в некоторых точках
    assertEquals(
      1.0 / Math.cos(Math.PI / 6),
      sec.calculate(Math.PI / 6, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      1.0 / Math.sin(Math.PI / 3),
      csc.calculate(Math.PI / 3, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      Math.cos(-Math.PI / 4) / Math.sin(-Math.PI / 4),
      cot.calculate(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // Проверим точки разрыва (должны вернуть NaN)
    assertTrue(Double.isNaN(sec.calculate(Math.PI / 2, CALCULATION_PRECISION)));
    assertTrue(Double.isNaN(csc.calculate(Math.PI, CALCULATION_PRECISION)));
    assertTrue(Double.isNaN(cot.calculate(0.0, CALCULATION_PRECISION)));
  }

  // --- Тест 5: Интеграция LogN ---
  // Sin, Cos, Sec, Csc, Cot - реальные. Ln - реальный. Log2, Log5, Log10 - реальные.
  // Композитные части - заглушки.
  @Test
  public void testSystemWithRealLogN() {
    System.out.println(
      "--- Testing FunctionSystem with REAL Trig funcs, Ln, LogN; STUBS for Composites ---"
    );
    ISin sin = new Sin();
    ICos cos = new Cos(sin);
    ISec sec = new Sec(cos);
    ICsc csc = new Csc(sin);
    ICot cot = new Cot(cos, sin);
    ILn ln = new Ln();
    ILog log2 = new LogN(ln, 2.0); // Реальный Log2
    ILog log5 = new LogN(ln, 5.0); // Реальный Log5
    ILog log10 = new LogN(ln, 10.0); // Реальный Log10

    // Композитные части - заглушки
    ITrigPart1 trigPart1Stub = new TrigPart1Stub();
    ITrigPart2 trigPart2Stub = new TrigPart2Stub();
    ITrigComposite trigCompositeStub = new TrigCompositeStub();
    ILogPart1 logPart1Stub = new LogPart1Stub();
    ILogPart2 logPart2Stub = new LogPart2Stub();
    ILogComposite logCompositeStub = new LogCompositeStub();

    FunctionSystem system = new FunctionSystem(
      trigCompositeStub,
      logCompositeStub
    );

    // Результаты системы все еще зависят от заглушек верхнего уровня
    assertEquals(
      128.0 * (3.0 + 2.0 * Math.sqrt(2)),
      system.compute(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // Проверим реальные LogN
    assertEquals(
      3.0,
      log2.calculate(8.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      1.0,
      log5.calculate(5.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      2.0,
      log10.calculate(100.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    assertEquals(
      Math.log(7) / Math.log(5),
      log5.calculate(7.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // Проверим некорректные входы для LogN
    assertTrue(Double.isNaN(log2.calculate(0.0, CALCULATION_PRECISION)));
    assertTrue(Double.isNaN(log5.calculate(-5.0, CALCULATION_PRECISION)));
    assertTrue(Double.isNaN(log10.calculate(1.0, 0.0))); // Невалидная точность
  }

  // --- Тест 6: Интеграция композитных частей (TrigPart1, TrigPart2, LogPart1, LogPart2) ---
  @Test
  public void testSystemWithRealParts() {
    System.out.println(
      "--- Testing FunctionSystem with REAL Parts; STUBS for Composites ---"
    );
    // Все базовые и производные реальные
    ISin sin = new Sin();
    ICos cos = new Cos(sin);
    ISec sec = new Sec(cos);
    ICsc csc = new Csc(sin);
    ICot cot = new Cot(cos, sin);
    ILn ln = new Ln();
    ILog log2 = new LogN(ln, 2.0);
    ILog log5 = new LogN(ln, 5.0);
    ILog log10 = new LogN(ln, 10.0);

    // Реальные составные части
    ITrigPart1 trigPart1 = new TrigPart1(sec, csc, cot);
    ITrigPart2 trigPart2 = new TrigPart2(sec, csc);
    ILogPart1 logPart1 = new LogPart1(log2, log5);
    ILogPart2 logPart2 = new LogPart2(log10, log2, log5);

    ITrigComposite trigCompositeStub = new TrigCompositeStub();
    ILogComposite logCompositeStub = new LogCompositeStub();

    FunctionSystem system = new FunctionSystem(
      trigCompositeStub,
      logCompositeStub
    );

    // Результаты системы все еще зависят от заглушек верхнего уровня
    assertEquals(
      128.0 * (3.0 + 2.0 * Math.sqrt(2)),
      system.compute(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // Проверим реальные составные части в некоторых точках
    // TrigPart1 for x = -PI/4
    double secVal_pi4 = sec.calculate(-Math.PI / 4, CALCULATION_PRECISION);
    double cscVal_pi4 = csc.calculate(-Math.PI / 4, CALCULATION_PRECISION);
    double cotVal_pi4 = cot.calculate(-Math.PI / 4, CALCULATION_PRECISION);
    double expectedTrigPart1 =
      (secVal_pi4 * secVal_pi4 - cscVal_pi4) / cotVal_pi4;
    assertEquals(
      expectedTrigPart1,
      trigPart1.calculate(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // TrigPart2 for x = -PI/4
    double baseTrigPart2 = secVal_pi4 - cscVal_pi4;
    double expectedTrigPart2 = baseTrigPart2 * baseTrigPart2;
    assertEquals(
      expectedTrigPart2,
      trigPart2.calculate(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // LogPart1 for x = 5
    double log2Val_5 = log2.calculate(5.0, CALCULATION_PRECISION);
    double log5Val_5 = log5.calculate(5.0, CALCULATION_PRECISION);
    double expectedLogPart1 = Math.pow(log2Val_5 * log5Val_5, 18);
    assertEquals(
      expectedLogPart1,
      logPart1.calculate(5.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // LogPart2 for x = 10
    double log10Val_10 = log10.calculate(10.0, CALCULATION_PRECISION);
    double log2Val_10 = log2.calculate(10.0, CALCULATION_PRECISION);
    double log5Val_10 = log5.calculate(10.0, CALCULATION_PRECISION);
    double expectedLogPart2 = (log10Val_10 / log2Val_10) + log5Val_10;
    assertEquals(
      expectedLogPart2,
      logPart2.calculate(10.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );

    // Проверим NaN случаи для частей
    assertTrue(Double.isNaN(trigPart1.calculate(0.0, CALCULATION_PRECISION))); // Деление на cot(0)=NaN
    assertTrue(Double.isNaN(trigPart2.calculate(0.0, CALCULATION_PRECISION))); // csc(0)=NaN
    // ИСПРАВЛЕНИЕ: Проверяем на 0, а не NaN
    assertEquals(
      0.0,
      logPart1.calculate(1.0, CALCULATION_PRECISION),
      COMPARISON_DELTA
    );
    // УБЕДИМСЯ, ЧТО МЫ ВЫЗЫВАЕМ МЕТОД ПРАВИЛЬНО:
    double resultLogPart2ForOne = logPart2.calculate(
      1.0,
      CALCULATION_PRECISION
    );
    System.out.printf(
      "[TEST DEBUG] Result of logPart2.calculate(1.0, %.2e) is: %s%n",
      CALCULATION_PRECISION,
      Double.toString(resultLogPart2ForOne)
    );
    assertTrue(
      "Expected logPart2(1.0) to be NaN",
      Double.isNaN(resultLogPart2ForOne)
    );
  }

  // --- Тест 7: Полностью интегрированная система ---
  @Test
  public void testFullyIntegratedSystem() {
    System.out.println("--- Testing FunctionSystem with ALL REAL modules ---");
    // Все реальные
    ISin sin = new Sin();
    ICos cos = new Cos(sin);
    ISec sec = new Sec(cos);
    ICsc csc = new Csc(sin);
    ICot cot = new Cot(cos, sin);
    ILn ln = new Ln();
    ILog log2 = new LogN(ln, 2.0);
    ILog log5 = new LogN(ln, 5.0);
    ILog log10 = new LogN(ln, 10.0);
    ITrigPart1 trigPart1 = new TrigPart1(sec, csc, cot);
    ITrigPart2 trigPart2 = new TrigPart2(sec, csc);
    ILogPart1 logPart1 = new LogPart1(log2, log5);
    ILogPart2 logPart2 = new LogPart2(log10, log2, log5);
    ITrigComposite trigComposite = new TrigComposite(trigPart1, trigPart2); // Реальный
    ILogComposite logComposite = new LogComposite(logPart1, logPart2); // Реальный

    FunctionSystem system = new FunctionSystem(trigComposite, logComposite);

    // Теперь проверяем результаты всей системы для некоторых точек,
    // сравнивая с ожидаемыми значениями (можно взять из WolframAlpha или рассчитать вручную)

    // Пример: x = -PI/4
    // Мы уже считали части в предыдущем тесте:
    double secVal_pi4 = 1.0 / Math.cos(-Math.PI / 4); // ~1.414
    double cscVal_pi4 = 1.0 / Math.sin(-Math.PI / 4); // ~-1.414
    double cotVal_pi4 = Math.cos(-Math.PI / 4) / Math.sin(-Math.PI / 4); // -1.0
    double realTrigPart1_pi4 =
      (secVal_pi4 * secVal_pi4 - cscVal_pi4) / cotVal_pi4; // (2 - (-sqrt(2))) / -1 = -(2+sqrt(2)) ~ -3.414
    double realTrigPart2_pi4 = Math.pow(secVal_pi4 - cscVal_pi4, 2); // (sqrt(2) - (-sqrt(2)))^2 = (2*sqrt(2))^2 = 8
    double realTrigComposite_pi4 = Math.pow(
      realTrigPart1_pi4 * realTrigPart2_pi4,
      2
    ); // (-(2+sqrt(2)) * 8)^2 ~ (-27.31)^2 ~ 745.9
    // Сравним с нашим вычислением в TrigCompositeStub: 128*(3+2sqrt(2)) ~ 744.98. Небольшое расхождение из-за точности.
    // Используем значение, вычисленное здесь:
    assertEquals(
      realTrigComposite_pi4,
      system.compute(-Math.PI / 4, CALCULATION_PRECISION),
      COMPARISON_DELTA * 10
    ); // Увеличим дельту для композитной функции

    // Пример: x = 5
    double log2_5_real = Math.log(5) / Math.log(2); // ~2.3219
    double log5_5_real = 1.0;
    double log10_5_real = Math.log(5) / Math.log(10); // ~0.6989
    double realLogPart1_5 = Math.pow(log2_5_real * log5_5_real, 18); // (log2(5))^18 ~ 4.36e6
    double realLogPart2_5 = (log10_5_real / log2_5_real) + log5_5_real; // (log10(5)/log2(5)) + 1 ~ 0.301 + 1 = 1.301
    double expectedLogComposite_5 = realLogPart1_5 + realLogPart2_5;
    assertEquals(
      expectedLogComposite_5,
      system.compute(5.0, CALCULATION_PRECISION),
      COMPARISON_DELTA * expectedLogComposite_5
    ); // Дельта пропорциональна значению

    // Проверка точек разрыва / неопределенностей
    // x = 0 (trig branch, cot(0) is NaN -> TrigPart1 NaN -> TrigComposite NaN)
    assertTrue(Double.isNaN(system.compute(0.0, CALCULATION_PRECISION)));
    // x = -PI/2 (trig branch, cos=0 -> sec=NaN, cot=0 -> TrigPart1 NaN -> TrigComposite NaN)
    assertTrue(
      Double.isNaN(system.compute(-Math.PI / 2, CALCULATION_PRECISION))
    );
    // x = -PI (trig branch, sin=0 -> csc=NaN, cot=NaN -> TrigPart1 NaN -> TrigComposite NaN)
    assertTrue(Double.isNaN(system.compute(-Math.PI, CALCULATION_PRECISION)));
    // x = 1 (log branch, log2(1)=0 -> LogPart2 NaN -> LogComposite NaN)
    assertTrue(Double.isNaN(system.compute(1.0, CALCULATION_PRECISION)));
    // x = 0.5 (log branch, all logs defined) - should return a number
    assertFalse(Double.isNaN(system.compute(0.5, CALCULATION_PRECISION)));
  }
}
