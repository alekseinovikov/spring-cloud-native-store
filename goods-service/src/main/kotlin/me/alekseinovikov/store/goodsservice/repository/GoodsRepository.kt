package me.alekseinovikov.store.goodsservice.repository

import me.alekseinovikov.store.goodsservice.domain.Good
import me.alekseinovikov.store.goodsservice.domain.Identifier

interface GoodsRepository {

    suspend fun loadGood(id: Identifier): Result<Good>

    suspend fun saveGood(good: Good)

}