package me.alekseinovikov.store.goodsservice.service

import kotlinx.coroutines.runBlocking
import me.alekseinovikov.store.goodsservice.IntegrationTest
import me.alekseinovikov.store.goodsservice.domain.GoodAlreadyDeletedException
import me.alekseinovikov.store.goodsservice.domain.OptimisticLockException
import me.alekseinovikov.store.goodsservice.domain.Version
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class GoodsServiceTest {

    @Autowired
    private lateinit var goodsService: GoodsService

    @Test
    fun createGood(): Unit = runBlocking {
        // given
        // when
        val result = goodsService.create("name", "description")

        // then
        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrThrow().name).isEqualTo("name")
        assertThat(result.getOrThrow().description).isEqualTo("description")
        assertThat(result.getOrThrow().deleted).isFalse()
        assertThat(result.getOrThrow().version).isEqualTo(Version(1UL))
    }

    @Test
    fun updateGood(): Unit = runBlocking {
        // given
        val result = goodsService.create("name", "description")
        val good = result.getOrThrow()
        val version = good.version

        // when
        val updateResult = goodsService.update(good.id, version, "new name", "new description")

        // then
        assertThat(updateResult.isSuccess).isTrue
        assertThat(updateResult.getOrThrow().name).isEqualTo("new name")
        assertThat(updateResult.getOrThrow().description).isEqualTo("new description")
        assertThat(updateResult.getOrThrow().deleted).isFalse
        assertThat(updateResult.getOrThrow().version).isEqualTo(Version(3UL))
    }

    @Test
    fun deleteGood(): Unit = runBlocking {
        // given
        val result = goodsService.create("name", "description")
        val good = result.getOrThrow()
        val version = good.version

        // when
        val deleteResult = goodsService.delete(good.id, version)

        // then
        assertThat(deleteResult.isSuccess).isTrue
    }

    @Test
    fun getGood(): Unit = runBlocking {
        // given
        val result = goodsService.create("name", "description")
        val good = result.getOrThrow()

        // when
        val getResult = goodsService.get(good.id)

        // then
        assertThat(getResult.isSuccess).isTrue
        assertThat(getResult.getOrThrow().name).isEqualTo("name")
        assertThat(getResult.getOrThrow().description).isEqualTo("description")
        assertThat(getResult.getOrThrow().deleted).isFalse
        assertThat(getResult.getOrThrow().version).isEqualTo(Version(1UL))
    }

    @Test
    fun deleteAlreadyDeletedGood(): Unit = runBlocking {
        // given
        val result = goodsService.create("name", "description")
        val good = result.getOrThrow()
        val version = good.version
        goodsService.delete(good.id, version)

        // when
        val deleteResult = goodsService.delete(good.id, version.next())

        // then
        assertThat(deleteResult.isFailure).isTrue
        assertThat(deleteResult.exceptionOrNull()).isInstanceOf(GoodAlreadyDeletedException::class.java)
    }

    @Test
    fun updateWrongVersionOptimisticLockException(): Unit = runBlocking {
        // given
        val result = goodsService.create("name", "description")
        val good = result.getOrThrow()
        val version = good.version

        // when
        val updateResult = goodsService.update(good.id, version.next(), "new name", "new description")

        // then
        assertThat(updateResult.isFailure).isTrue
        assertThat(updateResult.exceptionOrNull()).isInstanceOf(OptimisticLockException::class.java)
    }

}