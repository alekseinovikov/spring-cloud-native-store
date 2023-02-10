package me.alekseinovikov.store.goodsservice.repository

import me.alekseinovikov.store.goodsservice.domain.*
import org.springframework.stereotype.Component

@Component
class InMemoryGoodsRepository : GoodsRepository {

    private val events = mutableMapOf<Identifier, MutableList<Pair<Version, GoodsEvent>>>()

    override suspend fun loadGood(id: Identifier): Result<Good> {
        val foundEvents = events[id] ?: return Result.failure(GoodNotFoundException(id))
        return Good(id).let { good ->
            foundEvents.sortedBy { it.first.version }.forEach { (_, event) ->
                good.apply(event)
            }

            Result.success(good)
        }
    }

    override suspend fun saveGood(good: Good) {
        good.eventsToEmmit.forEach { event ->
            events.computeIfAbsent(event.id) { mutableListOf() }
                .also { it.add(Pair(event.version, event)) }
        }
    }

}