package me.alekseinovikov.store.goodsservice.domain

import com.github.f4b6a3.uuid.UuidCreator
import java.util.UUID

data class Identifier(private val id: UUID) {
    constructor(): this(UuidCreator.getTimeOrderedEpoch())
    constructor(uuid: String): this(UUID.fromString(uuid))

    override fun toString(): String {
        return id.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Identifier

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}