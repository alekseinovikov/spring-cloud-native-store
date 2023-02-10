package me.alekseinovikov.store.goodsservice.domain

data class OptimisticLockException(val id: Identifier, val version: Version)
    : RuntimeException("Optimistic lock failed for id: $id, version: $version")

data class GoodAlreadyDeletedException(val id: Identifier)
    : RuntimeException("Good already deleted for id: $id")