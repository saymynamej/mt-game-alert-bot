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
- 📊 **Интерактивные открытые опросы в Telegram:** Автоматическая отправка опроса в чат команды (*«Будешь играть?»* с вариантами *«Буду» / «Не буду» / «Думаю»*). Опрос создается открытым (не анонимным), что позволяет тренерам и капитанам видеть поименный состав.
- 🛡️ **Защита от дубликатов сообщений:** Сервис фиксирует факт уведомления в базе данных PostgreSQL, гарантируя отсутствие повторного спама в командные чаты.
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

| Переменная          | Обязательная | Описание                                                                                                                                                                                                     | Пример значения                                                          |
|:--------------------|:------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------|
| `BOT_TOKEN`         |    **Да**    | Секретный API-токен Telegram-бота, полученный у [@BotFather](https://t.me/BotFather). Используется для авторизации запросов к Telegram Bot API.                                                              | `123456789:ABCdefGhIJKlmNoPQRsTUVwxyZ`                                   |
| `TELEGRAM_SCHEMA`   |    **Да**    | Протокол для обращения к API Telegram (`https` для продакшна, `http` для локального мок-сервера/WireMock).                                                                                                   | `https`                                                                  |
| `TELEGRAM_HOST`     |    **Да**    | Хост Telegram Bot API. В штатном режиме — официальный API Telegram, для тестов — локальный хост.                                                                                                             | `api.telegram.org`                                                       |
| `TELEGRAM_PORT`     |    **Да**    | Порт Telegram Bot API (`443` для HTTPS, либо кастомный порт прокси/мока).                                                                                                                                    | `443`                                                                    |
| `MT_GAME_URL`       |    **Да**    | Шаблон URL внешнего API лиги для получения списка игр. Содержит два спецификатора `%s`: первый подставляется как `team_id`, второй — начальная дата выборки (`date_from` в формате `yyyy-MM-dd'T'HH:mm:ss`). | `https://mtgame.ru/api/v1/league/2/games/?team_id=%s&date_from=%s`       |
| `MT_GAME_TEAMS`     |    **Да**    | JSON-строка со словарём отслеживаемых команд, их статусом активности, идентификатором команды в лиге и ID целевого Telegram-чата.                                                                            | `{"MyTeam":{"enabled":true,"teamId":"10000","chatId":"-1003796928479"}}` |
| `DATABASE_URL`      |    **Да**    | JDBC-строка подключения к базе данных PostgreSQL.                                                                                                                                                            | `jdbc:postgresql://localhost:5432/mt_game_alert_bot`                     |
| `DATABASE_USERNAME` |    **Да**    | Имя пользователя базы данных PostgreSQL.                                                                                                                                                                     | `postgres`                                                               |
| `DATABASE_PASSWORD` |    **Да**    | Пароль пользователя базы данных PostgreSQL.                                                                                                                                                                  | `postgres`                                                               |

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

### Пример файла `.env`

Создайте файл `.env` в корневой директории или экспортируйте переменные в окружение:

```env
# Telegram Bot Credentials
BOT_TOKEN=123456789:ABCdefGhIJKlmNoPQRsTUVwxyZ
TELEGRAM_SCHEMA=https
TELEGRAM_HOST=api.telegram.org
TELEGRAM_PORT=443

# League API Settings
MT_GAME_URL=https://mtgame.ru/api/v1/league/2/games/?team_id=%s&date_from=%s

# Teams Mapping (JSON)
MT_GAME_TEAMS={"AlphaTeam":{"enabled":true,"teamId":"10000","chatId":"-1003796928479"}}

# PostgreSQL Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/mt_game_alert_bot
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
```

---

## 🏗 Архитектура и принцип работы

1. **Планировщик (`MTGameNotificationScheduler`):**
   - Запускает процесс проверки игр сразу при старте приложения (`@PostConstruct`), а затем каждые 5 минут по расписанию (`cron = "0 */5 * * * *"`).
2. **Получение данных (`MTGameApiService`):**
   - Делает HTTP-запрос к API лиги для каждой активной команды из `MT_GAME_TEAMS` за период начиная с текущей даты.
3. **Фильтрация и проверка дубликатов (`DBGameStorage` / `GameRepository`):**
   - Проверяет по таблице `notify` в PostgreSQL, отправлялось ли ранее уведомление по идентификатору игры (`id`).
4. **Уведомление и создание опроса (`TelegramApiService`):**
   - Если игра новая, бот отправляет в соответствующий `chatId` открытый опрос (`SendPoll` с `isAnonymous = false`).
   - Тема опроса формируется динамически: дата, время, название домашней и гостевой команд.
   - Варианты ответов: `Буду`, `Не буду`, `Думаю`.
5. **Сохранение состояния:**
   - После успешной отправки опроса ID игры и метаданные записываются в таблицу `notify` базы данных.

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
