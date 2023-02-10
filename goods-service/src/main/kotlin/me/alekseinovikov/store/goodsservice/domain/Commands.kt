package me.alekseinovikov.store.goodsservice.domain

sealed class Command(val optimisticVersion: Version) {
    data class CreateGood(val name: String, val description: String) : Command(Version(0UL))
    data class ChangeGoodUpdate(val name: String, val description: String, val version: Version) : Command(version)
    data class DeleteGood(val version: Version) : Command(version)
}
