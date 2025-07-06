# API Service - Сервис мониторинга API

## 📋 Описание

API Service - это Spring Boot приложение для мониторинга внешних API, сбора данных и их обработки. Сервис предоставляет REST API для получения статуса и данных, поддерживает аутентификацию, логирование и интеграцию с Kafka для обработки событий.

## 🏗️ Архитектура

### Технологический стек

- **Java 17** - основной язык программирования
- **Spring Boot 3.2.5** - фреймворк для создания приложений
- **Spring Security** - безопасность и аутентификация
- **Spring Data JPA** - работа с базой данных
- **PostgreSQL** - основная база данных
- **Apache Kafka** - обработка событий и сообщений
- **Docker & Docker Compose** - контейнеризация
- **Swagger/OpenAPI** - документация API
- **Lombok** - уменьшение boilerplate кода
- **MapStruct** - маппинг объектов
- **Maven** - управление зависимостями

### Структура проекта

```
src/main/java/com/example/api_service/
├── ApiServiceApplication.java          # Главный класс приложения
├── config/                             # Конфигурации
│   ├── KafkaConfig.java               # Конфигурация Kafka
│   ├── OpenApiConfig.java             # Конфигурация Swagger
│   ├── RetryConfig.java               # Конфигурация retry
│   └── SecurityConfig.java            # Конфигурация безопасности
├── controller/                         # REST контроллеры
│   └── ApiDataController.java         # Основной контроллер API
├── dto/                               # Data Transfer Objects
│   ├── ApiDataRequest.java            # DTO для запросов
│   ├── ApiDataResponse.java           # DTO для ответов
│   └── ErrorResponse.java             # DTO для ошибок
├── exception_handler/                  # Обработка исключений
│   ├── ApiException.java              # Базовое исключение API
│   ├── BadRequestException.java       # Исключение для 400 ошибок
│   ├── GlobalExceptionHandler.java    # Глобальный обработчик
│   └── ResourceNotFoundException.java # Исключение для 404 ошибок
├── mapper/                            # Маппинг объектов
│   └── ApiDataMapper.java             # Маппер для ApiData
├── model/                             # Модели данных
│   └── ApiDataEntity.java             # Сущность для БД
├── repository/                        # Репозитории
│   └── ApiDataRepository.java         # Репозиторий для ApiData
└── service/                           # Бизнес-логика
    └── ApiDataService.java            # Основной сервис
```

## 🚀 Быстрый старт

### Предварительные требования

- Java 17 или выше
- Maven 3.6+
- Docker и Docker Compose
- PostgreSQL (для локальной разработки)
- Apache Kafka (для локальной разработки)

### Запуск с Docker Compose

1. **Клонируйте репозиторий:**
   ```bash
   git clone <repository-url>
   cd api_service
   ```

2. **Запустите все сервисы:**
   ```bash
   docker-compose up -d
   ```

3. **Проверьте статус сервисов:**
   ```bash
   docker-compose ps
   ```

4. **Доступные сервисы:**
   - API Service: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Kafka UI: http://localhost:8081

## 🔐 Безопасность

### Аутентификация

Сервис использует HTTP Basic Authentication:

- **Роль USER**: доступ к `/status`
- **Роль ADMIN**: доступ ко всем эндпоинтам

### Настройка пользователей

По умолчанию создаются пользователи:
- `user/user` (роль USER)
- `admin/admin` (роль ADMIN)

## 📊 Kafka интеграция

### Топики

- `api-data-topic` - успешные API вызовы
- `api-errors-topic` - ошибки API вызовов

## 🧪 Тестирование

### Запуск тестов

```bash
# Все тесты
mvn test

# Конкретный тест
mvn test -Dtest=ApiDataServiceTest
```

### Покрытие тестами

- **ApiDataService** - тестирование бизнес-логики
- **ApiDataMapper** - тестирование маппинга

## 📚 Документация

### Swagger UI

Доступна по адресу: http://localhost:8080/swagger-ui.html

### Мониторинг

- **Health Check**: `/status`
- **Логи**: `logs/api-service.log`

