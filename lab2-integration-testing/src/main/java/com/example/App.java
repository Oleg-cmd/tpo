package com.example;

import com.example.impl.*;
import com.example.interfaces.*;
import com.example.util.CsvWriter;
import java.io.IOException;

public class App {

  public static void main(String[] args) {
    // --- 1. Определение констант ---
    final double precision = 1E-7; // Заданная точность для рядов Тейлора
    final String systemOutputFile = "function_system_results.csv";
    final String sinOutputFile = "sin_results.csv"; // Пример для отдельного модуля
    final String lnOutputFile = "ln_results.csv"; // Пример для отдельного модуля
    final String log10OutputFile = "log10_results.csv"; // Пример для отдельного модуля
    final String delimiter = ";"; // Используем точку с запятой как разделитель

    // Диапазон и шаг для X
    final double startX = -5.0;
    final double endX = 5.0;
    final double step = 0.1;

    // --- 2. Инициализация реальных модулей ---
    // Базовые функции
    ISin sin = new Sin();
    ILn ln = new Ln();

    // Производные тригонометрические
    ICos cos = new Cos(sin);
    ISec sec = new Sec(cos);
    ICsc csc = new Csc(sin);
    ICot cot = new Cot(cos, sin);

    // Производные логарифмические
    ILog log2 = new LogN(ln, 2.0);
    ILog log5 = new LogN(ln, 5.0);
    ILog log10 = new LogN(ln, 10.0);

    // Составные части
    // Тригонометрические
    ITrigPart1 trigPart1 = new TrigPart1(sec, csc, cot);
    ITrigPart2 trigPart2 = new TrigPart2(sec, csc);
    ITrigComposite trigComposite = new TrigComposite(trigPart1, trigPart2);

    // Логарифмические
    ILogPart1 logPart1 = new LogPart1(log2, log5);
    ILogPart2 logPart2 = new LogPart2(log10, log2, log5);
    ILogComposite logComposite = new LogComposite(logPart1, logPart2);

    // --- 3. Инициализация системы функций ---
    FunctionSystem functionSystem = new FunctionSystem(
      trigComposite,
      logComposite
    );

    // --- 4. Генерация CSV для всей системы ---
    System.out.println("Generating CSV for the entire function system...");
    try {
      // Используем перегруженный метод для FunctionSystem
      CsvWriter.writeResultsToCsv(
        functionSystem,
        systemOutputFile,
        startX,
        endX,
        step,
        precision,
        delimiter
      );
      System.out.println(
        "Successfully wrote system results to " + systemOutputFile
      );
    } catch (IOException e) {
      System.err.println(
        "Failed to write system results CSV: " + e.getMessage()
      );
    } catch (IllegalArgumentException e) {
      System.err.println(
        "Configuration error for system CSV writing: " + e.getMessage()
      );
    }

    // --- 5. (Опционально) Генерация CSV для отдельных модулей ---
    System.out.println("\nGenerating CSV for individual modules (examples)...");

    // Пример для Sin
    try {
      CsvWriter.writeResultsToCsv(
        sin, // Передаем модуль Sin
        sinOutputFile,
        startX,
        endX,
        step,
        precision,
        delimiter
      );
      System.out.println("Successfully wrote Sin results to " + sinOutputFile);
    } catch (IOException e) {
      System.err.println("Failed to write Sin results CSV: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println(
        "Configuration error for Sin CSV writing: " + e.getMessage()
      );
    }

    // Пример для Ln (обратите внимание на область определения x > 0)
    try {
      CsvWriter.writeResultsToCsv(
        ln, // Передаем модуль Ln
        lnOutputFile,
        Math.max(startX, Math.nextUp(0.0)), // Начинаем с наименьшего положительного значения в диапазоне
        endX,
        step,
        precision,
        delimiter
      );
      System.out.println("Successfully wrote Ln results to " + lnOutputFile);
    } catch (IOException e) {
      System.err.println("Failed to write Ln results CSV: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println(
        "Configuration error for Ln CSV writing: " + e.getMessage()
      );
    }

    // Пример для Log10 (область определения x > 0)
    try {
      CsvWriter.writeResultsToCsv(
        log10, // Передаем модуль Log10
        log10OutputFile,
        Math.max(startX, Math.nextUp(0.0)), // Начинаем с наименьшего положительного значения в диапазоне
        endX,
        step,
        precision,
        delimiter
      );
      System.out.println(
        "Successfully wrote Log10 results to " + log10OutputFile
      );
    } catch (IOException e) {
      System.err.println(
        "Failed to write Log10 results CSV: " + e.getMessage()
      );
    } catch (IllegalArgumentException e) {
      System.err.println(
        "Configuration error for Log10 CSV writing: " + e.getMessage()
      );
    }

    System.out.println("\nApplication finished.");
  }
}
