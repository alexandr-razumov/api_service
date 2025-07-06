# API Service - Spring Security Configuration

## Обзор безопасности

API сервис использует Spring Security с HTTP Basic Authentication для защиты эндпоинтов.

## Доступные роли

### USER
- Логин: `user`
- Пароль: `user`
- Доступ: GET `/status`

### ADMIN
- Логин: `admin`
- Пароль: `admin`
- Доступ: GET `/status`, GET `/data`

## Эндпоинты

| Метод | Путь | Роли | Описание |
|-------|------|------|----------|
| GET | `/status` | USER, ADMIN | Проверка статуса сервиса |
| GET | `/data` | ADMIN | Получение последних 10 записей |

## Использование Swagger UI

1. Откройте браузер и перейдите по адресу: `http://localhost:8080/swagger-ui.html`
2. Нажмите кнопку "Authorize" в правом верхнем углу
3. Введите учетные данные:
   - Для роли USER: `user` / `password`
   - Для роли ADMIN: `admin` / `admin`
4. Теперь вы можете тестировать API эндпоинты

## Тестирование с помощью curl

### Проверка статуса (USER/ADMIN)
```bash
curl -u user:password http://localhost:8080/status
curl -u admin:admin http://localhost:8080/status
```

### Получение данных (только ADMIN)
```bash
curl -u admin:admin http://localhost:8080/data
```

### Попытка доступа без авторизации
```bash
curl http://localhost:8080/status
# Вернет 401 Unauthorized
```

### Попытка доступа USER к данным ADMIN
```bash
curl -u user:password http://localhost:8080/data
# Вернет 403 Forbidden
```

## Конфигурация безопасности

Основная конфигурация находится в `SecurityConfig.java`:
- HTTP Basic Authentication
- Роли USER и ADMIN
- Защищенные эндпоинты с проверкой ролей
- Swagger UI доступен без авторизации

## Важные замечания

⚠️ **ВНИМАНИЕ**: Пароли в текущей конфигурации хранятся в открытом виде и предназначены только для разработки. В продакшене обязательно используйте BCrypt или другой безопасный способ хранения паролей. 