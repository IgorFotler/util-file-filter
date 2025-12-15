# util-file-filter

Утилита для фитрации содержимого файлов

Сборка:
- java 17+
- maven 3.8+

Зависимости:
- lombok 1.18.34 - https://projectlombok.org/

Сборка jar-файла:
- mvn clean package

Запуск утилиты:
- java -jar util-file-filter-1.0-jar-with-dependencies.jar [OPTIONS] file1.txt file2.txt file3.txt

Опции:
-o [PATH] - путь для результатов
-p [PREFIX] - задание перфикса
-a - задание режима добавления записи в существующие файлы
-s - вывод короткой статистики
-f - вывод полной статистики