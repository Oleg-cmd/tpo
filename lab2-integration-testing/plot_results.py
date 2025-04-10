import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.ticker as mticker
import os
import numpy as np # Для обработки NaN/inf

# --- Настройки ---
# Словарь: Название графика -> Имя CSV файла
csv_files_to_plot = {
    "Полная система функций": "function_system_results.csv",
    "Функция Sin(x)": "sin_results.csv",
    "Функция Ln(x)": "ln_results.csv",
    # Например:
    # "Функция Cos(x)": "cos_results.csv",
    # "Функция LogN(x, 2)": "log2_results.csv", # Если генерируешь такой файл
    # "Функция LogN(x, 5)": "log5_results.csv",
    # "Функция LogN(x, 10)": "log10_results.csv",
    # "Триг. композиция": "trig_composite_results.csv",
    # "Лог. композиция": "log_composite_results.csv",
}
csv_delimiter = ';' # Разделитель, который ты использовал в Java
output_directory = "plots" # Папка для сохранения графиков
plot_style = 'seaborn-v0_8-whitegrid' # Стиль графиков

# --- Подготовка ---
# Создать папку для графиков, если ее нет
if not os.path.exists(output_directory):
    os.makedirs(output_directory)
    print(f"Создана директория: {output_directory}")

if plot_style in plt.style.available:
     plt.style.use(plot_style)
else:
    print(f"Предупреждение: Стиль '{plot_style}' не найден. Используется стиль по умолчанию.")
    plot_style = 'default' # Используем стиль по умолчанию, если выбранный недоступен

# --- Функция для установки разумных пределов оси Y ---
def set_reasonable_ylim(data_y):
    """Устанавливает пределы оси Y, игнорируя выбросы/бесконечности."""
    finite_data = data_y[np.isfinite(data_y)]
    if finite_data.empty:
        return # Нет конечных данных для установки пределов

    q1 = finite_data.quantile(0.05) # 5-й процентиль
    q3 = finite_data.quantile(0.95) # 95-й процентиль
    iqr = q3 - q1
    lower_bound = q1 - 1.5 * iqr
    upper_bound = q3 + 1.5 * iqr

    # Если разброс очень мал, установим пределы +/- 1 от среднего
    if abs(upper_bound - lower_bound) < 1e-6:
         mean_val = finite_data.mean()
         lower_bound = mean_val - 1
         upper_bound = mean_val + 1

    # Убедимся, что пределы не NaN или Inf (на всякий случай)
    if np.isfinite(lower_bound) and np.isfinite(upper_bound):
        plt.ylim(lower_bound, upper_bound)
    else:
        # Если что-то пошло не так, используем минимальный/максимальный конечные значения
        if not finite_data.empty:
             min_y = finite_data.min()
             max_y = finite_data.max()
             padding = (max_y - min_y) * 0.1 if max_y > min_y else 1.0
             plt.ylim(min_y - padding, max_y + padding)


# --- Построение графиков ---
print("Начинаем построение графиков...")
for title, filename in csv_files_to_plot.items():
    filepath = os.path.join(filename) # Файл ищем в текущей директории
    if not os.path.exists(filepath):
        print(f"\nПредупреждение: Файл '{filename}' не найден. Пропускаем.")
        continue

    print(f"\nОбработка файла: '{filename}'...")
    try:
        # Чтение CSV
        df = pd.read_csv(filepath, delimiter=csv_delimiter)

        # Определение имен столбцов (предполагаем, что они первые два)
        if len(df.columns) < 2:
            print(f"Ошибка: В файле '{filename}' меньше двух столбцов.")
            continue
        x_col = df.columns[0]
        y_col = df.columns[1]

        # Преобразование в числовые типы, ошибки превратятся в NaN
        df[x_col] = pd.to_numeric(df[x_col], errors='coerce')
        df[y_col] = pd.to_numeric(df[y_col], errors='coerce')

        # Удаление строк, где X не число
        df.dropna(subset=[x_col], inplace=True)

        if df.empty:
            print(f"Ошибка: В файле '{filename}' нет валидных данных после очистки.")
            continue

        # Сортировка по X для корректного отображения линии
        df.sort_values(by=x_col, inplace=True)

        # Создание графика
        fig, ax = plt.subplots(figsize=(12, 7)) # Создаем фигуру и оси

        # Отображение точек (всех данных)
        ax.scatter(df[x_col], df[y_col], s=10, alpha=0.6, label='Вычисленные точки')

        # Отображение линии, соединяющей только конечные точки
        # Создаем копию для линии, заменяя Inf/NaN на маркеры разрыва
        df_line = df.copy()
        df_line[y_col] = df_line[y_col].replace([np.inf, -np.inf], np.nan)
        # В matplotlib NaN создает разрывы в линии автоматически
        ax.plot(df_line[x_col], df_line[y_col], linestyle='-', linewidth=1.5, label='Соединяющая линия (конечн.)')

        # Настройка графика
        ax.set_title(title, fontsize=16)
        ax.set_xlabel("X", fontsize=12)
        ax.set_ylabel("f(X)", fontsize=12)
        ax.grid(True, which='both', linestyle='--', linewidth=0.5) # Сетка для основных и промежуточных делений
        ax.axhline(0, color='black', linewidth=0.7) # Ось X
        ax.axvline(0, color='black', linewidth=0.7) # Ось Y

        # Установка пределов оси Y для лучшей читаемости
        set_reasonable_ylim(df[y_col])

        # Настройка тиков (меток на осях)
        ax.xaxis.set_major_locator(mticker.AutoLocator())
        ax.xaxis.set_minor_locator(mticker.AutoMinorLocator())
        ax.yaxis.set_major_locator(mticker.AutoLocator())
        ax.yaxis.set_minor_locator(mticker.AutoMinorLocator())
        ax.tick_params(axis='both', which='major', labelsize=10)

        ax.legend(fontsize=10) # Легенда

        # Сохранение графика
        output_filename = os.path.join(output_directory, f"{filename.replace('.csv', '')}.png")
        plt.savefig(output_filename, dpi=150) # Сохраняем с хорошим разрешением
        print(f"График сохранен: {output_filename}")
        plt.close(fig) # Закрыть фигуру, чтобы освободить память

    except Exception as e:
        print(f"Ошибка при обработке файла '{filename}': {e}")

print("\nПостроение всех графиков завершено.")