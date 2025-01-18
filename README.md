# Bayl - простой скриптовый яп с динамической типизацией

---
# Основные правила

- Все всегда начинается с названия и заканчивается `;`

  Примеры:
  ```js
  num = 1;
  str = "a";
  
  arr = [];
  dict = {};
  
  sum = function(a, b) {};
  ```
  
- массивы, словари и функции - ссылочные типы, а числа и строки - значимые.
---
# Переменные

Описание типов:

- number - для различных чисел

  - Десятичные числа:

    Формат: 123, 123.456, 123e10, 123.456e-2.
    Числа могут содержать дробную часть (.) и экспоненту (e или E) с необязательным знаком (+ или -).
  
  - Шестнадцатеричные числа:
  
    Формат: 0x1A3F, 0Xabc.
    Начинаются с 0x или 0X, за которыми следуют символы от 0 до 9 и от A до F (или их строчные аналоги).
  
  - Восьмеричные числа:
  
    Формат: 0o123, 0O777.
    Начинаются с 0o или 0O, за которыми следуют цифры от 0 до 7.

  - Двоичные числа:
  
    Формат: 0b1010, 0B1101.
    Начинаются с 0b или 0B, за которыми следуют цифры 0 или 1.

- string - для строк и символов

  формат: "s", 's', "string", 'string'

- array - для массивов

  формат: [], [элемент, элемент]

- dictionary - для словарей

  формат: {}, {ключ : значение}

- function - для функций

  формат: function() {}, function(аргумент1, аргумент2) {} 
---

# Операторы

Арифметические операторы:

- `+` Добавляет два операнда.
- `-` Вычитает второй операнд с первого.
- `*` Умножает оба операнда.
- `/` Делит числитель на де-числитель.
- `%` Оператор модуля и остаток после целочисленного деления.

Операторы сравнения:

- `==` Проверяет, равны ли значения двух операндов или нет, если да, то условие становится истинным.
- `>` Проверяет, превышает ли значение левого операнда значение правого операнда, если да, тогда условие становится истинным.
- `<` Проверяет, является ли значение левого операнда меньше значения правильного операнда, если да, тогда условие становится истинным.
- `=>` Проверяет, превышает ли значение левого операнда значение правого операнда, если да, тогда условие становится истинным.
- `<=` Проверяет, является ли значение левого операнда меньше или равно значению правильного операнда, если да, тогда условие становится истинным.

Логические операторы:

- `&&` Является логическим оператором `И`.
- `||` Является логическим оператором `ИЛИ`

Операторы для работы со строками:

- `~` Конкатенация двух строк

Операторы присваивания:

- `=` Простой оператор присваивания, присваивает значения из правых операндов в левый операнд.

---

# Циклы

Циклы обозначаются ключевым словом `while` и `foreach`.
  
- `while`:
  
  ```js
  while (i < 239) {
      println(i);
      i = i + 1;
  }
  ```
  
  В этом примере цикл будет продолжать выполняться до тех пор, пока `i` - значение меньше 239.


- `foreach`:

  ```js
  arr = [1, 2, 3];
  
  foreach(arr as i) {
      print(i);
  }
  ```

---

# Функции

Общая форма функции:

```bayl
название = function(аргумент1, аргумент2) {
    тело
    return то что нужно вернуть, если что-то нужно вернуть 
}
```

Пример функции:

```js
sum = function(a, b) {
  return a + b;  
};
```

# Коллекции

Методы массива:

```js
arr = [1, 2, 3];

arr_len(arr); // вернет 3
arr_push(arr, 4); // в arr будет 1, 2, 3, 4
print(arr); // выведет [1, 2, 3, 4]
```

Методы словаря:

```js
dict = {"1" : 1};
print(dict); // выведет {1=1}
```

---

# Комментарии

- однострочные
  ```js
  // коммент
  ```
- многострочные
  ```js
  /* комменты
  комменты */
  ```

# Встроенные функции

- `print` и `println` выводят то, что было передано как аргумент
  примеры:
  ```js
  println(239);
  print("239");
  ```
- `str_len` и `arr_len` выводят длину строки и массива соответственно
  примеры:
  ```js
  arr_len([1, 3, 4]); // вернет 3
  str_len("bayl"); // вернет 4
  ```
- `array_push` добавит элемент в конец массива
  пример:
  ```js
  arr = [1, 2];
  array_push(arr, 3); // в arr будет 1, 2, 3
  ```