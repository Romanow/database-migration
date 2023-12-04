package ru.romanow.database.migration.service

import ru.romanow.database.migration.models.UserResponse

interface UserService {
    fun findAll(): List<UserResponse>
}
