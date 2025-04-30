import matplotlib.pyplot as plt
import numpy as np

# --- Данные из результатов JMeter ---
# Количество одновременных пользователей
users = [10, 15, 20]

# Время отклика (90-й перцентиль, мс) из колонки '90% Line'
response_time_90_percentile = [745, 820, 927]

# Требование по максимальному времени отклика (мс)
requirement_threshold = 810

# --- Построение графика ---
plt.figure(figsize=(10, 6)) # Размер графика

# Строим линию времени отклика
plt.plot(users, response_time_90_percentile, marker='o', linestyle='-', linewidth=2, markersize=8, label='Время отклика (90-й перцентиль)')

# Добавляем горизонтальную линию для требования
plt.axhline(y=requirement_threshold, color='red', linestyle='--', linewidth=1.5, label=f'Макс. допустимое время ({requirement_threshold} мс)')

# --- Настройка отображения ---
plt.xlabel('Количество одновременных пользователей', fontsize=12)
plt.ylabel('Время отклика (90-й перцентиль), мс', fontsize=12)
plt.title('Зависимость времени отклика от нагрузки', fontsize=14, fontweight='bold')

# Устанавливаем метки на оси X точно по нашим данным
plt.xticks(users)

# Устанавливаем разумные пределы для оси Y (начиная с 0)
plt.ylim(bottom=0, top=max(response_time_90_percentile) * 1.1) # Немного выше максимального значения

plt.grid(True, linestyle=':', alpha=0.7) # Добавляем сетку
plt.legend(fontsize=10) # Показываем легенду

# --- Отображаем график ---
plt.tight_layout() # Автоматически подгоняет элементы графика
plt.show()

# --- Текстовый вывод для отчета ---
print("\nАнализ результатов стресс-тестирования для отчета:")
print("-" * 40)
print(f"Тестируемая конфигурация: [Укажи номер конфигурации, которую ты выбрал на этапе нагрузочного теста]") # Не забудь указать!
print(f"Требование по максимальному времени отклика: {requirement_threshold} мс (для 90% запросов).")
print("\nРезультаты по точкам нагрузки:")
for i in range(len(users)):
    status = "УДОВЛЕТВОРЯЕТ" if response_time_90_percentile[i] <= requirement_threshold else "НЕ УДОВЛЕТВОРЯЕТ"
    print(f" - {users[i]} пользователей: 90-й перцентиль = {response_time_90_percentile[i]} мс ({status} требованиям)")

print(f"\nВывод:")
print(f"Система перестает стабильно удовлетворять требованиям по времени отклика ({requirement_threshold} мс)")
print(f"при нагрузке примерно в 11-14 одновременных пользователей.")
print(f"При 15 пользователях 90-й перцентиль времени отклика ({response_time_90_percentile[1]} мс) уже превышает допустимый порог,")
print("и наблюдается значительный рост процента ошибок (32.89%), что указывает на перегрузку системы.")
print("-" * 40)