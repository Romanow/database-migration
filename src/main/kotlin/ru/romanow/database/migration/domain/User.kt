package ru.romanow.database.migration.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "name", length = 10, nullable = false)
    var name: String? = null,

    @Column(name = "status", length = 10)
    var status: String? = null,

    @Column(name = "location", length = 10)
    var location: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (status != other.status) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(id=$id, name=$name, status=$status, location=$location)"
    }
}
