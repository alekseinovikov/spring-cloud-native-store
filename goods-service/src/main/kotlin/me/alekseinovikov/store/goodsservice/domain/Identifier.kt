package me.alekseinovikov.store.goodsservice.domain

import com.github.f4b6a3.uuid.UuidCreator
import java.util.UUID

data class Identifier(val id: UUID) {
    constructor(): this(UuidCreator.getTimeOrderedEpoch())
    constructor(uuid: String): this(UUID.fromString(uuid))

    override fun toString(): String {
        return id.toString()
    }
}