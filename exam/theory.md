# Вопросы 1-14: теория

Краткие конспекты для ответа на зачете. Цифры и даты стоит запомнить в общих чертах, точный год редко требуется.

---

## 1. История развития мобильных операционных систем

- **1990-е, КПК.** Первые "умные" устройства: Apple Newton (1993), PalmOS (1996), Windows CE (1996). Вводятся стилус, органайзер, синхронизация с ПК.
- **Symbian** (1998-2000-е). Совместная разработка Nokia, Ericsson, Motorola. Первый массовый смартфон Ericsson R380 (2000). До 2010 года самая популярная ОС.
- **BlackBerry OS** (1999-2000-е). Почта, физическая клавиатура, корпоративный сегмент.
- **Windows Mobile** (2003), затем **Windows Phone** (2010) и **Windows 10 Mobile** (2015). Поддержка завершена в 2019-2020 годах.
- **iPhone и iOS** (2007). Мультитач-экран без стилуса, магазин App Store (2008).
- **Android** (2008). Компания Android Inc. куплена Google в 2005 году, первый телефон HTC Dream (T-Mobile G1). Открытый код, множество производителей.
- **2010-е.** Двухполюсный рынок: Android и iOS. Symbian, BlackBerry, Windows Phone уходят с рынка.
- **Сейчас.** Android занимает порядка 70% рынка, iOS порядка 28%. Развиваются жесты, ИИ-ассистенты, складные экраны, 5G.

## 2. Операционные системы Android, iOS, BlackBerry OS, Symbian OS, Microsoft Phone

| ОС | Разработчик | Ядро и язык | Особенности |
|---|---|---|---|
| **Android** | Google (Open Handset Alliance) | Ядро Linux; Kotlin, Java, C++ | Открытый код (AOSP), много производителей, магазин Google Play, виртуальная машина ART |
| **iOS** | Apple | Ядро XNU (Darwin); Swift, Objective-C | Закрытая, только на устройствах Apple, магазин App Store, строгая проверка приложений |
| **BlackBerry OS** | RIM (BlackBerry) | Собственная ОС, Java ME; позже QNX (BB10) | Защищенная почта (BES), физическая клавиатура, корпоративный рынок |
| **Symbian OS** | Symbian Ltd., затем Nokia | Собственное ядро, C++ | Работа на слабом железе, многозадачность, интерфейсы S60 и UIQ, закрыта Nokia в 2013 |
| **Windows Phone** | Microsoft | Ядро Windows NT; C#, XAML | Плиточный интерфейс Metro (Live Tiles), интеграция с сервисами Microsoft, свернута в 2019 |

## 3. Среды разработки под Android и iOS

**Android**
- **Android Studio** (основана на IntelliJ IDEA): редактор, эмулятор, профайлеры, Layout Inspector, Gradle.
- Языки: **Kotlin** (рекомендован Google с 2019), Java, C/C++ через NDK.
- Инструменты: Android SDK, `adb`, AVD Manager, Gradle, Logcat.
- Альтернативы: Flutter (Dart), React Native, Kotlin Multiplatform.

**iOS**
- **Xcode** (только macOS): редактор, Interface Builder, Simulator, Instruments.
- Языки: **Swift**, Objective-C.
- Интерфейс: SwiftUI (декларативный) или UIKit (классический).
- Для публикации нужна учетная запись Apple Developer.

## 4. Архитектура OS Android

Четыре-пять слоев снизу вверх:

1. **Ядро Linux.** Драйверы (экран, камера, Wi-Fi, Bluetooth), управление памятью и питанием, процессы, безопасность.
2. **Hardware Abstraction Layer (HAL).** Единый интерфейс между драйверами и системой.
3. **Нативные библиотеки и Android Runtime.** Библиотеки на C/C++: SQLite, OpenGL ES, WebKit/Chromium, libc, Media Framework. **ART** (Android Runtime) выполняет код приложений (раньше Dalvik): компиляция AOT и JIT.
4. **Java API Framework.** Менеджеры системы: Activity Manager, Window Manager, Package Manager, Content Providers, Notification Manager, Resource Manager, View System.
5. **Приложения.** Системные (телефон, контакты, браузер) и установленные пользователем.

