package me.alekseinovikov.store.goodsservice.service

import me.alekseinovikov.store.goodsservice.domain.Good
import me.alekseinovikov.store.goodsservice.domain.Identifier
import me.alekseinovikov.store.goodsservice.domain.Version

interface GoodsService {
    suspend fun create(name: String, description: String): Result<Good>
    suspend fun update(id: Identifier, version: Version, name: String, description: String): Result<Good>
    suspend fun delete(id: Identifier, version: Version): Result<Unit>

    suspend fun get(id: Identifier): Result<Good>

}