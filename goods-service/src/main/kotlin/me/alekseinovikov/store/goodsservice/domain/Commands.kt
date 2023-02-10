package me.alekseinovikov.store.goodsservice.domain

sealed class GoodsCommand(val optimisticVersion: Version) {
    data class Create(val name: String, val description: String) : GoodsCommand(Version(0UL))
    data class Update(val name: String, val description: String, val version: Version) : GoodsCommand(version)
    data class Delete(val version: Version) : GoodsCommand(version)
}