Каждое приложение запускается в своем процессе под своим Linux-пользователем (песочница). Доступ к ресурсам выдают разрешения (permissions).

## 5. Структура разрабатываемых приложений под Android

Проект в Android Studio (см. этот репозиторий):

```
app/
  src/main/
    AndroidManifest.xml     описание приложения: компоненты, разрешения, иконка, стартовый экран
    java/ru/gpustore/app/   код на Kotlin
    res/
      layout/               XML-разметка экранов
      drawable/             изображения и векторы
      values/               строки, цвета, темы
      mipmap-*/             иконки приложения
  build.gradle.kts          настройки сборки модуля и зависимости
build.gradle.kts            общие настройки проекта
settings.gradle.kts         список модулей
gradle/libs.versions.toml   версии библиотек
```

**Четыре типа компонентов:**
- **Activity**: один экран с интерфейсом.
- **Service**: фоновая работа без интерфейса.
- **BroadcastReceiver**: реакция на системные события.
- **ContentProvider**: доступ к данным между приложениями.

**Жизненный цикл Activity:** `onCreate` → `onStart` → `onResume` (экран активен) → `onPause` → `onStop` → `onDestroy`. Поворот экрана пересоздает Activity, поэтому состояние хранят во ViewModel.

Связь компонентов: **Intent** (явный указывает класс, неявный описывает действие). Ресурсы доступны через класс `R` (`R.string.app_name`). Приложение собирается в APK или AAB.

## 6. Архитектура OS iOS

Четыре слоя ("слоеный пирог"):

1. **Core OS.** Ядро XNU, драйверы, безопасность, Bluetooth, Accelerate, внешние аксессуары.
2. **Core Services.** Основные сервисы: Foundation, Core Data, Core Location, Core Motion, SQLite, iCloud, сеть, StoreKit.
3. **Media.** Графика и звук: Core Graphics, Core Animation, Metal, AVFoundation, Core Audio, Core Image.
4. **Cocoa Touch.** Фреймворки интерфейса: UIKit, SwiftUI, уведомления, мультитач, AirDrop, многозадачность, доступность.

Приложения работают в песочнице, подпись кода обязательна, безопасность поддерживается Secure Enclave. Каждый слой использует только слои ниже себя.

## 7. Структура разрабатываемых приложений под iOS

Проект Xcode:

- **AppDelegate / SceneDelegate** (UIKit) или `@main struct App` (SwiftUI): точка входа, события жизненного цикла.
- **Info.plist**: настройки приложения (название, разрешения, ориентации).
- **View / ViewController**: экраны. Паттерн MVC (в UIKit) или MVVM (в SwiftUI).
- **Storyboard / XIB** или код SwiftUI: описание интерфейса.
- **Assets.xcassets**: изображения, иконки, цвета.
- **Models**: данные; Core Data или SwiftData для хранения.
- **Entitlements, подписи, профили**: права и сертификаты для запуска на устройстве.

Состояния приложения: Not running, Inactive, Active, Background, Suspended. Распространение: App Store через App Store Connect, TestFlight для тестов.

## 8. Модель OSI

Эталонная модель сетевого взаимодействия, 7 уровней (сверху вниз):

| № | Уровень | Задача | Примеры |
|---|---|---|---|
| 7 | Прикладной (Application) | Доступ приложений к сети | HTTP, FTP, SMTP, DNS |
| 6 | Представления (Presentation) | Формат данных, шифрование, сжатие | TLS/SSL, JPEG, ASCII |
| 5 | Сеансовый (Session) | Установка и завершение сеансов | RPC, NetBIOS |
| 4 | Транспортный (Transport) | Надежная доставка, порты | TCP, UDP |
| 3 | Сетевой (Network) | Адресация и маршрутизация | IP, ICMP, маршрутизатор |
| 2 | Канальный (Data Link) | Передача кадров, MAC-адреса | Ethernet, Wi-Fi, коммутатор |
| 1 | Физический (Physical) | Передача битов по среде | кабель, радиосигнал, хаб |

