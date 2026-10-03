# Вопросы 15-26: Kotlin

Каждая тема объяснена коротко и показана на примере из этого приложения.
Файлы указаны относительно `app/src/main/java/ru/gpustore/app/`.

## 15. Переменные

Две формы объявления:

- `val` задает значение один раз, потом оно не меняется (как final в Java).
- `var` можно переприсваивать.

Тип обычно выводится сам (вывод типов), но его можно записать явно.

```kotlin
val name = "GeForce RTX 4090"      // String, неизменяемая
var quantity: Int = 1              // Int, изменяемая
quantity = 2
```

В приложении: `data/SeedData.kt` (все поля у `GraphicsCard` объявлены как `val`), `ui/ViewModels.kt` (`val selectedTab = MutableStateFlow(0)`: сама ссылка неизменна, а значение внутри меняется). Правило: по умолчанию пишем `val`, `var` только когда без него нельзя.

`const val` это константа времени компиляции: `const val EXTRA_CARD_ID = "card_id"` в `ItemActivity`.
`lateinit var` откладывает инициализацию: `private lateinit var binding` в Activity (значение появится в `onCreate`).
`by lazy` вычисляет значение при первом обращении: `private val database by lazy { ... }` в `StoreApp.kt`.

## 16. Типы данных

| Группа | Типы |
|---|---|
| Целые | `Byte`, `Short`, `Int`, `Long` |
| Дробные | `Float`, `Double` |
| Символ и логический | `Char`, `Boolean` |
| Строка | `String` |
| Особые | `Any` (корень иерархии), `Unit` (аналог void), `Nothing` |

В Kotlin нет примитивов в коде: все типы это объекты, но компилятор сам превращает их в примитивы JVM, где возможно. Автоматического расширения нет: `Int` в `Long` нужно преобразовывать явно (`x.toLong()`).

Строковые шаблоны: `"$brand, $memoryGb ГБ $memoryType"` в `GraphicsCard.shortSpec`.
Числовой литерал с подчеркиванием: `189_990` в `SeedData.kt`.

## 17. Условные операторы

`if` в Kotlin это выражение, оно возвращает значение, поэтому тернарного оператора нет:

```kotlin
val text = if (count == 0) "Корзина пуста" else "Товаров в корзине: $count"
```

Пример из `ui/MenuActivity.kt`: выбор подписи по числу товаров.

`when` заменяет switch и мощнее его: принимает значения, диапазоны, типы, условия.

```kotlin
fun brandArt(brand: String): Int = when (brand) {
    "AMD" -> R.drawable.gpu_amd
    "Intel" -> R.drawable.gpu_intel
    else -> R.drawable.gpu_nvidia
}
```

Это `util/BrandArt.kt`. Диапазоны: `in 1..10`, `!in`. Сравнения `==` сравнивают содержимое (equals), `===` сравнивает ссылки.

## 18. Nullable

Обычный тип не может хранить `null`. Чтобы разрешить, добавляют `?`: `String?`.

| Конструкция | Смысл |
|---|---|
| `a?.length` | безопасный вызов: вернет `null`, если `a` равно `null` |
| `a ?: "по умолчанию"` | элвис-оператор: значение справа, если слева `null` |
| `a!!` | утверждение "не null", бросит исключение, если это не так (избегать) |
| `a?.let { ... }` | выполнить блок только если не `null` |
| `if (a != null)` | после проверки компилятор считает `a` не-null (smart cast) |

В приложении: `val card = state.card ?: return@collect` в `ItemActivity` (если товар еще не загружен, выходим). `Flow<GraphicsCard?>` в `StoreDao.observeCard`, потому что строки с таким id может не быть. `it?.toString().orEmpty()` в `MainActivity` при чтении поля поиска.

## 19. Циклы

```kotlin
for (i in 1..5) { }              // от 1 до 5 включительно
for (i in 0 until 5) { }         // от 0 до 4
for (i in 10 downTo 0 step 2) { }
for (card in cards) { }          // по коллекции
for ((index, ch) in digits.withIndex()) { }
while (x > 0) { }
do { } while (x > 0)
```

В приложении: `for ((i, ch) in digits.withIndex())` в `util/PriceFormat.kt` расставляет пробелы в цене. Для коллекций чаще используют функции вместо циклов: `forEach`, `map`, `filter`, `sumOf`.

`break` и `continue` работают как в Java, у них есть метки: `outer@ for (...) { ... break@outer }`.

## 20. Массивы

Массив имеет фиксированный размер. Создание:

```kotlin
val a = arrayOf(1, 2, 3)
val b = IntArray(5)                  // [0, 0, 0, 0, 0]
val c = Array(3) { i -> i * i }      // [0, 1, 4]
```

Основные методы: `size`, `a[0]`, `a[0] = 5`, `indices`, `contains`, `indexOf`, `sort()`, `sortedArray()`, `reversed()`, `sum()`, `max()`, `min()`, `joinToString()`, `copyOf()`, `copyOfRange()`, `toList()`, `map`, `filter`.

В приложении массивы встречаются в аннотациях: `entities = [GraphicsCard::class, CartItem::class]` в `StoreDatabase.kt` и `foreignKeys = [...]` в `CartItem.kt`. В обычном коде удобнее списки.

## 21. Списки

`List` только для чтения, `MutableList` можно менять:

```kotlin
val fixed = listOf("AMD", "Intel")                // нельзя добавлять
val editable = mutableListOf("AMD")
editable.add("NVIDIA")
```

Методы: `add`, `remove`, `removeAt`, `get`/`[i]`, `size`, `isEmpty`, `first`, `last`, `contains`, `indexOf`, `sorted`, `sortedBy`, `reversed`, `take`, `drop`, `filter`, `map`, `any`, `all`, `count`, `sumOf`, `groupBy`, `joinToString`.

