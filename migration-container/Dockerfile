FROM liquibase/liquibase:4.20

COPY src/main/resources/db/liquibase /liquibase/changelog
COPY migration-container/drivers/postgresql-driver.jar /liquibase/lib
COPY migration-container/config/liquibase.docker.properties /liquibase
