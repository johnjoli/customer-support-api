# Customer Support API

Учебный backend-проект на Java и Spring Boot для отработки реальных практик разработки: REST API, многослойная архитектура, безопасность (Spring Security + JWT), асинхронное взаимодействие через брокер сообщений (RabbitMQ) и контейнеризация.

## Что умеет приложение

- **Аутентификация и авторизация**: Безопасная регистрация пользователей и вход с получением JWT-токена.
- **Разграничение доступа**: Защита эндпоинтов с помощью ролевой модели (ROLE_CUSTOMER, ROLE_USER).
- **Управление клиентами и тикетами**: Создание обращений, отслеживание статусов, приоритетов и ведение комментариев.
- **Асинхронная обработка (RabbitMQ)**: Отправка событий в очереди брокера для последующей асинхронной обработки.
- **SLA и бизнес-логика**: Автоматический расчет дедлайнов (SLA deadline) и фоновая проверка просроченных обращений.
- **Фильтрация и пагинация**: Гибкий поиск тикетов через query-параметры.

## Стек технологий

- Java 21
- Spring Boot 4.0.6
- Spring Security & JJWT (Java JWT)
- Spring AMQP / RabbitMQ
- Spring Data JPA & PostgreSQL
- Flyway (Миграции схемы БД)
- Docker & Docker Compose
- Lombok & MapStruct
- OpenAPI / Swagger UI
- JUnit 5, Mockito, MockMvc

## Запуск проекта через Docker Compose

Вся инфраструктура (БД, брокер очередей и само приложение) полностью изолирована и запускается в контейнерах.

### Шаг 1. Перейдите в корневую папку проекта

cd customer-support-api

Шаг 2. Соберите проект

./mvnw clean package -DskipTests

Шаг 3. Поднимите весь стек контейнеров

docker compose up --build
Приложение будет доступно по адресу: http://localhost:8080

Полезные ссылки:
Swagger UI (Документация API): http://localhost:8080/swagger-ui/index.html

RabbitMQ Management Console: http://localhost:15672 (вход в веб-панель брокера)

Для остановки всех контейнеров:

docker compose down

Основные endpoint'ы API
1. Открытые эндпоинты (Аутентификация)
HTTP
POST /api/v1/auth/register  - Регистрация нового пользователя
POST /api/v1/auth/login     - Вход (возвращает JWT-токен)
2. Защищенные эндпоинты
Требуют передачи токена в заголовке: Authorization: Bearer
=====
Customers
HTTP
GET    /api/v1/customers
GET    /api/v1/customers/{id}
POST   /api/v1/customers
PUT    /api/v1/customers/{id}
DELETE /api/v1/customers/{id}
=====
Tickets
HTTP
GET   /api/v1/tickets
POST  /api/v1/customers/{customerId}/tickets
GET   /api/v1/customers/{customerId}/tickets
GET   /api/v1/customers/{customerId}/tickets/{ticketId}
PATCH /api/v1/customers/{customerId}/tickets/{ticketId}/status
=====
Comments
HTTP
POST /api/v1/customers/{customerId}/tickets/{ticketId}/comments
GET  /api/v1/customers/{customerId}/tickets/{ticketId}/comments
=====
Миграции базы данных (Flyway)
Все изменения структуры таблиц контролируются через sql-скрипты миграций в src/main/resources/db/migration. Flyway автоматически применяет их при старте контейнера. Hibernate работает в режиме validate.