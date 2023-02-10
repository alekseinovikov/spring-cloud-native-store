package me.alekseinovikov.store.goodsservice.domain

data class OptimisticLockException(val id: Identifier, val currentVersion: Version, val versionInCommand: Version)
    : RuntimeException("Optimistic lock failed for id: $id, currentVersion: $currentVersion, version in command: $versionInCommand")

data class GoodAlreadyDeletedException(val id: Identifier)
    : RuntimeException("Good already deleted for id: $id")

data class GoodNotFoundException(val id: Identifier)
    : RuntimeException("Good not found for id: $id")