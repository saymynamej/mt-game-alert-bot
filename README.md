# 🏆 MT Game Alert Bot (ABL League Monitoring & Poll Bot)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org)
[![Liquibase](https://img.shields.io/badge/Liquibase-4.x-2962FF?logo=liquibase&logoColor=white)](https://www.liquibase.org)
[![Telegram Bot API](https://img.shields.io/badge/Telegram%20Bot%20API-10.3.0-24A1DE?logo=telegram&logoColor=white)](https://core.telegram.org/bots/api)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**MT Game Alert Bot** — автоматизированный сервис на базе **Kotlin** и **Spring Boot** для регулярного мониторинга расписания матчей спортивной лиги (ABL / MT-Game) и автоматического создания открытых (неанонимных) опросов в Telegram-чатах команд для сбора игрового состава.

---

## 📌 Оглавление
- [✨ Возможности](#-возможности)
- [🛠 Технологический стек](#-технологический-стек)
- [⚙️ Переменные окружения (Environment Variables)](#️-переменные-окружения-environment-variables)
  - [Таблица переменных](#таблица-переменных)
  - [Формат конфигурации команд (`MT_GAME_TEAMS`)](#формат-конфигурации-команд-mt_game_teams)
  - [Пример файла `.env`](#пример-файла-env)
- [🏗 Архитектура и принцип работы](#-архитектура-и-принцип-работы)
- [🚀 Сборка и запуск](#-сборка-и-запуск)
  - [1. Локальное окружение (Docker Compose)](#1-локальное-окружение-docker-compose)
  - [2. Сборка проекта](#2-сборка-проекта)
  - [3. Запуск приложения](#3-запуск-приложения)
- [🧪 Тестирование](#-тестирование)
- [📝 Лицензия](#-лицензия)

---

## ✨ Возможности

- 🔄 **Автоматический мониторинг расписания:** Регулярный опрос API лиги (по расписанию через Cron каждые 5 минут, а также сразу при старте приложения).
- 🏋️‍♂️ **Оповещения о регулярных тренировках:** Автоматическое создание опросов для тренировок с указанием дня недели, времени, места и настраиваемым интервалом заблаговременного уведомления (`notifyDaysBefore`).
- 🔁 **Принудительные повторные уведомления (Force Send):** Возможность повторной отправки опроса по событию при выставлении флага `force_send = true` в базе данных.
- 📊 **Интерактивные открытые опросы в Telegram:** Автоматическая отправка опроса в чат команды (*«Будешь играть?»* с вариантами *«Буду» / «Не буду» / «Думаю»*). Опрос создается открытым (не анонимным), что позволяет тренерам и капитанам видеть поименный состав.
- 🛡️ **Защита от дубликатов сообщений:** Сервис фиксирует факт уведомления в базе данных PostgreSQL (или файловом хранилище), гарантируя отсутствие повторного спама в командные чаты.
- 👥 **Мультикомандность:** Гибкая настройка списка отслеживаемых команд и привязанных к ним Telegram-чатов через единую JSON-конфигурацию.
- 🗄️ **Автоматические миграции БД:** Структура базы данных управляется через Liquibase.

---

## 🛠 Технологический стек

### Основной стек (Backend & Core)
- **Язык разработки:** [Kotlin 2.3.20](https://kotlinlang.org/) (JVM-таргет: Java 25 / 1.8 совместимость)
- **Фреймворк:** [Spring Boot 4.1.1](https://spring.io/projects/spring-boot)
  - `spring-boot-starter-web` — базовая веб-инфраструктура и `RestClient`
  - `spring-boot-starter-data-jdbc` / `spring-boot-starter-jdbc` — работа с базой данных
  - `spring-boot-starter-liquibase` — управление структурой и версионированием базы данных
  - `spring-boot-context` — планировщик задач (`@EnableScheduling`, `@Scheduled`)
- **База данных:** [PostgreSQL 16](https://www.postgresql.org/) (HikariCP пул соединений)
- **Миграции БД:** [Liquibase](https://www.liquibase.org/) (XML ChangeLogs)
- **Telegram SDK:** [TelegramBots Spring Boot Starter 10.3.0](https://github.com/rubenlagus/TelegramBots) (`telegrambots-client`, Long Polling / Bot API)
- **Сериализация / JSON:** [Jackson Kotlin Module](https://github.com/FasterXML/jackson-module-kotlin)
- **Сборщик проекта:** [Apache Maven](https://maven.apache.org/) (`kotlin-maven-plugin`, `spring-boot-maven-plugin`)

### Тестирование и инфраструктура
- **Тестовый фреймворк:** [JUnit 5 (Jupiter)](https://junit.org/junit5/) + `kotlin-test-junit5`
- **Мокирование:** [Mockito](https://site.mockito.org/) & `mockito-junit-jupiter`
- **Интеграционное тестирование БД:** [Testcontainers PostgreSQL 1.21.4](https://testcontainers.com/modules/postgresql/)
- **Мокирование внешних HTTP API:** [WireMock Standalone 3.10.0 / 3.13.2](https://wiremock.org/)
- **Асинхронные проверки:** [Awaitility Kotlin](http://www.awaitility.org/)
- **Контейнеризация:** [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)
- **CI/CD:** [GitHub Actions](https://github.com/features/actions) (`maven-verify.yml`)

---

## ⚙️ Переменные окружения (Environment Variables)

Конфигурация приложения полностью вынесена в переменные окружения. Ниже представлена подробная таблица всех используемых переменных, их назначение и примеры значений:

### Таблица переменных

| Переменная          | Обязательная | Описание                                                                                                                                                                                                     | Пример значения                                                                                                                                                |
|:--------------------|:------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `BOT_TOKEN`         |    **Да**    | Секретный API-токен Telegram-бота, полученный у [@BotFather](https://t.me/BotFather). Используется для авторизации запросов к Telegram Bot API.                                                              | `123456789:ABCdefGhIJKlmNoPQRsTUVwxyZ`                                                                                                                         |
| `TELEGRAM_SCHEMA`   |    **Да**    | Протокол для обращения к API Telegram (`https` для продакшна, `http` для локального мок-сервера/WireMock).                                                                                                   | `https`                                                                                                                                                        |
| `TELEGRAM_HOST`     |    **Да**    | Хост Telegram Bot API. В штатном режиме — официальный API Telegram, для тестов — локальный хост.                                                                                                             | `api.telegram.org`                                                                                                                                             |
| `TELEGRAM_PORT`     |    **Да**    | Порт Telegram Bot API (`443` для HTTPS, либо кастомный порт прокси/мока).                                                                                                                                    | `443`                                                                                                                                                          |
| `MT_GAME_URL`       |    **Да**    | Шаблон URL внешнего API лиги для получения списка игр. Содержит два спецификатора `%s`: первый подставляется как `team_id`, второй — начальная дата выборки (`date_from` в формате `yyyy-MM-dd'T'HH:mm:ss`). | `http://localhost:8089/api/v1/league/2/games/?team_id=%s&date_from=%s`                                                                                         |
| `MT_GAME_TEAMS`     |    **Да**    | JSON-строка со словарём отслеживаемых команд, их статусом активности, идентификатором команды в лиге и ID целевого Telegram-чата.                                                                            | `{"MyTeam":{"enabled":true,"teamId":"10000","chatId":"-1003796928479"}}`                                                                                       |
| `TRAININGS`         |    **Да**    | JSON-строка со словарём расписания регулярных тренировок, местом, временем, днём недели и интервалом заблаговременного создания опроса.                                                                      | `{"MondayTraining":{"place":"СК Торпедо","chatId":"-1003796928479","enabled":true,"day":"MONDAY","startTime":"20:00","endTime":"21:30","notifyDaysBefore":2}}` |
| `DATABASE_URL`      |    **Да**    | JDBC-строка подключения к базе данных PostgreSQL.                                                                                                                                                            | `jdbc:postgresql://localhost:5432/mt_game_alert_bot`                                                                                                           |
| `DATABASE_USERNAME` |    **Да**    | Имя пользователя базы данных PostgreSQL.                                                                                                                                                                     | `postgres`                                                                                                                                                     |
| `DATABASE_PASSWORD` |    **Да**    | Пароль пользователя базы данных PostgreSQL.                                                                                                                                                                  | `postgres`                                                                                                                                                     |

---

### Формат конфигурации команд (`MT_GAME_TEAMS`)

Переменная `MT_GAME_TEAMS` передаётся в виде JSON-объекта, где ключ — произвольное название команды, а значение — объект со свойствами:

```json
{
  "Team Alpha": {
    "enabled": true,
    "teamId": "10000",
    "chatId": "-1003796928479"
  },
  "Team Beta": {
    "enabled": false,
    "teamId": "10001",
    "chatId": "-1009876543210"
  }
}
```

- `teamId` *(String)* — идентификатор команды во внешнем API лиги.
- `chatId` *(String)* — уникальный идентификатор Telegram-чата (группы / супергруппы / канала), куда бот будет отправлять опросы.
- `enabled` *(Boolean)* — флаг активности мониторинга для данной команды (`true` — опрашивать и уведомлять, `false` — игнорировать).

---

### Формат конфигурации тренировок (`TRAININGS`)

Переменная `TRAININGS` задаётся в виде JSON-объекта, где ключ — идентификатор/название тренировки, а значение — объект со свойствами:

```json
{
  "Main Training": {
    "place": "СК Торпедо, ул. Ленина 10",
    "chatId": "-1003796928479",
    "enabled": true,
    "day": "MONDAY",
    "startTime": "20:00",
    "endTime": "21:30",
    "notifyDaysBefore": 2
  },
  "Weekend Training": {
    "place": "Зал №2",
    "chatId": "-1003796928479",
    "enabled": true,
    "day": "SATURDAY",
    "startTime": "11:00",
    "endTime": "13:00",
    "notifyDaysBefore": 1
  }
}
```

- `place` *(String)* — место проведения тренировки, отображаемое в тексте опроса.
- `chatId` *(String)* — уникальный идентификатор Telegram-чата для отправки опроса.
- `enabled` *(Boolean)* — активность тренировки (`true` — создавать опрос по расписанию, `false` — пропустить).
- `day` *(String)* — день недели (значение enum `java.time.DayOfWeek`: `MONDAY`, `TUESDAY`, `WEDNESDAY`, `THURSDAY`, `FRIDAY`, `SATURDAY`, `SUNDAY`).
- `startTime` *(String)* — время начала тренировки в формате `HH:mm` (например, `"20:00"`).
- `endTime` *(String)* — время окончания тренировки в формате `HH:mm` (например, `"21:30"`).
- `notifyDaysBefore` *(Int)* — за сколько дней до даты тренировки бот должен создать опрос в чате (например, `2` означает, что опрос для тренировки в понедельник будет отправлен в субботу).

---

### Пример файла `.env`

Создайте файл `.env` в корневой директории или экспортируйте переменные в окружение:

```env
# Telegram Bot Credentials
BOT_TOKEN=123456789:ABCdefGhIJKlmNoPQRsTUVwxyZ
TELEGRAM_SCHEMA=https
TELEGRAM_HOST=api.telegram.org
TELEGRAM_PORT=443

# League API Settings
MT_GAME_URL=http://localhost:8089/api/v1/league/2/games/?team_id=%s&date_from=%s

# Teams Mapping (JSON)
MT_GAME_TEAMS={"AlphaTeam":{"enabled":true,"teamId":"10000","chatId":"-1003796928479"}}

# Trainings Schedule (JSON)
TRAININGS={"MondayTraining":{"place":"СК Торпедо","chatId":"-1003796928479","enabled":true,"day":"MONDAY","startTime":"20:00","endTime":"21:30","notifyDaysBefore":2}}

# PostgreSQL Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/mt_game_alert_bot
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
```

---

## 🏗 Архитектура и принцип работы

1. **Планировщик (`MTGameNotificationScheduler`):**
   - Запускается сразу при старте приложения (`@PostConstruct`), а затем каждые 5 минут по расписанию (`cron = "0 */5 * * * *"`).
   - Последовательно вызывает все зарегистрированные компоненты `NotificationService`.

2. **Модули оповещения (`NotificationService`):**
   - **`MTGameNotificationService` (Матчи лиги):**
     - Запрашивает через `GameService` (`MTGameApiService`) список предстоящих матчей из внешнего REST API для всех активных команд из `MT_GAME_TEAMS`.
     - При нахождении нового матча формирует сообщение с домашней и гостевой командами, датой и временем.
     - Создает опрос в Telegram через `PollService` и сохраняет запись в `EventStorage`.
   - **`TrainingsNotificationService` (Тренировки):**
     - Проверяет активные тренировки из конфигурации `TRAININGS`.
     - Вычисляет количество дней до ближайшей даты тренировки: если оно совпадает с `notifyDaysBefore`, генерирует детерминированный ID события на целевую дату.
     - Формирует текст опроса с местом и отформатированным интервалом времени (например, *«Тренировка: СК Торпедо. Дата: Понедельник, 15 мая (20:00 - 21:30)»*).
     - Создает опрос в Telegram и сохраняет запись в `EventStorage`.
   - **`ForceNotificationService` (Принудительный повторный опрос):**
     - Выбирает из БД все события с флагом `force_send = true` (`findAllByForceResend()`).
     - Повторно отправляет опрос в чат и сбрасывает флаг `force_send` в `false`.

3. **Хранилище событий (`EventStorage` / `DBEventStorage` / `FileEventStorage`):**
   - По умолчанию используется `DBEventStorage` на базе таблицы `events` в PostgreSQL (с полями `id`, `chat_id`, `poll_message`, `notify_date`, `force_send`).
   - Исключает повторную отправку опросов по уже обработанным матчам и тренировкам.
   - В качестве fallback предусмотрен `FileEventStorage`, сохраняющий ID событий в файл.

4. **Интеграция с Telegram (`TelegramApiService` / `PollService`):**
   - Отправляет открытый (неанонимный) опрос (`SendPoll` с `isAnonymous = false`) с вариантами ответов `Буду`, `Не буду`, `Думаю`.
   - Автоматически закрепляет созданный опрос в чате (`PinChatMessage`).

---

## 🚀 Сборка и запуск

### 1. Локальное окружение (Docker Compose)
Для быстрого поднятия тестового окружения (PostgreSQL + WireMock) выполните:

```bash
docker compose -f docker/docker-compose.yml up -d
```

Это запустит:
- **PostgreSQL 16** на порту `5432` (БД `mt_game_alert_bot`, пользователь `postgres`, пароль `postgres`)
- **WireMock 3.13.2** на порту `8089` с преднастроенными моками ответов API игр

### 2. Сборка проекта
Сборка исполняемого jar-пакета с помощью Maven:

```bash
mvn clean package
```

### 3. Запуск приложения
Запуск через скомпилированный Spring Boot Fat-JAR:

```bash
java -jar target/mt-game-alert-bot-1.0-SNAPSHOT.jar
```

Или прямой запуск через Maven-плагин:

```bash
mvn spring-boot:run
```

---

## 🧪 Тестирование

Проект покрыт интеграционными тестами с использованием **JUnit 5**, **Testcontainers (PostgreSQL)** и **WireMock**:

```bash
mvn clean test
```

В тестах автоматически поднимается изолированный контейнер базы данных PostgreSQL и стаб внешнего API игр, имитирующий реальный флоу получения матчей и отправки опросов в Telegram.

---

## 📝 Лицензия

Проект распространяется под свободной лицензией **[MIT](LICENSE)**.