Единицы данных: данные (уровни 5-7) → сегмент/дейтаграмма (4) → пакет (3) → кадр (2) → биты (1).

## 9. Модель TCP/IP

Практическая модель интернета, 4 уровня:

| Уровень | Соответствует OSI | Протоколы |
|---|---|---|
| Прикладной | 5-7 | HTTP/HTTPS, FTP, SMTP, DNS, DHCP, SSH |
| Транспортный | 4 | TCP (надежный, с подтверждением, соединение), UDP (быстрый, без гарантий) |
| Межсетевой (Internet) | 3 | IP (IPv4, IPv6), ICMP, ARP |
| Доступа к сети (канальный) | 1-2 | Ethernet, Wi-Fi |

Отличия от OSI: меньше уровней, модель появилась раньше и описывает реальные протоколы, а OSI это теоретический эталон. В Android сетевые запросы делают через HTTP-клиенты (OkHttp, Retrofit, HttpURLConnection), для этого нужно разрешение `INTERNET`.

## 10. Жизненный цикл разработки мобильных приложений

Этапы:

1. **Идея и анализ.** Целевая аудитория, конкуренты, требования, бизнес-модель.
2. **Проектирование.** Архитектура, прототип, дизайн интерфейса (Figma), выбор платформы и технологий.
3. **Разработка.** Код, база данных, API, интеграции. Гибкие методологии (Scrum, итерации).
4. **Тестирование.** Модульные тесты, интерфейсные тесты, ручное тестирование на разных устройствах и версиях ОС, бета-тест.
5. **Публикация.** Google Play Console, App Store Connect: описание, скриншоты, политика конфиденциальности, модерация.
6. **Поддержка и развитие.** Исправление ошибок, обновления, аналитика, отзывы, новые версии ОС.

В этих лабораторных пройдены этапы 2 (макет в Figma), 3 (Kotlin) и 4 (тесты).

## 11. Технология Wi-Fi при разработке мобильных приложений

- **Wi-Fi** это семейство стандартов IEEE 802.11 (a/b/g/n/ac/ax). Работает в диапазонах 2,4 и 5 ГГц (Wi-Fi 6E: 6 ГГц).
- Режимы: инфраструктурный (через точку доступа), ad-hoc.
- В Android за Wi-Fi отвечают `WifiManager` (сканирование сетей, состояние), `ConnectivityManager` (тип соединения, проверка интернета), `NetworkCallback` (подписка на изменения сети), `WifiNetworkSpecifier` и `WifiNetworkSuggestion` (подключение к сети).
- Разрешения: `ACCESS_WIFI_STATE`, `CHANGE_WIFI_STATE`, `ACCESS_FINE_LOCATION` или `NEARBY_WIFI_DEVICES` (для сканирования), `INTERNET`, `ACCESS_NETWORK_STATE`.
- С Android 10 нельзя включать Wi-Fi программно: пользователь делает это сам.
- Полезно: проверять тип сети перед загрузкой больших файлов, не блокировать интерфейс при потере связи, кэшировать данные локально (как в этом приложении через Room).

## 12. Технология Wi-Fi Direct для Android

