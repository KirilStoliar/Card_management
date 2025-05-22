# Bank Card Management System

Система управления банковскими картами с аутентификацией пользователей
и транзакциями между картами.

## Основные возможности

- Регистрация и аутентификация пользователей (JWT)
- Управление банковскими картами (создание, блокировка, активация)
- Переводы между картами
- Разделение прав доступа (ADMIN/USER)
- Шифрование номеров карт в базе данных
- Документированное API (Swagger UI)

## Технологии

- Java 17
- Spring Boot 3
- PostgreSQL
- Liquibase (миграции БД)
- JWT аутентификация
- Docker

## Запуск через Docker Compose (рекомендуемый способ)

1. **Подготовка окружения**:

   Установите [Docker](https://docs.docker.com/get-docker/) и [Docker Compose](https://docs.docker.com/compose/install/)

2. **Сборка и запуск**:

   ```bash
   ***Собрать и запустить контейнеры***
   docker-compose up --build
   
   ***Остановить контейнеры***
   docker-compose down
   ```

3. **Доступ к приложению**:

**API**: http://localhost:8080  
**Swagger UI**: http://localhost:8080/swagger-ui.html  
**Тестовые пользователи**:

- Администратор:

    - Логин: admin@example.com
    - Пароль: admin123

- Обычный пользователь:

    - Логин: user@example.com
    - Пароль: user123

## Запуск без Docker (для разработки)

- **Требования**:

Java 17+  
PostgreSQL 15+  
Maven 3.8+

- **Настройка базы данных**:
```sql
CREATE DATABASE card_db;
CREATE USER admin WITH PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE card_db TO admin;
```

- **Настройка приложения**:

Проверьте файл application-local.yml в src/main/resources:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/card_db
    username: admin
    password: admin

card:
  encryption:
    key: mySuperSecretKey123

jwt:
  secret: myJwtSecretKey1234567890
  ```

- **Сборка и запуск**:

```bash
# Сборка проекта
mvn clean install

# Запуск приложения
java -jar target/card-management-system-*.jar
```

## Использование API

После запуска откройте Swagger UI для доступа к документации API:
http://localhost:8080/swagger-ui.html

Примеры запросов:

- **Аутентификация**:

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "user123"
}
```

- **Создание карты (требуются права ADMIN)**:

```http
POST /api/cards
Authorization: Bearer <your_token>
Content-Type: application/json

{
  "cardNumber": "4111111111111111",
  "cardHolderName": "Test User",
  "expirationDate": "2025-12-31",
  "status": "ACTIVE",
  "balance": 1000,
  "userId": 1
}
```

## Структура проекта

```
src/
├── main/
│   ├── java/
│   │   └── com/stoliar/cardManagement/
│   │       ├── config/       # Конфигурации Spring
│   │       ├── controller/   # REST контроллеры
│   │       ├── dto/          # Data Transfer Objects
│   │       ├── exception/    # Кастомные исключения
│   │       ├── model/        # Сущности БД
│   │       ├── repository/   # Репозитории JPA
│   │       ├── service/      # Бизнес-логика
│   │       ├── util/        # Вспомогательные классы
│   │       └── CardManagementApplication.java
│   └── resources/
│       ├── db/              # Миграции Liquibase
│       └── application.yml  # Основной конфиг
├── docker-compose.yml
├── Dockerfile
├── pom.xml
```
