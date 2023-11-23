package ru.romanow.database.migration

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import ru.romanow.database.migration.config.DatabaseTestConfiguration

@SpringBootTest
@Import(DatabaseTestConfiguration::class)
class DatabaseMigrationApplicationTest {

    @Test
    fun test() {
    }
}
