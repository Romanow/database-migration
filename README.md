# Database migration

[![Build project](https://github.com/Romanow/database-migration/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Romanow/database-migration/actions/workflows/build.yml)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit)](https://github.com/pre-commit/pre-commit)

## Реализация

1. Собрать образ liquibase и запускать как `initContainer`.
2. При откате использовать последнюю версию и помимо `helm rollback <release>`, запускать job для отката до
   тега `liquibase rollback --tag=$ROLLBACK_TAG`.

### Прямые и обратные операции

|                         Операция                          |                                 Revert                                 |
|:---------------------------------------------------------:|:----------------------------------------------------------------------:|
| `CREATE TABLE (VIEW, INDEX, FUNCTION, TRIGGER, SEQUENCE)` |                                 `DROP`                                 |
|                       `ADD COLUMN `                       |                             `DROP COLUMN`                              |
|                      `ALTER COLUMN `                      |                             `ALTER COLUMN`                             |
|                   `ADD CONSTRAINT ...`                    |                           `DROP CONSTRAINT`                            |
|                 `ALTER ... RENAME TO ...`                 |                       `ALTER ... RENAME TO ...`                        |
|     `DROP TABLE (VIEW, FUNCTION, TRIGGER, SEQUENCE)`      |                                `CREATE`                                |
|                 `DROP COLUMN (NOT NULL)`                  |      `ADD COLUMN, UPDATE ... SET ..., ALTER COLUMN SET NOT NULL`       |
|                     `DROP CONSTRAINT`                     | `DELETE ...` (records, which not meet constraint), `CREATE CONSTRAINT` |
|                         `INSERT`                          |       `DELETE` (can leave unmodified, if doesn't affect process)       |
|                         `UPDATE`                          |       `UPDATE` (can leave unmodified, if doesn't affect process)       |
|                   `DELETE`, `TRUNCATE`                    |                         `INSERT` (if possible)                         |

### Тестовые данные

#### Запрос на получение версии сервиса

**Request**

```http request
GET http://localhost:8080/api/v1/version
```

**Response**

```json
{
  "version": "${git.build.version}",
  "commit": "${git.commit.id.abbrev}",
  "time": "${git.commit.time}"
}
```

#### Запрос на получение данных из БД

**Request**

```http request
GET http://localhost:8080/api/v1/users
```

**Response**

```json
[
  {
    "name": "${user.name}",
    "status": "${user.status}",
    "location": "${user.location}"
  }
]
```

#### Миграции

1. [Миграция v1.0: Создание таблицы users](src/main/resources/db/liquibase/changelog/v1.0_CreateUserTable.xml)
2. [Миграция v2.0: Изменение поля id на тип IDENTITY](src/main/resources/db/liquibase/changelog/v2.0_ChangeIdToIdentity.xml)
3. [Миграция v3.0: Создание таблицы address и перенесение данных из поля location](src/main/resources/db/liquibase/changelog/v3.0_CreateAddressTable.xml)
4. [Миграция v4.0: Изменение типа колонки status на enum](src/main/resources/db/liquibase/changelog/v4.0_ChangeStatusToEnum.xml)
5. [Миграция v5.0: Партиционирование таблицы users](src/main/resources/db/liquibase/changelog/v5.0_MigrateUsersToPartitions.xml)
6. [Миграция v6.0: Увеличение поля login до 80 символов и добавление новых пользователей](src/main/resources/db/liquibase/changelog/v6.0_EnlargeLoginFieldSize.xml)
