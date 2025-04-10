package com.example.util;

import com.example.FunctionSystem; // Импортируем основную систему
import com.example.interfaces.ICalculable; // Или можем использовать базовый интерфейс
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale; // Для точки в качестве десятичного разделителя

public class CsvWriter {

  /**
   * Записывает результаты вычислений модуля (или всей системы) в CSV файл.
   *
   * @param function   Модуль или система для вычисления (реализует ICalculable).
   * @param filename   Имя выходного CSV файла.
   * @param startX     Начальное значение X.
   * @param endX       Конечное значение X.
   * @param step       Шаг изменения X.
   * @param precision  Точность вычислений для функции.
   * @param delimiter  Разделитель для CSV (например, "," или ";").
   * @throws IllegalArgumentException если step некорректен (0 или не соответствует направлению startX -> endX).
   */
  public static void writeResultsToCsv(
    ICalculable function, // Принимаем любой модуль через интерфейс
    String filename,
    double startX,
    double endX,
    double step,
    double precision,
    String delimiter
  ) throws IOException {
    // Проверка корректности шага
    if (step == 0) {
      throw new IllegalArgumentException("Step cannot be zero.");
    }
    if (startX < endX && step < 0) {
      throw new IllegalArgumentException(
        "Step must be positive if startX < endX."
      );
    }
    if (startX > endX && step > 0) {
      throw new IllegalArgumentException(
        "Step must be negative if startX > endX."
      );
    }
    if (
      precision <= 0 || Double.isNaN(precision) || Double.isInfinite(precision)
    ) {
      throw new IllegalArgumentException(
        "Precision must be positive and finite."
      );
    }

    // Используем try-with-resources для автоматического закрытия BufferedWriter
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      // Записываем заголовок
      writer.write("X" + delimiter + "Result");
      writer.newLine();

      // Итерируем по значениям X
      // Используем BigDecimal для шага, чтобы избежать накопления ошибок double?
      // Для простоты пока оставим double, но это потенциальное улучшение.
      double currentX = startX;

      // Условие цикла зависит от направления шага
      if (step > 0) {
        while (currentX <= endX) {
          writeLine(writer, function, currentX, precision, delimiter);
          currentX += step;
          // Предотвращение возможного зацикливания из-за ошибок округления double
          if (
            Math.abs(currentX - (currentX - step)) < Math.abs(step) / 1000.0
          ) {
            System.err.printf(
              "Warning: Step %f might be too small relative to X %f, potential infinite loop avoided.%n",
              step,
              currentX
            );
            break;
          }
        }
        // Убедимся, что последняя точка (endX) включена, если она пропущена из-за шага
        if (currentX - step < endX && startX <= endX) {
          writeLine(writer, function, endX, precision, delimiter);
        }
      } else { // step < 0
        while (currentX >= endX) {
          writeLine(writer, function, currentX, precision, delimiter);
          currentX += step; // step отрицательный, поэтому вычитание
          // Предотвращение возможного зацикливания
          if (
            Math.abs(currentX - (currentX - step)) < Math.abs(step) / 1000.0
          ) {
            System.err.printf(
              "Warning: Step %f might be too small relative to X %f, potential infinite loop avoided.%n",
              step,
              currentX
            );
            break;
          }
        }
        // Убедимся, что последняя точка (endX) включена
        if (currentX - step > endX && startX >= endX) {
          writeLine(writer, function, endX, precision, delimiter);
        }
      }
    } catch (IOException e) {
      System.err.println(
        "Error writing to CSV file '" + filename + "': " + e.getMessage()
      );
      throw e; // Пробрасываем исключение дальше
    }
  }

  /**
   * Вспомогательный метод для вычисления и записи одной строки в CSV.
   */
  private static void writeLine(
    BufferedWriter writer,
    ICalculable function,
    double x,
    double precision,
    String delimiter
  ) throws IOException {
    double result = function.calculate(x, precision);
    String resultString;

    if (Double.isNaN(result)) {
      resultString = "NaN";
    } else if (Double.isInfinite(result)) {
      resultString = (result > 0) ? "Infinity" : "-Infinity";
    } else {
      // Форматируем с использованием точки как разделителя и достаточным количеством знаков
      resultString = String.format(Locale.US, "%.10f", result);
    }

    String xString = String.format(Locale.US, "%.10f", x); // Форматируем X тоже

    writer.write(xString + delimiter + resultString);
    writer.newLine();
  }

  // Перегруженный метод для удобства, если хотим записывать именно FunctionSystem
  public static void writeResultsToCsv(
    FunctionSystem functionSystem,
    String filename,
    double startX,
    double endX,
    double step,
    double precision,
    String delimiter
  ) throws IOException {
    // Создаем обертку ICalculable для FunctionSystem
    ICalculable systemCalculator = (x, prec) -> functionSystem.compute(x, prec);

    writeResultsToCsv(
      systemCalculator,
      filename,
      startX,
      endX,
      step,
      precision,
      delimiter
    );
  }
}
