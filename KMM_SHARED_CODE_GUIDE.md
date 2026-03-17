# Kotlin Multiplatform (KMM) — Общий код и интеграция

Этот документ описывает принципы создания и интеграции общего кода в KMM проектах, включая механизмы взаимодействия с платформами и управление памятью.

---

## 1. Структура Shared-модуля и expect/actual

Механизм `expect`/`actual` позволяет описывать интерфейс в общем коде, а реализацию — в платформенном.

### Пример (CommonMain):
```kotlin
// В commonMain описываем ожидаемую функциональность
expect fun getPlatformName(): String

class Greeting {
    fun greet(): String = "Привет от ${getPlatformName()}"
}
```

### Реализация (AndroidMain / IosMain):
```kotlin
// В androidMain
actual fun getPlatformName(): String = "Android ${android.os.Build.VERSION.SDK_INT}"

// В iosMain
actual fun getPlatformName(): String = UIDevice.currentDevice.systemName()
```

---

## 2. Управление памятью и ограничения шаринга

### Старая модель (Strict Freezing):
Ранее в Kotlin/Native существовало понятие **freezing** (заморозка). Объект, доступный из нескольких потоков, должен был быть неизменяемым (`freeze()`). Это создавало сложности при работе с корутинами и состоянием.

### Новая модель (New Memory Manager):
Начиная с Kotlin 1.7.20+, используется новая модель памяти, которая **убирает ограничения на заморозку**. 
*   Объекты можно свободно передавать между потоками.
*   Циклические ссылки обрабатываются сборщиком мусора корректно.
*   **Ограничение:** Важно следить за потокобезопасностью при обращении к изменяемым переменным из разных потоков (использовать атомики или блокировки).

---

## 3. Интеграция с iOS и Android

### Android Integration:
Shared-модуль подключается как обычная зависимость в `build.gradle.kts`:
```kotlin
implementation(project(":shared"))
```

### iOS Integration:
Есть два основных способа:
1.  **CocoaPods:** Плагин KMM генерирует `.podspec` файл. Xcode подтягивает код как зависимость CocoaPods.
2.  **XCFramework:** Модуль компилируется в бинарный фреймворк, который вручную или через скрипт добавляется в проект Xcode.

### Интеграция Flow в iOS:
Поскольку Swift не поддерживает Kotlin Flow напрямую, часто используются обертки (Wrappers) или библиотеки (например, `KMP-NativeCoroutines`), которые превращают Flow в `AsyncSequence` или `Combine Publisher`.

---

**Итог:** KMM позволяет выносить бизнес-логику (Networking, Database, UseCases) в единый код, оставляя UI нативными, что дает идеальный баланс между переиспользованием и качеством пользовательского опыта.