В приложении: `listOf(GraphicsCard(...), ...)` в `data/SeedData.kt`. `lines.sumOf { it.lineTotal }` в `ViewModels.kt` считает итог корзины. `dao.searchCards("").first().take(2).map { it.id }` в тесте `StoreDaoTest`.

## 22. Словари

`Map` хранит пары ключ-значение:

```kotlin
val prices = mapOf("RTX 4090" to 189_990, "RX 7600" to 27_990)   // только чтение
val stock = mutableMapOf<String, Int>()
stock["RTX 4090"] = 5
```

Методы: `get`/`[key]` (вернет `null`, если ключа нет), `getOrDefault`, `containsKey`, `containsValue`, `keys`, `values`, `entries`, `put`, `remove`, `size`, `filter`, `map`, `forEach`. Перебор: `for ((k, v) in map)`.

В приложении `Map` явно не используется, но по смыслу на нем построено `Intent.putExtra(key, value)`: данные передаются между экранами парами ключ-значение (`EXTRA_CARD_ID` это ключ). Если нужен пример для ответа, можно сгруппировать карты: `SeedData.cards.groupBy { it.brand }` вернет `Map<String, List<GraphicsCard>>`.

## 23. Функции

```kotlin
fun formatPrice(price: Int): String { ... }          // обычная
fun double(x: Int) = x * 2                           // однострочная
fun greet(name: String = "гость") { }                // значение по умолчанию
greet(name = "Егор")                                 // именованный аргумент
fun sum(vararg n: Int) = n.sum()                     // переменное число аргументов
val square: (Int) -> Int = { it * it }               // лямбда
```

Функции высшего порядка принимают другие функции: `CardAdapter(onClick: (GraphicsCard) -> Unit)` в `ui/Adapters.kt`.
Функции-расширения добавляют метод существующему классу: `fun View.applySystemBarsPadding()` в `util/Insets.kt`, `val Activity.repository` в `ui/Factories.kt`.
`suspend fun` это функция корутины, которая может приостанавливаться: все методы `StoreRepository`, меняющие данные.

## 24. Исключения

Все исключения непроверяемые: `throws` писать не нужно. Конструкция:

```kotlin
try {
    val n = text.toInt()
} catch (e: NumberFormatException) {
    println("Не число")
} finally {
    println("Выполнится всегда")
}
```

`try` это выражение: `val n = try { text.toInt() } catch (e: Exception) { 0 }`.
Бросить исключение: `throw IllegalArgumentException("Количество меньше нуля")`. Проверки: `require(x > 0)`, `check(state)`, `error("...")`. Безопасное преобразование без исключения: `toIntOrNull()`.

В приложении исключения обрабатывает Room и корутины: если запрос к базе завершится ошибкой, она всплывет в `viewModelScope`. Явно: уникальность корзины решена не через исключение, а через `OnConflictStrategy.IGNORE` и счетчик измененных строк в `StoreDao.addToCart`. Это хороший пример ответа на вопрос "как избежать исключения проверкой".

## 25. Классы и объекты

```kotlin
class Cat(val name: String, var age: Int) {        // первичный конструктор
    fun meow() = println("$name: мяу")
}
```

Виды:

- `data class` генерирует `equals`, `hashCode`, `toString`, `copy`, `componentN`: `GraphicsCard`, `CartItem`, `CartLine`.
- `object` это синглтон: `object SeedData`, `private object CardDiff`.
- `companion object` хранит статические члены класса: `StoreDatabase.create(...)`, `ItemActivity.EXTRA_CARD_ID`.
- `abstract class` нельзя создать напрямую: `StoreDao`, `StoreDatabase`.
- `interface` описывает контракт.
- `sealed class` задает закрытый набор наследников.
- `enum class` перечисление.

По умолчанию классы закрыты для наследования (`final`). Чтобы наследовать, нужно `open`. Наследование: `class MainActivity : AppCompatActivity()`, переопределение: `override fun onCreate(...)`.
Свойства с геттером: `val shortSpec: String get() = "..."` в `GraphicsCard`.
Видимость: `public` (по умолчанию), `private`, `protected`, `internal`.
Делегаты свойств: `by viewModels { ... }`, `by lazy { ... }`.

## 26. Переопределение и перегрузка операторов

Переопределение (override) меняет поведение унаследованного метода. В Kotlin и метод, и класс должны быть открыты, а переопределение помечается словом `override`: `override fun onCreate(savedInstanceState: Bundle?)`, `override fun onBindViewHolder(...)` в `ui/Adapters.kt`.

Перегрузка функций: одинаковое имя, разные параметры.

```kotlin
fun show(x: Int) { }
fun show(x: String) { }
```

Перегрузка операторов: для своего класса можно задать смысл операторов через функции с ключевым словом `operator`.

| Оператор | Функция |
|---|---|
| `a + b` | `plus` |
| `a - b` | `minus` |
| `a * b` | `times` |
| `a == b` | `equals` |
| `a[i]` | `get` |
| `a[i] = v` | `set` |
| `a in b` | `contains` |
| `a < b` | `compareTo` |
| `-a` | `unaryMinus` |

```kotlin
data class Money(val rub: Int) {
    operator fun plus(other: Money) = Money(rub + other.rub)
    operator fun times(n: Int) = Money(rub * n)
}
val total = Money(100) + Money(50) * 2        // Money(200)
```

В приложении `==` для `data class` уже переопределен автоматически: `a == b` в `areContentsTheSame` из `ui/Adapters.kt` сравнивает все поля двух `GraphicsCard`. Операторы `[]` и `+` используются в `Map` и строках из стандартной библиотеки.
