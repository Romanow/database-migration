package ru.romanow.database.migration.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "address")
data class Address(
    @Id
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "city", nullable = false)
    var city: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Address

        if (id != other.id) return false
        if (city != other.city) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (city?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Address(id=$id, city=$city)"
    }
}