- **Wi-Fi Direct** (Wi-Fi P2P) позволяет устройствам соединяться напрямую, без точки доступа. Скорость выше Bluetooth, дальность до 100-200 м.
- Одно из устройств становится **Group Owner** (играет роль точки доступа), остальные подключаются к нему.
- API: пакет `android.net.wifi.p2p`. Главные классы: `WifiP2pManager`, `WifiP2pManager.Channel`, `WifiP2pDevice`, `WifiP2pInfo`, `WifiP2pConfig`.
- Порядок работы:
  1. Получить `WifiP2pManager`, создать канал `initialize()`.
  2. Зарегистрировать `BroadcastReceiver` на события: `WIFI_P2P_STATE_CHANGED_ACTION`, `WIFI_P2P_PEERS_CHANGED_ACTION`, `WIFI_P2P_CONNECTION_CHANGED_ACTION`, `WIFI_P2P_THIS_DEVICE_CHANGED_ACTION`.
  3. Искать устройства: `discoverPeers()`, получить список через `requestPeers()`.
  4. Подключиться: `connect(channel, config, listener)`.
  5. Узнать адрес владельца группы: `requestConnectionInfo()`, затем обмен данными через сокеты (`ServerSocket` / `Socket`).
- Разрешения: `ACCESS_WIFI_STATE`, `CHANGE_WIFI_STATE`, `INTERNET`, `NEARBY_WIFI_DEVICES` (Android 13+) или `ACCESS_FINE_LOCATION`.
- Применение: передача файлов, игры по локальной сети, обмен данными без интернета.

## 13. Технология Bluetooth и Bluetooth LE

**Bluetooth Classic**
- Диапазон 2,4 ГГц, радиус обычно до 10 м, скорость до 2-3 Мбит/с.
- Подходит для потоковых данных: аудио (A2DP), гарнитура, передача файлов.
- В Android: `BluetoothAdapter`, `BluetoothDevice`, `BluetoothSocket` (обмен данными по RFCOMM), `BluetoothServerSocket`. Поиск `startDiscovery()`, сопряжение (pairing).

**Bluetooth Low Energy (BLE, Bluetooth 4.0+)**
- Очень низкое энергопотребление, небольшие порции данных.
- Модель **GATT**: устройство-сервер (peripheral) предоставляет **сервисы**, внутри них **характеристики** (значения) и **дескрипторы**, каждый с UUID.
- Роли: **Central** (сканирует и подключается, обычно телефон), **Peripheral** (рекламируется: фитнес-браслет, датчик).
- В Android: `BluetoothLeScanner`, `BluetoothGatt`, `BluetoothGattCallback`, `BluetoothGattCharacteristic`. Читать, писать, подписываться на уведомления.
- Применение: фитнес-трекеры, пульсометры, умный дом, маячки (beacons).

**Разрешения:** `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, `BLUETOOTH_ADVERTISE` (Android 12+); для старых версий `BLUETOOTH`, `BLUETOOTH_ADMIN`, `ACCESS_FINE_LOCATION`.

| | Classic | LE |
|---|---|---|
| Энергопотребление | выше | очень низкое |
| Скорость | выше | ниже |
| Данные | потоки, аудио | небольшие значения |
| Подключение | дольше | быстрое |

## 14. Библиотека MultipeerConnectivity для iOS

- Фреймворк Apple для **прямого обмена между устройствами** поблизости, без сервера. Использует Wi-Fi, одноранговый Wi-Fi и Bluetooth, выбирая подходящее само.
- Основные классы:
  - `MCPeerID`: идентификатор устройства в сети.
  - `MCSession`: сеанс, через него передаются данные.
  - `MCNearbyServiceAdvertiser` / `MCAdvertiserAssistant`: объявляет, что устройство доступно.
  - `MCNearbyServiceBrowser` / `MCBrowserViewController`: ищет устройства.
  - Делегаты (`MCSessionDelegate` и другие) сообщают о подключении, получении данных, потоков и файлов.
- Что можно передавать: данные (`Data`), потоки (`InputStream`), файлы (ресурсы).
- Применение: обмен файлами, многопользовательские игры, чаты без интернета, AirDrop-подобные функции.
- Ограничения: только для устройств Apple; на iOS 14+ нужны описание в `Info.plist` (`NSLocalNetworkUsageDescription`, `NSBonjourServices`).
- Аналог на Android: Wi-Fi Direct и Nearby Connections API.
