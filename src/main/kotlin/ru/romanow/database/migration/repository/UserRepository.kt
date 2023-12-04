package ru.romanow.database.migration.repository

import org.springframework.data.repository.CrudRepository
import ru.romanow.database.migration.domain.User

interface UserRepository : CrudRepository<User, Int>
