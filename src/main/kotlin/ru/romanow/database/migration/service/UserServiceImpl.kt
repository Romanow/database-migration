package ru.romanow.database.migration.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.romanow.database.migration.models.UserResponse
import ru.romanow.database.migration.repository.UserRepository

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    @Transactional(readOnly = true)
    override fun findAll(): List<UserResponse> =
        userRepository
            .findAll()
            .map { UserResponse(id = it.id, name = it.name, status = it.status?.name, address = it.address?.city) }
            .toList()
}
