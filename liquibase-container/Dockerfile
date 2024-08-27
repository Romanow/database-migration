FROM liquibase/liquibase:4.20

COPY src/main/resources/db/liquibase /BOOT-INF/classes/db/liquibase
COPY liquibase-container/drivers/postgresql-driver.jar /liquibase/lib
COPY liquibase-container/config/liquibase.docker.properties /liquibase
