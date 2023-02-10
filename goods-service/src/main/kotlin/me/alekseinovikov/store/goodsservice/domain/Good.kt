package me.alekseinovikov.store.goodsservice.domain

data class Good(
    val id: Identifier,
    var name: String,
    var description: String,
    var deleted: Boolean = false,
    val version: Version = Version()
) {
    constructor() : this(Identifier(), "", "")
    constructor(id: Identifier) : this(id, "", "")

    val eventsToEmmit = mutableListOf<GoodsEvent>()

    @Throws(OptimisticLockException::class, GoodAlreadyDeletedException::class)
    fun apply(goodsCommand: GoodsCommand): Result<Good> {
        val validationResult = checkVersionAndDeleted(goodsCommand)
        if (validationResult.isFailure) {
            return Result.failure(validationResult.exceptionOrNull()!!)
        }

        when (goodsCommand) {
            is GoodsCommand.Create -> apply(goodsCommand)
            is GoodsCommand.Delete -> applyDelete()
            is GoodsCommand.Update -> apply(goodsCommand)
        }

        eventsToEmmit.forEach { apply(it) }
        return Result.success(this)
    }

    fun apply(goodsEvent: GoodsEvent): Result<Unit> {
        when (goodsEvent) {
            is GoodsEvent.Created -> {
                name = goodsEvent.name
                description = goodsEvent.description
            }

            is GoodsEvent.NameChanged -> name = goodsEvent.name
            is GoodsEvent.DescriptionChanged -> description = goodsEvent.description
            is GoodsEvent.Deleted -> deleted = true
        }

        this.version.update(goodsEvent.version)
        return Result.success(Unit)
    }

    private fun checkVersionAndDeleted(goodsCommand: GoodsCommand): Result<Unit> {
        if (goodsCommand.optimisticVersion != version) {
            return Result.failure(OptimisticLockException(id, version, goodsCommand.optimisticVersion))
        }

        if (deleted) {
            return Result.failure(GoodAlreadyDeletedException(id))
        }

        return Result.success(Unit)
    }

    private fun apply(goodsCommand: GoodsCommand.Create) {
        eventsToEmmit.add(GoodsEvent.Created(id, version.next(), goodsCommand.name, goodsCommand.description))
    }

    private fun applyDelete() {
        eventsToEmmit.add(GoodsEvent.Deleted(id, version.next()))
    }

    private fun apply(goodsCommand: GoodsCommand.Update) {
        eventsToEmmit.add(GoodsEvent.NameChanged(id, version.next(), goodsCommand.name))
        eventsToEmmit.add(GoodsEvent.DescriptionChanged(id, version.next(), goodsCommand.description))
    }

}