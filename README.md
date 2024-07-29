# Database migration

[![Build project](https://github.com/Romanow/database-migration/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Romanow/database-migration/actions/workflows/build.yml)
[![pre-commit](https://img.shields.io/badge/pre--commit-enabled-brightgreen?logo=pre-commit)](https://github.com/pre-commit/pre-commit)

## Реализация

1. Собрать образ liquibase и запускать как `initContainer`.
2. При откате использовать последнюю версию и помимо `helm rollback <release>`, запускать job для отката до
   тега `liquibase rollback --tag=$ROLLBACK_TAG`.

```shell
$ docker run --rm \
    --network database-migration \
    romanowalex/liquibase-container:latest \
    rollback --tag=v3.0 \
    --url=jdbc:postgresql://postgres:5432/migration \
    --username=program \
    --password=test
```

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

## Тестирование в кластере k8s

```shell
# create local cluster
$ kind create cluster --config kind.yml
$ kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

# install required repos
$ helm repo add romanow https://romanow.github.io/helm-charts/
$ helm repo update

# install postgres
$ helm install postgres romanow/postgres --values postgres/values.yaml

# install service
$ helm upgrade \
    migration-application \
    romanow/java-service \
    --values=k8s/migration-application/values.yaml \
    --set image.tag="$IMAGE_VERSION" \
    --set ingress.name=k8s \
    --set ingress.domain=romanow-alex.ru \
    --description "$IMAGE_VERSION" \
    --install \
    --wait

# rollback migration
$ helm install \
    --generate-name \
    romanow/common-job \
    --values=k8s/rollback-job/values.yaml \
    --set rollbackTag="$VERSION" \
    --wait

# rollback revision
$ helm rollback migration-application "$REVISION" --wait
```

## Public report

[Деплой и откат приложения и миграций БД](report/README.md)

## TODO

1. Больше фокус на + и - миграций.
2. Мы можем себе это позволить, потому что данных не много (> 10Gb), если данных много, то тут другие подходы.
3. Деплоймент с даунтаймом, мы можем себе позволить релизное окно.
4. Из-за того, что мы не удаляем данные (и не восстанавливаем из архива), у нас данные растут и мы можем проверять
   performance.
5. Генерация схемы через ORM – выстрел в ногу: долго, дорого, неконтролируемо.
