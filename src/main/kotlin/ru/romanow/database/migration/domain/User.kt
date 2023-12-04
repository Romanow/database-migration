package ru.romanow.database.migration.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "name", length = 10, nullable = false)
    var name: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    var status: Status? = null,

    @ManyToOne
    @JoinColumn(name = "address_id")
    var address: Address? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (status?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(id=$id, name=$name, status=$status)"
    }
}
