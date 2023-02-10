package me.alekseinovikov.store.goodsservice.service

import me.alekseinovikov.store.goodsservice.domain.Good
import me.alekseinovikov.store.goodsservice.domain.GoodsCommand
import me.alekseinovikov.store.goodsservice.domain.Identifier
import me.alekseinovikov.store.goodsservice.domain.Version
import me.alekseinovikov.store.goodsservice.repository.GoodsRepository
import org.springframework.stereotype.Service

@Service
class GoodsServiceImpl(private val goodsRepository: GoodsRepository) : GoodsService {
    override suspend fun create(name: String, description: String): Result<Good> =
        Result.success(Good())
            .run { applyAndSave(this, GoodsCommand.Create(name, description)) }

    override suspend fun update(
        id: Identifier,
        version: Version,
        name: String,
        description: String
    ): Result<Good> =
        goodsRepository.loadGood(id)
            .run { applyAndSave(this, GoodsCommand.Update(name, description, version)) }

    override suspend fun delete(id: Identifier, version: Version): Result<Unit> =
        goodsRepository.loadGood(id)
            .run { applyAndSave(this, GoodsCommand.Delete(version)).map { } }

    override suspend fun get(id: Identifier): Result<Good> = goodsRepository.loadGood(id)

    private suspend fun applyAndSave(goodResult: Result<Good>, command: GoodsCommand): Result<Good> {
        if (goodResult.isFailure) return goodResult
        val good = goodResult.getOrThrow()

        val applyResult = good.apply(command)
        return when {
            applyResult.isFailure -> applyResult
            else -> {
                goodsRepository.saveGood(good)
                Result.success(good)
            }
        }
    }

}