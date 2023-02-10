package me.alekseinovikov.store.goodsservice.domain

import kotlin.jvm.Throws

data class Good(
    private val id: Identifier,
    private var name: String,
    private var description: String,
    private var deleted: Boolean = false,
    private val version: Version = Version()
) {

    private val eventsToEmmit = mutableListOf<Event>()

    @Throws(OptimisticLockException::class, GoodAlreadyDeletedException::class)
    fun apply(command: Command): List<Event> {
        checkVersionAndDeleted(command)

        when (command) {
            is Command.CreateGood -> apply(command)
            is Command.DeleteGood -> apply(command)
            is Command.ChangeGoodUpdate -> apply(command)
        }

        eventsToEmmit.forEach { apply(it) }
        return eventsToEmmit
    }

    fun apply(event: Event) {
        when (event) {
            is Event.GoodCreated -> {
                name = event.name
                description = event.description
            }
            is Event.GoodNameChanged -> name = event.name
            is Event.GoodDescriptionChanged -> description = event.description
            is Event.GoodDeleted -> deleted = true
        }
    }

    private fun checkVersionAndDeleted(command: Command) {
        if (command.optimisticVersion != version) {
            throw OptimisticLockException(id, version)
        }

        if (deleted) {
            throw GoodAlreadyDeletedException(id)
        }
    }

    private fun apply(command: Command.CreateGood) {
        eventsToEmmit.add(Event.GoodCreated(id, command.name, command.description))
    }

    private fun apply(command: Command.DeleteGood) {
        eventsToEmmit.add(Event.GoodDeleted(id))
    }

    private fun apply(command: Command.ChangeGoodUpdate) {
        eventsToEmmit.add(Event.GoodNameChanged(id, command.name))
        eventsToEmmit.add(Event.GoodDescriptionChanged(id, command.description))
    }

}