# java-explore-with-me
Сервис для организации мероприятий позволяет пользователям создавать события (от выставок до киносеансов), собирать участников, управлять публикацией и модерацией контента. Реализован на микросервисной архитектуре: основной сервис разделён на публичный, закрытый и административный API, а отдельный сервис статистики фиксирует запросы к эндпоинтам и формирует аналитику. Дополнительно добавлены комментарии к событиям с функционалом модерации.

### Приложение включает в себя сервисы:
- Основной сервис — содержит всё необходимое для работы продукта.
    - Просмотр событий без авторизации;
    - Возможность создания и управления категориями;
    - События и работа с ними - создание, модерация;
    - Запросы пользователей на участие в событии - запрос, подтверждение, отклонение;
    - Создание и управление подборками;
- Сервис статистики — хранит количество просмотров и позволяет делать различные выборки для анализа работы приложения.

  ## Описание сервисов
### Основной сервис (порту 8080)
API основного сервиса разделен на три части. Первая — публичная, доступна без регистрации любому пользователю сети. Вторая — закрытая, доступна только авторизованным пользователям. Третья — административная, для администраторов сервиса.

- **Публичный** (доступен для всех пользователей)
    - API для работы с событиями
    - API для работы с категориями
    - API для работы с подборками событий
- **Приватный** (доступен только для зарегистрированных пользователей)
    - API для работы с событиями
    - API для работы с запросами текущего пользователя на участие в событиях
- **Административный** (доступен только для администратора проекта)
    - API для работы с событиями
    - API для работы с категориями
    - API для работы с пользователями
    - API для работы с подборками событий
### Сервис статистики (порту 9090):
Собирает информацию. Во-первых, о количестве обращений пользователей к спискам событий и, во-вторых, о количестве запросов к подробной информации о событии. На основе этой информации формируется статистика о работе приложения.
- **Административный** (доступен только для администратора проекта)
    - API для работы со статистикой посещений
 
## Спецификация REST API swagger
- [Основной сервис](ewm-main-service-spec.json)
- [Сервис статистики](ewm-stats-service-spec.json)
- [Фича Комментарии](ewm-feature-comments-spec.json)

## Инструкция по развертыванию проекта:
1. Скопировать репозиторий
2. Собрать проект: `mvn clean  package`
3. Запуск контейнеров: `docker-compose build`, 
                       `docker-compose up -d`

## Стэк

| Java 21 | JavaScript | Maven | Spring Boot | PostgreSQL | H2 | JPA | Hibernate | Swagger | Junit5 | Mockito | Postman | Docker |


[Pull Request #3 - Feature Comments](https://github.com/Paul-Value/java-explore-with-me/pull/3)
