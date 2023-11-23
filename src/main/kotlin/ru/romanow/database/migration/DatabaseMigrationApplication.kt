package ru.romanow.database.migration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DatabaseMigrationApplication

fun main(args: Array<String>) {
    runApplication<DatabaseMigrationApplication>(*args)
}
