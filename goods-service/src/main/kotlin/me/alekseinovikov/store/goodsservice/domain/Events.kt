package me.alekseinovikov.store.goodsservice.domain

sealed class Event {

    data class GoodCreated(val id: Identifier, val name: String, val description: String) : Event()

    data class GoodNameChanged(val id: Identifier, val name: String) : Event()

    data class GoodDescriptionChanged(val id: Identifier, val description: String) : Event()

    data class GoodDeleted(val id: Identifier) : Event()

}